package com.alioo.format.service.test.tree;

//给定一个数组，找出数组的峰值。返回其下标
public class FengDemo {


    public static int feng(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {

                return i;
            }
        }

        return arr.length - 1;
    }

    public static int feng2(int[] arr) {
        int low = 0;
        int high = arr.length - 1;

        while (low < high) {
            int mid = (low + high) / 2;
            if (arr[mid] > arr[mid + 1]) {
                high = mid;
            } else if (arr[mid] < arr[mid + 1]) {
                low = mid+1;
            }

        }


        return low;
    }


    public static void main(String[] args) {
        int[] arr = {3, 5, 7, 6, 2};

        int i = feng(arr);
        System.out.println(i);

        i = feng2(arr);
        System.out.println(i);




    }

}
