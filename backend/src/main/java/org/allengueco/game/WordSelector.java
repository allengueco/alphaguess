package org.allengueco.game;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;

import java.util.Random;

public class WordSelector {
    TreeSortedSet<String> validWords;
    int size;
    Random random;

    public WordSelector(TreeSortedSet<String> words, Random random) {
        this.validWords = words;
        this.size = words.size();
        this.random = random;
    }

    public String randomWord() {
        int randomIndex = random.nextInt(0, size);
        return this.validWords.selectWithIndex((word, index) -> index == randomIndex, Lists.mutable.empty()).getOnly();
    }

}
