package kr.co.goplan.mtgame.util;

import java.util.Collections;
import java.util.Vector;

public class util {
    
    /* 나중에 완성
    //enum Days {월, 화, 수, 목, 금, 토, 일}
    enum Days {MONDAY, 화, 수, 목, 금, 토, 일}
    Vector<Integer> dayList = new Vector<Integer>();

    int _dayOfWeek = 12;
    int[] a = {1,2,4,8,16,32,64};

    public void WeekDay(int _dayOfWeek) {
        for (int i = 0; i < Days.values().length - 1; i++) {

            *//*
             * 사이에 있는 값을 찾았으면, dayList에 작은 값을 저장하고 _dayOfWeek -= temp 수행후
             * i = 0초기화한다.
             *//*

//			int a = Integer.numberOfLeadingZeros(_dayOfWeek);

            if ((int) Math.pow(2, Days.MONDAY.ordinal()) + i <= _dayOfWeek && _dayOfWeek <= (int) Math.pow(2, Days.MONDAY.ordinal() + i + 1)) {
                for (int j = 0; j < a.length; j++) {
                    if (_dayOfWeek == a[j]) {
                        int temp = (int) Math.pow(2, Days.MONDAY.ordinal() + i + 1);
                        _dayOfWeek -= temp;

                        dayList.add((Integer) logX(temp, 2));
                        break;
                    }
                }

                int temp = (int) Math.pow(2, Days.MONDAY.ordinal() + i);
                _dayOfWeek -= temp;

                dayList.add((Integer) logX(temp, 2));
                i = 0;
            }

            if (_dayOfWeek == 0) {
                break;
            }

            if (_dayOfWeek == 1) {
                dayList.add((Integer) logX(_dayOfWeek, 2));
                break;
            }
        }

        //내림차순으로 정렬한다.
        Collections.sort(dayList);

        *//*
         * dayList = {0,1,4,6} 예를들면 이런 식으로 정렬되었을 것이다.
         * dayList와 Days를 이용해서
         * 요일을 구해야 한다.
         *//*

        Vector<String> vec = new Vector<String>();

        for (Integer integer : dayList) {
            for (Days day : Days.values()) {

                if (integer == day.ordinal()) {
                    vec.add(day.toString());
                    break;
                }
            }
        }
    }
*/
}
