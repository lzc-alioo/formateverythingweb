package com.alioo.format.service.test.tree;

public class Solution2 {


    //使用前序遍历序列化二叉树
    int dep(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = dep(root.left);
        int right = dep(root.right);

        int max = Math.max(left+1, right+1);
        return max;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(3);
        root.right = new TreeNode(7);

        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(4);

        root.left.right.right = new TreeNode(4);

        Solution2 solution = new Solution2();

        int max = solution.dep(root);
        System.out.println("max=" + max);


    }
}

