import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Autocorrect
 * <p>
 * A command-line tool to suggest similar words when given one not in the dictionary.
 * </p>
 * @author Zach Blick
 * @author Eisha Yadav
 */
public class Autocorrect {
    private String[] dict;
    private int threshold;

    /**
     * Constucts an instance of the Autocorrect class.
     * @param words The dictionary of acceptable words.
     * @param threshold The maximum number of edits a suggestion can have.
     */
    public Autocorrect(String[] words, int threshold) {
        this.dict = words;
        this.threshold = threshold;
    }

    /**
     * Runs a test from the tester file, AutocorrectTester.
     * @param typed The (potentially) misspelled word, provided by the user.
     * @return An array of all dictionary words with an edit distance less than or equal
     * to threshold, sorted by edit distnace, then sorted alphabetically.
     */
    public String[] runTest(String typed) {
        ArrayList<String[]> candidates = new ArrayList<>();
        if (Arrays.asList(dict).contains(typed)) {
            return new String[]{"WOW ur good at spelling"};
        }
        // Calculate Edit Distance using Levenshtien Distance
        for (String word : dict) {
            // Skip words unlikely to match through the two char overlap check before computing full edit distance
            if (!hasCharOverlap(typed, word)) continue;
            int dist = editDistance(typed, word);
            if (dist <= threshold) {
                candidates.add(new String[]{word, String.valueOf(dist)});
            }
        }

        candidates.sort((a, b) -> {
            int distA = Integer.parseInt(a[1]);
            int distB = Integer.parseInt(b[1]);
            if (distA != distB) return distA - distB;
            return a[0].compareTo(b[0]);
        });

        // Only return the three best matches
        int resultSize = Math.min(3, candidates.size());
        String[] result = new String[resultSize];
        for (int i = 0; i < resultSize; i++) {
            result[i] = candidates.get(i)[0];
        }
        return result;
    }

    // Checks if there is the two char overlap for efficiency
    private boolean hasCharOverlap(String a, String b) {
        // Words very different in length can't be within threshold edits
        if (Math.abs(a.length() - b.length()) > threshold) return false;
        // Single-char words skip  check
        if (a.length() < 2 || b.length() < 2) return true;

        HashSet<String> twoChars = new HashSet<>();
        for (int i = 0; i < a.length() - 1; i++) {
            twoChars.add(a.substring(i, i + 2));
        }
        for (int i = 0; i < b.length() - 1; i++) {
            if (twoChars.contains(b.substring(i, i + 2))) return true;
        }
        return false;
    }

    public static void main(String[] args){
        // Initialize
        String dict[] = loadDictionary("large");
        Autocorrect corrector = new Autocorrect(dict, 2);
        Scanner s = new Scanner(System.in);

        // Prompts for word
        System.out.println("Enter a word: ");
        String typed = s.nextLine();

        while(!typed.isEmpty()){
            String[] results = corrector.runTest(typed);
            // If no remaining words then either perfect match or completely wrong
            if(results.length == 0) System.out.println("No matches found.");
            else{
                System.out.println("Thinking...... ");
                for(String word: results){
                    System.out.println(word);
                }
            }
            System.out.println("Enter a word: ");
            typed = s.nextLine();
        }

    }

    public int editDistance(String wordFromDict, String word){

        // Create a tabulation table to store previous edit distance values
        int[][] table = new int[wordFromDict.length() + 1][word.length() + 1];

        // Iterate through word from dictionary
        for(int i = 0; i < wordFromDict.length()+1; i++){
            // Iterate through word being worked through
            for(int j = 0; j < word.length()+1; j++){
                // Edge cases
                // If one word is empty edit distance is length of other word
                if(i == 0) table[0][j] = j;
                else if(j == 0) table[i][0] = i;

                    // If heads are equal edit distance doesn't change
                else if(wordFromDict.charAt(i-1) == word.charAt(j-1)){
                    table[i][j] = table[i-1][j-1];
                }

                // Check the three cases, addition, deletion, and substitution
                else{
                    int substitution = table[i-1][j-1];
                    int deletion = table[i][j-1]; // Same as addition in theory
                    int add = table[i-1][j];
                    // Add the minimum value plus one to tabulate
                    table[i][j] = 1 + Math.min(Math.min(deletion, substitution), add);
                }
            }
        }
        // Return the final value
        return table[wordFromDict.length()][word.length()];
    }

    /**
     * Loads a dictionary of words from the provided textfiles in the dictionaries directory.
     * @param dictionary The name of the textfile, [dictionary].txt, in the dictionaries directory.
     * @return An array of Strings containing all words in alphabetical order.
     */
    private static String[] loadDictionary(String dictionary)  {
        try {
            String line;
            BufferedReader dictReader = new BufferedReader(new FileReader("dictionaries/" + dictionary + ".txt"));
            line = dictReader.readLine();

            // Update instance variables with test data
            int n = Integer.parseInt(line);
            String[] words = new String[n];

            for (int i = 0; i < n; i++) {
                line = dictReader.readLine();
                words[i] = line;
            }
            return words;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

