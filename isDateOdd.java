package com.javarush.task.task08.task0827;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.*;

/* 
Реализовать метод isDateOdd(String date) так, 
чтобы он возвращал true, если количество дней с начала года - нечетное число, 
иначе false
*/

public class isDateOdd {
    public static void main(String[] args) throws Exception{
        System.out.println(isDateOdd("JANUARY 2 2020"));
        
    }

    public static boolean isDateOdd(String date) throws Exception{
        int i = 0;
        DateFormat df = new SimpleDateFormat("MMMMM d yyyy", Locale.ENGLISH);
        Date dt = df.parse(date);
        Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		i = cal.get(6);
		if (i%2==0){
		    return false;
		}
		else return true;
    }
}
