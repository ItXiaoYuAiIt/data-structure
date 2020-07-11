package com.itxiaoyuaiit.learn.list;

/**
 * @ClassName Test
 * @Description TODO
 * @Author wuyuqing
 * @Date 2020/7/9 23:10
 * @Version 1.0
 */
public class Test {
    public static void main(String[] args) {
        MyArrayList<Integer> myArrayList = new MyArrayList<>();
        for (int i = 0; i < 100; i++) {
            myArrayList.add(i);
        }
        System.out.println(myArrayList.size());
        //System.out.println(myArrayList.get(-1));
        System.out.println(myArrayList.indexOf(72));
        myArrayList.remove(0);
        System.out.println(myArrayList.size());
        System.out.println(myArrayList.get(0));
        System.out.println(myArrayList.indexOf(72));
        System.out.println(15 >> 1);
    }

}
