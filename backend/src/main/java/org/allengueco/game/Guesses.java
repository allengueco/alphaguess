package org.allengueco.game;

import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public record Guesses(
        Set<String> before,
        Set<String> after) {
    public static Guesses empty() {
        return new Guesses(TreeSortedSet.newSet(), TreeSortedSet.newSet());
    }

    public static Guesses from(List<Guess> g) {
        var before = new TreeSet<String>();
        var after = new TreeSet<String>();

        for (var guess : g) {
            switch (guess.position) {
                case BEFORE -> before.add(guess.word);
                case AFTER -> after.add(guess.word);
            }
        }

        return new Guesses(before, after);
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