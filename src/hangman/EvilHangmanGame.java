/**
 * The object which will create the dictionary, make guesses (by checking the dictionary and returning the
 * new set), and storing the guessed letters.
 */

package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    private final Set<String> possibleWords = new HashSet<>();
    private final SortedSet<Character> guessedLetters = new TreeSet<>();

    /**
     * Start the game. Read the dictionary, and save the words within the set of words.
     * If the dictionary is empty, throw an exception.
     * @param dictionary Dictionary of words to use for the game
     * @param wordLength Number of characters in the word to guess
     */
    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        // Scan file for words
        Scanner scanner = new Scanner(dictionary);
        while (scanner.hasNext()) {
            String word = scanner.next();
            if (!possibleWords.contains(word)) {
                possibleWords.add(word);
            }
        }

        // Check if dictionary is empty. If so, throw error.
        if (possibleWords.isEmpty())
            throw new EmptyDictionaryException();

        // Begin game.
        System.out.println("Welcome to Hangman!");
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        Set<String> newWords = new HashSet<>();
        // This is where the evil algorithm happens!
        return possibleWords;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }
}
