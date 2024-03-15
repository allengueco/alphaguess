package org.allengueco.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuessesTest {
    Guesses guesses;

    @BeforeEach
    void setup() {
        guesses = Guesses.newGuesses();
    }

    @Test
    void guessBefore() {
        final String ANSWER = "CASE";
        final String GUESS = "BASE";

        guesses.addGuess(ANSWER, GUESS);

        assertIterableEquals(List.of(GUESS), guesses.getAfterGuesses());
        assertIterableEquals(List.of(), guesses.getBeforeGuesses());
    }

    @Test
    void guessAfter() {
        final String ANSWER = "CASE";
        final String GUESS = "VASE";

        guesses.addGuess(ANSWER, GUESS);

        assertIterableEquals(List.of(GUESS), guesses.getBeforeGuesses());
        assertIterableEquals(List.of(), guesses.getAfterGuesses());
    }

    @Test
    void guessCorrect() {
        final String ANSWER = "CORRECT";
        final String GUESS = "CORRECT";

        assertThrows(IllegalStateException.class, () -> guesses.addGuess(ANSWER, GUESS));

        assertTrue(guesses.getBeforeGuesses().isEmpty());
        assertTrue(guesses.getAfterGuesses().isEmpty());
    }

    @Test
    void guessCaseInsensitive() {
        final String ANSWER = "PACE";
        final String GUESS1 = "BLACK";
        final String GUESS2 = "blacK";

        Guesses.Result g1 = guesses.addGuess(ANSWER, GUESS1);
        Guesses.Result g2 = guesses.addGuess(ANSWER, GUESS2);

        assertEquals(Guesses.Result.ADDED, g1);
        assertEquals(Guesses.Result.ALREADY_GUESSED, g2);

        assertTrue(guesses.getBeforeGuesses().isEmpty());
        assertFalse(guesses.getAfterGuesses().isEmpty());
    }

    @Test
    void guessBeforeOrder() {
        final String ANSWER = "CASE";

        guesses.addGuess(ANSWER, "BASE");
        guesses.addGuess(ANSWER, "BOOST");
        guesses.addGuess(ANSWER, "ACORN");

        assertIterableEquals(List.of("ACORN", "BASE", "BOOST"), guesses.getAfterGuesses());
        assertTrue(guesses.getBeforeGuesses().isEmpty());
    }
}