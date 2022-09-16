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

    /**
     * Guess a character. This will return a set of words, which are the new words which the evilHangMan game
     * will use for the next guess.
     * @param guess The character being guessed, case insensitive
     *
     * @return The new set of words--none of which have the desired letter, or just the least amount possible.
     * @throws GuessAlreadyMadeException If the guess has already been mad, throw this!
     */
    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        // This is where the evil algorithm happens!
        if (guessedLetters.contains(guess)) {
            throw new GuessAlreadyMadeException();
        } else {
            guessedLetters.add(guess);
        }

        // Divide the possible words into word groups.
        Map<String, Set<String>> wordGroups = partitionWords(guess);

        // Find the largest word.
        possibleWords = findLargest(wordGroups, guess);

        return possibleWords;
    }

    /**
     * Return the guessed letters in a sorted array.
     * @return The guessed letters--but sorted!
     */
    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    /**
     * Check if the two keys have the guessed character in the same slots.
     * @param guess the character to guess
     * @param possibleWord the word that we're checking
     * @param key the word that we're checking against
     * @return true if in the same spots
     */
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

    /**
     * Partition the words from possible words by which slots have the letter 'guess' in it.
     * @param guess the letter we're guessing with.
     * @return Map wordGroups, which contains sets of strings.
     */
    private Map<String, Set<String>> partitionWords(Character guess) {
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
        return wordGroups;
    }

    /**
     * Within wordGroups, there are several smaller sets of words. We want to return the one with the highest count,
     * and if they are equal, according to a criteria described within.
     * @param wordGroups The map of all the groups of words we created.
     * @param guess The letter we're accounting for.
     * @return Set of strings, the value of the entry with the highest count in the map given.
     */
    private Set<String> findLargest(Map<String, Set<String>> wordGroups, Character guess) {
        // ##### FIND LARGEST SET #####
        Map.Entry<String,Set<String>> largestGroup = null;
        for (Map.Entry<String, Set<String>> currGroup : wordGroups.entrySet()) {
            if (largestGroup == null) {
                largestGroup = currGroup;
            }
            else if (currGroup.getValue().size() > largestGroup.getValue().size()) {
                largestGroup = currGroup;
            }
            else if (currGroup.getValue().size() == largestGroup.getValue().size()) {
                String keyOld = largestGroup.getKey();
                String keyNew = currGroup.getKey();
                // else choose the one with the fewest letters
                int letterCountOld = 0;
                int letterCountNew = 0;
                for (Character letter : keyOld.toCharArray()) {
                    if (letter == guess)
                        letterCountOld++;
                }
                for (Character letter : keyNew.toCharArray()) {
                    if (letter == guess)
                        letterCountNew++;
                }
                if (letterCountNew < letterCountOld)
                    largestGroup = currGroup;
                // else Choose the one with the most right-most letter
                else if (letterCountNew == letterCountOld) {
                    int oldIndex = keyOld.length()-1;
                    int newIndex = keyNew.length()-1;
                    for (int i = 0; i < keyOld.length(); i++) {
                        if (keyOld.charAt(i) == guess) {
                            oldIndex = i;
                            break;
                        }
                    }
                    for (int i = 0; i < keyOld.length(); i++) {
                        if (keyNew.charAt(i) == guess) {
                            newIndex = i;
                            break;
                        }
                    }
                    if (newIndex > oldIndex)
                        largestGroup = currGroup;
                    else if (newIndex == oldIndex) {
                        do {
                            for (; oldIndex < keyOld.length(); oldIndex++) {
                                if (keyOld.charAt(oldIndex) == guess) {
                                    break;
                                }
                            }
                            for (; newIndex < keyOld.length(); newIndex++) {
                                if (keyNew.charAt(newIndex) == guess) {
                                    break;
                                }
                            }
                        } while (oldIndex == newIndex);
                    }

                }

            }
        }
        assert largestGroup != null;
        return largestGroup.getValue();
    }
}
