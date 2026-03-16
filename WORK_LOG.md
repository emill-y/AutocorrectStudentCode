Date
Time
Update
Feb 24th
8:00-8:45 pm
This session was spent thinking about how to tackle this problem from a structural perspective, since that is what was emphasized. Honestly, I spent a large time just reading through the problem set and understanding the source code. I realized that I should prioritize getting one word decoded first prior to creating scaffolding.
Feb 25th
5:15-5:45 pm
I spent some more time working on understanding the general structure of the problem set, and then thinking more specifically about how the number of error (edit distance) algorithm would work. I realized that the edit distance was reliant on a couple of different factors, and got slightly confused on how certain words with the same edit distance could be prioritized.
Feb 25th
5:45-6:15 pm
I decided to wait to learn the specificities of the edit distance algorithm before diving into implementation. I felt like rushing into code without fully understanding the algorithm would just lead to more confusion, so I took this time to read more carefully and sit with the problem.
Feb 26th
7:30-8:15 pm
I came back to the edit distance algorithm today feeling more grounded. I started sketching out the tabulation approach, basically a 2D table where each cell represents the edit distance between prefixes of the two words. I found it helpful to think through small examples by hand first, like comparing "cat" to "bat," before translating that logic into code. I'm starting to feel more confident about how the base cases (empty strings) and the three operations (addition, deletion, substitution) map onto the table.
Feb 27th
9:00-9:15 pm
Short session tonight, but somewhat productive. I worked through the indexing logic more carefully, specifically making sure I wasn't going out of bounds when accessing characters. I realized the table dimensions need to account for the empty string prefix cases, which tripped me up for a bit. I have a clearer sense now of how i and j correspond to characters in each word.
Feb 28th
8:30-9:15 pm
I started actually writing the editDistance method tonight. Getting the nested loop structure down felt satisfying, iterating through both words and filling in the table cell by cell. I ran into a bug with my index alignment (off-by-one errors are genuinely frustrating), and spent a chunk of time debugging that. By the end I had something that at least compiled, even if it's not fully correct yet.
Feb 29th
9:15-9:45 pm
I kept working on debugging the editDistance method. I traced through the logic manually for a few word pairs and identified that my edge case handling (when i or j equals 0) was being checked in the wrong order relative to the character comparison. I restructured the conditionals so the boundary cases are handled before the character check, which made more logical sense.
Feb 29th
9:45-10:15 pm
I tested the method more thoroughly and it's producing more reasonable values now, though I still need to verify edge cases like identical words (should return 0) and completely different words. I also started thinking ahead to how runTest will use editDistance. I'll need to loop over the whole dictionary, collect words within the threshold, and then sort by edit distance and then alphabetically. That sort feels like the next thing to figure out.
Feb 29th
10:15-10:45 pm
I wrapped up tonight by cleaning up the code and adding comments to editDistance so my logic is documented. I also started stubbing out the full runTest method. Right now it just calls editDistance on a hardcoded word, but I'm thinking through how to store candidates. An ArrayList probably makes sense since I don't know how many words will fall within the threshold ahead of time. 


March 2nd
7:00–7:45 pm
I came back after a few days away and reviewed what I had so far. The editDistance method feels solid at this point, and runTest is working for basic cases. I started thinking about how to make the spell checker faster in practice, since looping over the entire dictionary for every word felt inefficient. I remembered reading about bigram indexing as a filtering technique and wanted to explore that more seriously. The idea of checking for shared two-character substrings before computing the full edit distance seemed like a natural optimization that wouldn't break correctness.
March 2nd
7:45–8:30 pm
I started implementing the hasCharOverlap method. The basic idea is to build a HashSet of all consecutive two-character substrings from the typed word, then scan through the dictionary word's bigrams and check for any match. I also added the early length-difference check, since if two words differ in length by more than the threshold, they can't possibly be within that edit distance. Getting both filters working together took some careful thought, but by the end of the session I had a version that seemed to prune the candidate pool significantly.
March 7th
6:15–7:00 pm
Tonight I focused on testing hasCharOverlap more carefully to make sure I wasn't accidentally filtering out valid matches. I worked through some edge cases like single-character words and words that are just one edit apart but share no bigrams. I added the short-circuit for words under two characters so those always pass through to the full edit distance check. I also ran the full test suite and checked that my results still matched expected outputs now that the filter was in place.
March 7th
7:00–7:45 pm
I started thinking about how to expose the autocorrect logic as a server rather than just a command-line tool. I looked into Java's built-in HttpServer from com.sun.net.httpserver and was surprised by how lightweight it is. I sketched out the structure of AutocorrectServer.java — basically just spinning up a server on port 8000, parsing the query parameter from the URL, calling corrector.runTest(), and returning a JSON array. I got a basic version running locally and confirmed it was responding correctly in the browser.
March 10th
8:00–8:50 pm
I spent this session building out the frontend HTML file. I wanted it to feel like a real document editor with live spell checking, similar to Google Docs. I used a contenteditable div as the editor area and set up a debounced input listener so it waits a bit after the user stops typing before firing off spell check requests. The core loop fetches suggestions for each unique word and rebuilds the innerHTML with red wavy underlines on misspelled ones. Getting the caret to not jump around after innerHTML gets rewritten was genuinely annoying and took most of the session.
March 11th
7:30–8:15 pm
I finished up the context menu feature tonight. Right-clicking a red-underlined word now shows a small dropdown with the top suggestions returned from the server, plus options to ignore the word or add it to a personal dictionary for the session. Clicking a suggestion swaps the word in place and reruns spell check. I also handled the sentinel value my server returns when the word is correct so that correctly spelled words don't get flagged. Getting the menu to close on any outside click or keypress took a small amount of cleanup.
March 15th
5:00–5:45 pm
I did a full end-to-end pass today — starting the Java server, opening the HTML frontend, and typing through a range of misspelled words to see how everything held together. Most things worked well. I noticed the server was being hit once per unique word per keystroke cycle which felt reasonable. I cleaned up the CORS header handling in AutocorrectServer and made sure the JSON response was being built correctly without any edge case crashes on empty results.
March 15th
5:45–6:30 pm
Final cleanup session. I went through both files and added comments explaining the less obvious parts, like why the bigram filter exists and how the caret bookmark/restore logic works in the frontend. I also reflected on the overall architecture — the separation between the core Autocorrect logic, the HTTP server wrapper, and the frontend felt clean. The bigrams optimization meaningfully reduces how often the expensive O(m×n) edit distance gets called, which was the main performance goal I set out to hit.

