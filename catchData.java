package com.javarush.task.task09.task0921;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/* 
Написать программу, которая будет вводить числа с клавиатуры.
Код по чтению чисел с клавиатуры должен быть в методе readData.
Код внутри readData обернуть в try..catch.
Если пользователь ввёл какой-то текст, вместо ввода числа, то метод должен перехватить исключение и вывести на экран
все ранее введенные числа в качестве результата.
Числа выводить с новой строки сохраняя порядок ввода.
*/

public class catchData {
    public static void main(String[] args) {
        readData();
    }

    public static void readData() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String s = "";
        ArrayList<Integer> al = new ArrayList<>();
        try {
        while(true){
            s = reader.readLine();
            if(s == " ") { break; }
            al.add(Integer.parseInt(s));
            }
        }
        catch(Exception e){
                for (Integer i : al){
                    System.out.println(i);
                }
            }
        }
    }
