/**
 * The object which will create the dictionary, make guesses (by checking the dictionary and returning the
 * new set), and storing the guessed letters.
 */

package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    private Set<String> possibleWords = new HashSet<>(); // #####  'B'  #####
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
            if (!possibleWords.contains(word) && word.length() == wordLength) {
                possibleWords.add(word);
            }
        }

        // Check if dictionary is empty. If so, throw error.
        if (possibleWords.isEmpty()) {
            throw new EmptyDictionaryException();
        }

        // Begin game.
        System.out.println("Welcome to Hangman!");
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        // This is where the evil algorithm happens!
        if (guessedLetters.contains(guess)) {
            throw new GuessAlreadyMadeException();
        } else {
            guessedLetters.add(guess);
        }

        // Divide the possible words into word groups.
        Map<String, Set<String>> wordGroups = new HashMap<>();
        for (String possibleWord : possibleWords) {
            String foundGroupKey = null;
            // First, check if there's a key in the map that matches the same pattern as our word.
            for (Map.Entry<String, Set<String>> wordGroup : wordGroups.entrySet()) {
                String key = wordGroup.getKey();
                if (checkKey(guess, possibleWord, key)) {
                    foundGroupKey = key;
                    break;
                }
            }

            // If a word was found, create a new set and add it to the map. Else add it to an existing set.
            if (foundGroupKey == null) {
                Set<String> newSet = new HashSet<>();
                newSet.add(possibleWord);
                wordGroups.put(possibleWord, newSet);
            } else {
                wordGroups.get(foundGroupKey).add(possibleWord);
            }
        }

        // Find smallest Set
        Set<String> largestGroup = null;
        for (Map.Entry<String, Set<String>> wordGroup : wordGroups.entrySet()) {
            if (largestGroup == null) {
                largestGroup = wordGroup.getValue();
            }
            else if (wordGroup.getValue().size() > largestGroup.size()) {
                largestGroup = wordGroup.getValue();
            }
            else if (wordGroup.getValue().size() == largestGroup.size()) {
                // choose the letter where the group doesn't appear
                // else choose the one with the fewest letters
                // else Choose the one with the most right-most letter
            }
        }

        guessedLetters.add(guess);
        possibleWords = largestGroup;

        return possibleWords;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    private boolean checkKey(char guess, String possibleWord, String key) {
        for (int i = 0; i < key.length(); i++) {
            if (
                (possibleWord.toCharArray()[i] == guess && key.toCharArray()[i] != guess) ||
                (possibleWord.toCharArray()[i] != guess && key.toCharArray()[i] == guess)
            )
                return false;
        }
        return true;
    }
}
