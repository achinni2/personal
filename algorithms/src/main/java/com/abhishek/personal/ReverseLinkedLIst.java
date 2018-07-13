package com.abhishek.personal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class ReverseLinkedList {
    public ListNode reverseList(ListNode head) {
        if(head == null || head.next == null)
            return head;
        List<Integer> nums= new LinkedList<Integer>();
        ListNode prev = head;
        ListNode curr = head.next;
        ListNode tmp = new ListNode(0);
        while(curr != null)
        {
            tmp = curr;
            curr = curr.next;
            tmp.next = prev;
            prev = prev.next;
        }

        return tmp;

    }


}

