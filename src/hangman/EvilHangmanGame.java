package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;

public class EvilHangmanGame implements IEvilHangmanGame {
    Set<String> words = new HashSet<>();
    Set<Character> guessedWords = new HashSet<>();

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        // Scan file for words
        Scanner scanner = new Scanner(dictionary);
        while (scanner.hasNext()) {
            String word = scanner.next();
            if (!words.contains(word)) {
                words.add(word);
            }
        }

        // Check if dictionary is empty. If so, throw error.
        if (words.isEmpty())
            throw new EmptyDictionaryException();

        // Begin game.
        System.out.println("Welcome to Hangman!");
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        return null;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return null;
    }
}
