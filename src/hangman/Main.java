package hangman;

import java.io.*;
import java.util.*;
import hangman.EmptyDictionaryException;


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
    Integer remainingGuesses;

    public static void main(String[] args) {
        String fileName = args[0];
        File dictionary = new File(fileName);
        Integer wordLength = Integer.parseInt(args[1]); // >= 2
        Integer remainingGuesses = Integer.parseInt(args[2]); // >= 1

        // Initialize game
        EvilHangmanGame evilHangmanGame = new EvilHangmanGame();

        // Start!
        try {
            evilHangmanGame.startGame(dictionary, wordLength);
        } catch(IOException | EmptyDictionaryException e) {
            System.out.println("Error found!");
            e.printStackTrace();
        }

        ArrayList<Character> guessedLetters = new ArrayList<>();
        while (remainingGuesses > 0) {
            userInput();
            remainingGuesses--;
        }
    }

    private void userInput() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("REMAINING GUESSES: " + remainingGuesses);
            System.out.println("Enter guess:");
            String guess = reader.readLine();
            while (guess.length() > 1 || Character.isLetter(guess.charAt(0))) {
                if (guess.length() > 1)
                    System.out.println("Guess must be one letter.");
                if (Character.isLetter(guess.charAt(0)))
                    System.out.println("Guess must be an uppercase or lowercase letter.");
                guess = reader.readLine();
            }
            guess = guess.toLowerCase();
            remainingGuesses--;
        } catch (IOException e) {
            System.out.println("Bad input!");
            e.printStackTrace();
        }
    }
}
