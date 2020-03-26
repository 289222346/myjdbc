package com.myjdbc.jdbc.test;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyTest {
    public static void main(String[] args0) throws NoSuchMethodException, IOException {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(5);
        list.add(8);
        list.add(9);
        list.add(100);
        list.add(125);
        list.add(158);
        int num = 18;
        List l2 = findMinniDiff(list, num);
        for (Object o : l2) {
            System.out.println(o);
        }
    }

    static List<Integer> findMinniDiff(List<Integer> list, int num) {
        List l2 = new ArrayList();
        Integer z1 = null;
        Integer z2 = null;
        int c1 = 0;
        int c2 = 0;
        for (int i = 0; i < list.size(); i++) {
            int x1 = list.get(i);
            if (x1 == num) {
                if (i != 0) {
                    c1 = list.get(i - 1);
                    z1 = Math.abs(c1 - num);
                }
                if (i < list.size() - 1) {
                    c2 = list.get(i + 1);
                    z2 = Math.abs(c2 - num);
                }
                break;
            } else if (x1 > num) {
                if (i != 0) {
                    c1 = list.get(i - 1);
                    z1 = Math.abs(c1 - num);
                }
                c2 = x1;
                z2 = Math.abs(c2 - num);
                break;
            }
        }
        if (z1 != z2) {
            if (z1 < z2) {
                l2.add(c1);
            } else {
                l2.add(c2);
            }
        } else {
            l2.add(c1);
            l2.add(c2);
        }
        return l2;
    }


}
