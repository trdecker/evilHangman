/**
 * @File: Main.java
 * @Author: Tad Decker
 * @Date: 9-13-2022
 * ...
 * C S 240
 * Rodham
 */

package hangman;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

/**
 * 1. Args: String dictionary Integer >=2 wordLength Integer >= 1 guesses
 * 2. Begin with a SET of all English words (derived from the given dictionary)
 * 3. Run game. While (guessesLeft > 0):
 *      a. Display number of remaining guesses, alphabetized list of words picked thus far, and
 *         the partially corrected words
 *      b. Prompt user for next guess. If not letter, or already guessed that letter, display
 *         error and prompt.
 *      c. If the CURRENT word contains the letter, report number. Else guessesLeft-- and dipslay messsage.
 *         If ALL WORDS in the list contain the letter, say "yes there are /number/ /letter/s", and display
 *         the LEAST NUMBER OF TIMES IT APPEARS IN A WORD. (example, for 'ham', 'banana', and 'car', display 1)
 * 4. When the user runs out of guesses, pick a list from the word list and display it. If
 *    the user wins, display "You win!" and display the correct word.
 * ...
 * ...
 * ...
 * THE EVIL ALGORITHM
 * 1. Begin with the set of English words from the input file (args[0]) (SET B)
 * 2. Create subset of words of length wordLength (args[1]) of set B (SET L)
 * 3. Each time user enters a word:
 *      a. partition the wordList (B) into "words groups" based on the positions of the guessed letter
 *         in the word. (See example)
 *      b. Choose the largest word group to replace L
 *         If two groups are the same size:
 *            1. Choose the group that letter DOESN'T appear in
 *            2. else if each group has the letter, choose the one with the LEAST
 *            3. else if choose the group with the most right-most letter
 *            4. else choose the group with the next most right-most letter and REPEAT STEP 4
 */
public class Main {
    public static void main(String[] args) {
        String fileName = args[0];
        File dictionary = new File(fileName);
        int wordLength = Integer.parseInt(args[1]); // >= 2
        int remainingGuesses = Integer.parseInt(args[2]); // >= 1

        StringBuilder answer = new StringBuilder(wordLength);
        answer.append("_".repeat(Math.max(0, wordLength)));

        // Initialize game
        EvilHangmanGame evilHangmanGame = new EvilHangmanGame();

        // Start!
        try {
            evilHangmanGame.startGame(dictionary, wordLength);
        } catch(IOException | EmptyDictionaryException e) {
            System.out.println("Error found!");
            e.printStackTrace();
        }

        boolean success = true;
        Set<String> possibleWords = new HashSet<>();

        while (remainingGuesses > 0) {
            boolean correctLetter = false;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("You have " + remainingGuesses + " left");
                // Print guessed letters
                SortedSet<Character> guessedLetters = evilHangmanGame.getGuessedLetters();
                StringBuilder usedLetters = new StringBuilder();
                usedLetters.append("Used letters:");
                for (Character letter : guessedLetters) {
                    usedLetters.append(" ").append(letter);
                }
                System.out.println(usedLetters);
                // Print word
                System.out.println("Word: " + answer);

                System.out.println("Enter guess:");
                String guess;

                // Iterate until user submits a Character that is valid.
                boolean validLetter;
                do {
                    guess = reader.readLine();
                    guess = guess.toLowerCase();
                    if (guess.length() == 0) {
                        System.out.println("Invalid input");
                        validLetter = false;
                        continue;
                    }
                    else if (guess.length() > 1 || !Character.isLetter(guess.charAt(0))) {
                        System.out.println("Invalid input");
                        validLetter = false;
                        continue;
                    }
                    // Check for the letter. This will also update the Set<String> currWords(?).
                    try {
                        possibleWords = evilHangmanGame.makeGuess(guess.charAt(0));
                        String key = possibleWords.toArray()[0].toString();
                        // Update the answer
                        for (int i = 0; i < key.length(); i++) {
                            if (key.substring(i, i + 1).equals(guess)) {
                                answer.replace(i,i+1,guess);
                                correctLetter = true;
                            }
                        }
                        validLetter = true;
                    } catch(GuessAlreadyMadeException e) {
                        System.out.println("You already used that letter");
                        validLetter = false;
                    }

                    success = false;
                    for (Character letter : answer.toString().toCharArray()) {
                        if (letter != '_') {
                            success = true;
                        }
                        else {
                            success = false;
                            break;
                        }
                    }
                    if (success) break;

                    // Output if bad guess
                    if (!correctLetter && validLetter)
                        System.out.println("Sorry, there are no " + guess + "'s");
                }
                while (!validLetter);
            } catch (IOException e) {
                System.out.println("Bad input!");
                e.printStackTrace();
            }

            if (success) break;

            remainingGuesses--;
        }

        if (!success) {
            System.out.println("You lose!");
            System.out.println("The word: " + possibleWords.toArray()[0].toString());
        }
        else {
            System.out.println("Congrats! You win.");
        }
    }
}
