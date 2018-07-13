package com.abhishek.personal;


import java.util.ArrayList;
import java.util.List;

public class MergeTwoBSTs {

    static class TreeNode {
        private int value;
        private TreeNode leftNode;
        private TreeNode rightNode;

        public TreeNode(int value) {
            super();
            this.value = value;
        }
        public int getValue() {
            return value;
        }
        public void setValue(int value) {
            this.value = value;
        }
        public TreeNode getLeftNode() {
            return leftNode;
        }
        public void setLeftNode(TreeNode leftNode) {
            this.leftNode = leftNode;
        }
        public TreeNode getRightNode() {
            return rightNode;
        }
        public void setRightNode(TreeNode rightNode) {
            this.rightNode = rightNode;
        }
    }

    public static TreeNode mergeBSTToFormBalancedTree(TreeNode tree1, TreeNode tree2){
        if(tree1 != null && tree2 != null){
            List<Integer> sortedList1 = new ArrayList<Integer>();
            List<Integer> sortedList2 = new ArrayList<Integer>();
            getSortedList(tree1, sortedList1);
            getSortedList(tree2, sortedList2);
            List<Integer> sortedList3 = merge(sortedList1, sortedList2);
            return createTree(sortedList3, 0, sortedList3.size() - 1);
        }
        else if(tree1 != null){
            return tree1;
        }
        else if(tree2 != null){
            return tree2;
        }
        return null;
    }

    // left, root, right( INORDER TRAVERSAL GIVES SORTED ORDER)
    private static void getSortedList(TreeNode node, List<Integer> sortedList){
        if(node != null){
            getSortedList(node.getLeftNode(), sortedList);
            sortedList.add(node.getValue());
            getSortedList(node.getRightNode(), sortedList);
        }
    }

    private static List<Integer> merge(List<Integer> sortedList1, List<Integer> sortedList2){
        List<Integer> mergeList = new ArrayList<Integer>(sortedList1.size() + sortedList2.size());
        int index1 = 0, index2 = 0;
        while(index1 < sortedList1.size() && index2 < sortedList2.size()){
            if(sortedList1.get(index1) <= sortedList2.get(index2)){
                mergeList.add(sortedList1.get(index1));
                index1++;
            } else {
                mergeList.add(sortedList2.get(index2));
                index2++;
            }
        }
        while(index1 < sortedList1.size()){
            mergeList.add(sortedList1.get(index1));
            index1++;
        }
        while(index2 < sortedList2.size()){
            mergeList.add(sortedList2.get(index2));
            index2++;
        }
        return mergeList;
    }

    // Create Tree using array
    private static TreeNode createTree(List<Integer> data, int begin, int end) {
        if (begin > end) {
            return null;
        }
        if (begin == end) {
            return new TreeNode(data.get(begin));
        }
        int size = end - begin + 1;
        int lSize = (size - 1) >> 1;
        TreeNode parent = new TreeNode(data.get(begin + lSize));
        parent.setLeftNode(createTree(data, begin, begin + lSize - 1));
        parent.setRightNode(createTree(data, begin + lSize + 1, end));
        return parent;
    }

    public static void printLevelWiseTree(List<TreeNode> nodes) {
        if(nodes == null || nodes.isEmpty()){
            return;
        }
        else{
            List<TreeNode> next = new ArrayList<TreeNode>();
            for(TreeNode node : nodes) {
                System.out.print(node.getValue()+" ");
                if(node.getLeftNode() != null){
                    next.add(node.getLeftNode());
                }
                if(node.getRightNode() != null){
                    next.add(node.getRightNode());
                }
            }
            System.out.println();
            printLevelWiseTree(next);
        }
    }

    public static void main(String[] args) {
        TreeNode tree1 = new TreeNode(90);
        tree1.setLeftNode(new TreeNode(70));
        tree1.setRightNode(new TreeNode(110));

        TreeNode tree2 = new TreeNode(60);
        tree2.setLeftNode(new TreeNode(5));
        tree2.setRightNode(new TreeNode(800));

        TreeNode balancedTree = mergeBSTToFormBalancedTree(tree1, tree2);
        List<TreeNode> list = new ArrayList<TreeNode>();
        list.add(balancedTree);
        printLevelWiseTree(list);
    }
}
