package com.alioo.format.service.test.extend;

public class ExtendDemo {

    public static void main(String[] args) {

        AA a=new BB();
        a.show();


        BB b=new BB();
        b.show();

    }

}

class AA {
    public void show(){
        System.out.println("aa");
    }
}

class BB extends AA {
    public void show(){
        System.out.println("bb");
    }
}


//final abstract class CC {
//    public void show(){
//        System.out.println("aa");
//    }
//}