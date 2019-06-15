package com.alioo.format.service.test.tree;

public class Solution {
    //使用前序遍历序列化二叉树
    String Serialize(TreeNode root) {
        StringBuffer sb = new StringBuffer();
        if (root == null) {
            //空节点（#）
            sb.append("#,");
            return sb.toString();
        }
        sb.append(root.val + ",");
        sb.append(Serialize(root.left));
        sb.append(Serialize(root.right));
        return sb.toString();
    }

    int index = -1;
    //反序列化：根据某种遍历方式得到的序列化字符串结果，重构二叉树
    TreeNode Deserialize(String str) {
        String val[] = str.split(",");

        TreeNode root = Deserialize1(val, index);

//        index++;
//
//        if (val[index].equals("#")) {
//            return null;
//        }
//        TreeNode root = new TreeNode(Integer.parseInt(val[index]));
//        root.left = Deserialize(str);
//        root.right = Deserialize(str);
//
//        Solution solution = new Solution();
//        String str2 = solution.Serialize(root);
//        System.out.println("str2.tmp=" + str2+"(index:"+index+")");

        System.out.println("index+"+index);
        return root;
    }


    TreeNode Deserialize1(String val[], int index) {
        index++;

        if (val[index].equals("#")) {
            return null;
        }
        TreeNode root = new TreeNode(Integer.parseInt(val[index]));
        root.left = Deserialize1(val, index);
        root.right = Deserialize1(val, index);

        Solution solution = new Solution();
        String str2 = solution.Serialize(root);
        System.out.println("str2.tmp=" + str2+"(index:"+index+")");

        return root;
    }


    public static void main(String[] args) {
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(3);
        root.right = new TreeNode(7);

        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(4);

        Solution solution = new Solution();

        String str = solution.Serialize(root);
        System.out.println("str=" + str);

        TreeNode root2 = solution.Deserialize(str);
        String str2 = solution.Serialize(root2);
        System.out.println("str2=" + str2);

    }
}

class TreeNode {
    int val = 0;
    TreeNode left = null;
    TreeNode right = null;

    public TreeNode(int val) {
        this.val = val;

    }

}