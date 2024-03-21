package org.allengueco.game;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.allengueco.GuessesSerializer;
import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;

import java.util.Set;

@JsonSerialize(using = GuessesSerializer.class)
public record Guesses(Set<String> before, Set<String> after) {
    public static Guesses empty() {
        return new Guesses(TreeSortedSet.newSet(), TreeSortedSet.newSet());
    }

    public Mutate mutate() {
        return new Mutate(this);
    }

    public static class Mutate {
        Set<String> before;
        Set<String> after;

        public Mutate(Set<String> before, Set<String> after) {
            this.before = before;
            this.after = after;
        }

        public Mutate(Guesses guesses) {
            this(guesses.before, guesses.after);
        }


        public Mutate withBefore(Set<String> before) {
            this.before = before;
            return this;
        }

        public Mutate withAfter(Set<String> after) {
            this.after = after;
            return this;
        }

        public Guesses build() {
            return new Guesses(before, after);
        }
    }
}