package com.alioo.format.service.test.tree.trie;

import java.util.Arrays;

public class Trie {
    char val;

    boolean isEnd = false;

    Trie[] subChildren = new Trie[26];

    public Trie(char val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "Trie{" +
                "val=" + val +
                ", isEnd=" + isEnd +
                ", subChildren=" + Arrays.toString(subChildren) +
                '}';
    }
}
