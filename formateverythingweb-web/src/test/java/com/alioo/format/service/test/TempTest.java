//package com.alioo.format.service.test;
//
//import com.alioo.util.FileUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TempTest {
//
//    public static void main(String[] args) throws Exception {
//
//
//        List<String> list = FileUtil.readFile2List("/Users/alioo/Downloads/data.txt");
//        System.out.println(list);
//
//        List<Byte> list2 = new ArrayList<>();
//        for (String line : list) {
//            String[] arr = line.split(" ");
//            for (String one : arr) {
//                if (one.length() != 2) {
//                    System.out.println("one=" + one + "=");
//                    break;
//                }
//
//                Integer one2 = Integer.valueOf(one, 16);
////                byte one3 = (byte) (one2 - 128);
//
//                byte one3 = 0;
//                if (one2 < 128) {
//                    one3 = (byte) one2.intValue();
//                } else {
//                    one3 = (byte) (one2.intValue() - 128);
//
//                }
//                list2.add(one3);
//            }
//        }
//
//        System.out.println("list2=" + list2);
//        System.out.println("list2=" + list2.size());
//
//        byte[] arr3 = new byte[list2.size()];
//        for (int i = 0; i < list2.size(); i++) {
//            Byte b = list2.get(i);
//            arr3[i] = b;
//        }
//
//        String str = new String(arr3);
//        System.out.println("str=" + str);
//
////        {
////            final BASE64Encoder encoder = new BASE64Encoder();
////            final BASE64Decoder decoder = new BASE64Decoder();
////            final String text = "字串文字";
////            final byte[] textByte = text.getBytes("UTF-8");
//////编码
////            final String encodedText = encoder.encode(textByte);
////            System.out.println(encodedText);
//////解码
////            System.out.println(new String(decoder.decodeBuffer(encodedText), "UTF-8"));
////        }
//
//
//        byte[] arr4 = Base64.decode(arr3);
//        System.out.println("arr4=" + arr4);
//
//    }
//
//    public static int consecutiveNumbersSum3(int N) {
//        int sum = 0;
//        int i = 1;
//        int ans = 0;
//        while (sum < N) {
//            if ((N - sum) % i == 0) {
//                ans++;
//            }
//            i++;
//            sum += i - 1;
//        }
//        return ans;
//    }
//
//
//}