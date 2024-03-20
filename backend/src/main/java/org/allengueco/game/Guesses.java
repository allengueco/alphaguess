package org.allengueco.game;

import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Guesses implements Serializable {
    private Set<String> before = TreeSortedSet.newSet(String.CASE_INSENSITIVE_ORDER);

    private Set<String> after = TreeSortedSet.newSet(String.CASE_INSENSITIVE_ORDER);

    public Guesses() {

    }

    public static Guesses empty() {
        return new Guesses();
    }

    @Override
    public String toString() {
        return "Guesses{" + "before=" + before + ", after=" + after + '}';
    }

    /**
     * Adds guess to either before or after guesses, according to the {@link String#CASE_INSENSITIVE_ORDER}.
     *
     * @param answer answer to the game
     * @param guess  user's guess
     * @return
     * @throws IllegalStateException if guess and answer are equal according to this.comparator.
     */
    public Result addGuess(String answer, String guess) throws IllegalStateException {
        int result = String.CASE_INSENSITIVE_ORDER.compare(answer, guess);
        if (result == 0) {
            return Result.EQUAL;
        }

        Set<String> half = result > 0 ? after : before;
        boolean added = half.add(guess);
        return added ? Result.ADDED : Result.ALREADY_GUESSED;
    }

    public List<String> getBefore() {
        return this.before.stream().toList();
    }

    public void setBefore(Set<String> before) {
        this.before = before;
    }

    public List<String> getAfter() {
        return this.after.stream().toList();
    }

    public void setAfter(Set<String> after) {
        this.after = after;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guesses guesses = (Guesses) o;
        return Objects.equals(getBefore(), guesses.getBefore()) && Objects.equals(getAfter(), guesses.getAfter());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBefore(), getAfter());
    }

    public enum Result {
        EQUAL,
        ALREADY_GUESSED,
        ADDED
    }
}