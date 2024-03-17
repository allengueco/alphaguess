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

        assertIterableEquals(List.of(GUESS), guesses.getAfter());
        assertIterableEquals(List.of(), guesses.getBefore());
    }

    @Test
    void guessAfter() {
        final String ANSWER = "CASE";
        final String GUESS = "VASE";

        guesses.addGuess(ANSWER, GUESS);

        assertIterableEquals(List.of(GUESS), guesses.getBefore());
        assertIterableEquals(List.of(), guesses.getAfter());
    }

    @Test
    void guessCorrect() {
        final String ANSWER = "CORRECT";
        final String GUESS = "CORRECT";

        assertThrows(IllegalStateException.class, () -> guesses.addGuess(ANSWER, GUESS));

        assertTrue(guesses.getBefore().isEmpty());
        assertTrue(guesses.getAfter().isEmpty());
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

        assertTrue(guesses.getBefore().isEmpty());
        assertFalse(guesses.getAfter().isEmpty());
    }

    @Test
    void guessBeforeOrder() {
        final String ANSWER = "CASE";

        guesses.addGuess(ANSWER, "BASE");
        guesses.addGuess(ANSWER, "BOOST");
        guesses.addGuess(ANSWER, "ACORN");

        assertIterableEquals(List.of("ACORN", "BASE", "BOOST"), guesses.getAfter());
        assertTrue(guesses.getBefore().isEmpty());
    }
}