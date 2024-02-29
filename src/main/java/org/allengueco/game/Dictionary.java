package org.allengueco.game;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;

import java.util.Random;

/**
 * Holds our valid words. Right now we use a {@link TreeSortedSet} to back our Dictionary, but a Trie data
 * structure might be more efficient here.
 */
public class Dictionary {
    private final TreeSortedSet<String> words;
    private final int size;
    private final Random random;

    public Dictionary() {
        this.words = TreeSortedSet.newSet();
        this.size = 0;
        this.random = new Random();
    }

    public Dictionary(TreeSortedSet<String> words, Random random) {
        this.words = words;
        this.size = words.size();
        this.random = random;
    }

    public boolean contains(String word) {
        return this.words.contains(word);
    }

    public String randomWord() {
        int randomIndex = random.nextInt(0, size);
        return this.words.selectWithIndex((word, index) -> index == randomIndex, Lists.mutable.empty()).getOnly();
    }
}
