package com.alioo.format.service.test.tree.trie;

public class TrieSynchronizedDemo {

    Trie root = new Trie(' ');

    public void insert(String word) {

        Trie node = root;
        for (int i = 0; i < word.length(); i++) {

            char c = word.charAt(i);
            int idx = c - 65;

            if (node.subChildren[idx] == null) {
                synchronized (node) {
                    if (node.subChildren[idx] == null) {
                        Trie sub = new Trie(c);
                        node.subChildren[idx] = sub;
                    }
                }

            }
            node = node.subChildren[idx];
        }
        node.isEnd = true;

    }

    public boolean exist(String word) {

        Trie node = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int idx = c - 65;

            if (node.subChildren[idx] == null) {
                return false;
            }
            node = node.subChildren[idx];
        }

        return node.isEnd;
    }


    public static void main(String[] args) {
        TrieSynchronizedDemo demo = new TrieSynchronizedDemo();
        demo.insert("HELLO");
        System.out.println(demo.root);
        boolean f = demo.exist("HELLO");
        System.out.println(f);
    }

}


