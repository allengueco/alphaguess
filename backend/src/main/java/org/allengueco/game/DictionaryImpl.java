package org.allengueco.game;

import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;

/**
 * Holds our valid words. Right now we use a {@link TreeSortedSet} to back our Dictionary, but a Trie data
 * structure might be more efficient here.
 */
public class DictionaryImpl implements Dictionary {
    private final TreeSortedSet<String> words;
    private final int size;

    public DictionaryImpl() {
        this.words = TreeSortedSet.newSet();
        this.size = 0;
    }

    public DictionaryImpl(TreeSortedSet<String> words) {
        this.words = words;
        this.size = words.size();
    }

    @Override
    public boolean contains(String word) {
        return this.words.contains(word);
    }
}
