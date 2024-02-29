package org.allengueco.game;

import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

public class Guesses {
    public final Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;

    private final Set<String> before = TreeSortedSet.newSet(comparator);
    private final Set<String> after = TreeSortedSet.newSet(comparator);

    private Guesses() {
    }

    public static Guesses newGuesses() {
        return new Guesses();
    }

    @Override
    public String toString() {
        return "Guesses{" + "before=" + before + ", after=" + after + '}';
    }

    /**
     * Adds guess to either before or after guesses, according to the {@link Guesses#comparator}.
     *
     * @param answer answer to the game
     * @param guess  user's guess
     * @return true if guess was added, false if already exists in either set
     * @throws IllegalStateException if guess and answer are equal according to this.comparator.
     */
    public boolean addGuess(String answer, String guess) throws IllegalStateException {
        int result = this.comparator.compare(answer, guess);
        if (result == 0) {
            throw new IllegalStateException("Guess must not equal Answer at this point: guess=%s, answer=%s".formatted(guess, answer));
        }

        Set<String> half = result < 0 ? after : before;
        return half.add(guess);
    }

    public Collection<String> getBeforeGuesses() {
        return this.before;
    }

    public Collection<String> getAfterGuesses() {
        return this.after;
    }
}