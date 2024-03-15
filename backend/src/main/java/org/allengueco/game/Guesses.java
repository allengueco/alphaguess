package org.allengueco.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;
import org.springframework.boot.jackson.JsonObjectDeserializer;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
@JsonSerialize(keyAs = String.class, contentAs = List.class)
@JsonDeserialize(keyAs = String.class, contentAs = List.class)
public class Guesses {
    @JsonIgnore
    public final Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;

    private final Set<String> before = TreeSortedSet.newSet(comparator);
    private final Set<String> after = TreeSortedSet.newSet(comparator);

    public Guesses() {
    }

    public static Guesses newGuesses() {
        return new Guesses();
    }

    public static Guesses withGuesses(Collection<String> before, Collection<String> after) {
        Guesses newGuesses = newGuesses();

        newGuesses.before.addAll(before);
        newGuesses.after.addAll(after);

        return newGuesses;
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
     * @return
     * @throws IllegalStateException if guess and answer are equal according to this.comparator.
     */
    public Result addGuess(String answer, String guess) throws IllegalStateException {
        int result = this.comparator.compare(answer, guess);
        if (result == 0) {
            return Result.EQUAL;
        }

        Set<String> half = result > 0 ? after : before;
        boolean added = half.add(guess);
        return added ? Result.ADDED : Result.ALREADY_GUESSED;
    }

    public Collection<String> getBeforeGuesses() {
        return this.before;
    }

    public Collection<String> getAfterGuesses() {
        return this.after;
    }

    public Comparator<String> comparator() {
        return this.comparator;
    }

    public enum Result {
        EQUAL,
        ALREADY_GUESSED,
        ADDED
    }
}