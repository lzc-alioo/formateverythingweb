package com.alioo.leecode;

import java.util.ArrayList;
import java.util.List;

public class LeeCode56 {

    public static void main(String[] args) {
        //[[1,3],[2,6],[8,10],[15,18]]

        List<Interval> intervals = new ArrayList<>();
//        intervals.add(new Interval(1, 3));
//        intervals.add(new Interval(2, 6));
//        intervals.add(new Interval(8, 10));
//        intervals.add(new Interval(15, 18));
//        intervals.add(new Interval(4, 9));

        intervals.add(new Interval(1, 4));
        intervals.add(new Interval(5, 6));
        intervals.add(new Interval(0, 8));

        List<Interval> newlist = new LeeCode56().merge(intervals);

    }
    public List<Interval> merge(List<Interval> intervals) {

        List<Interval> newlist = new ArrayList<>();

        while (!intervals.isEmpty()) {
            dosth(intervals, newlist);
        }

        System.out.printf("newlist=" + newlist);
        return newlist;
    }


    void dosth(List<Interval> intervals, List<Interval> newlist) {


        Interval obj = intervals.get(0);

        List<Interval> removelist = new ArrayList<>();
        removelist.add(obj);

        for (int j = 1; j < intervals.size(); j++) {

            if (obj.end >= intervals.get(j).start && obj.end <=intervals.get(j).end ) {
                obj.end =  intervals.get(j).end ;

                removelist.add(intervals.get(j));
            }
            if (obj.start >= intervals.get(j).start && obj.start <= intervals.get(j).end) {
                obj.start =  intervals.get(j).start ;

                removelist.add(intervals.get(j));
            }

            if (obj.start <= intervals.get(j).start && obj.end >= intervals.get(j).end ) {

                removelist.add(intervals.get(j));
            }

        }


        intervals.removeAll(removelist);

        newlist.add(obj);

//        System.out.println("tmpnewlist=" + newlist);

    }


}


class Interval {
    public int start;
    public int end;

    Interval() {
        start = 0;
        end = 0;
    }

    Interval(int s, int e) {
        start = s;
        end = e;
    }

    @Override
    public String toString() {
        return "Interval{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
