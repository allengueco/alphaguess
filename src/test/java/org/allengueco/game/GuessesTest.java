package org.allengueco.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuessesTest {
    Guesses guesses;

    @BeforeEach
    void setup () {
        guesses = Guesses.newGuesses();
    }

    @Test
    void guessBefore() {
        final String ANSWER = "CASE";
        final String GUESS = "BASE";

        guesses.addGuess(ANSWER, GUESS);

        assertIterableEquals(List.of(GUESS), guesses.getBeforeGuesses());
        assertIterableEquals(List.of(), guesses.getAfterGuesses());
    }

    @Test
    void guessAfter() {
        final String ANSWER = "CASE";
        final String GUESS = "VASE";

        guesses.addGuess(ANSWER, GUESS);

        assertIterableEquals(List.of(GUESS), guesses.getAfterGuesses());
        assertIterableEquals(List.of(), guesses.getBeforeGuesses());
    }

    @Test
    void guessCorrect() {
        final String ANSWER = "CORRECT";
        final String GUESS = "CORRECT";

        assertThrows(IllegalStateException.class, () -> guesses.addGuess(ANSWER, GUESS));

        assertTrue(guesses.getAfterGuesses().isEmpty());
        assertTrue(guesses.getBeforeGuesses().isEmpty());
    }

    @Test
    void guessCaseInsensitive() {
        final String ANSWER = "PACE";
        final String GUESS1 = "BLACK";
        final String GUESS2 = "blacK";

        boolean g1 = guesses.addGuess(ANSWER, GUESS1);
        boolean g2 = guesses.addGuess(ANSWER, GUESS2);

        assertTrue(g1);
        assertFalse(g2);

        assertTrue(guesses.getAfterGuesses().isEmpty());
        assertFalse(guesses.getBeforeGuesses().isEmpty());
    }

    @Test
    void guessBeforeOrder() {
        final String ANSWER = "CASE";

        guesses.addGuess(ANSWER, "BASE");
        guesses.addGuess(ANSWER, "BOOST");
        guesses.addGuess(ANSWER, "ACORN");

        assertIterableEquals(List.of("ACORN", "BASE", "BOOST"), guesses.getBeforeGuesses());
        assertTrue(guesses.getAfterGuesses().isEmpty());
    }
}