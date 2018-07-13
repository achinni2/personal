package com.abhishek.personal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class KthLargestElementHeap {
    private static int L;
    public int findKthLargest(int[] nums, int k)
    {
        heapSort(nums);
        return nums[nums.length - k];
    }

    private void heapSort(int[] nums)
    {
        buildHeap(nums);
        for(int i = L;i >=1; i--)
        {
            swap(nums, 0, i);
            L = L - 1;
            heapify(nums, 0);
        }
    }

    private void buildHeap(int[] nums)
    {
        L = nums.length-1;
        for(int i = L/2; i >=0; i--)
            heapify(nums, i);
    }

    //max-heap
    private void heapify(int[] nums, int i)
    {
        int l = 2 * i;
        int r = l + 1;
        int largest = i;
        if((l <= L) && (nums[l] > nums[largest]))
            largest = l;
        if((r <= L) && (nums[r] > nums[largest]))
            largest = r;
        if(largest != i)
        {
            swap(nums, i, largest);
            heapify(nums, largest);
        }
    }

    private void swap(int[] arr, int a, int b)
    {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }


    public static int[] stringToIntegerArray(String input) {
        input = input.trim();
        input = input.substring(1, input.length() - 1);
        if (input.length() == 0) {
            return new int[0];
        }

        String[] parts = input.split(",");
        int[] output = new int[parts.length];
        for(int index = 0; index < parts.length; index++) {
            String part = parts[index].trim();
            output[index] = Integer.parseInt(part);
        }
        return output;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = in.readLine()) != null) {
            int[] nums = stringToIntegerArray(line);
            line = in.readLine();
            int k = Integer.parseInt(line);

            int ret = new KthLargestElementHeap().findKthLargest(nums, k);

            String out = String.valueOf(ret);

            System.out.print(out);
        }
    }
}
