package com.alioo.format.service.test.algorithm;

public class MaxArry {


    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        int[] array = {-2, 11, -14, 13, 3, -5, -5, -2};
        int result = maxSunArray(array);
        System.out.println("连续子数组之最大和为：" + result);
    }

    public static int maxSunArray(int[] array) {

        int sum = 0, max = 0;

        for (int i = 0; i < array.length; i++) {
            if (sum <= 0) {
                sum = array[i];
            } else {
                sum += array[i];
            }
        }

        if (sum > max) {
            max = sum;
        }

        return max;

    }
}
