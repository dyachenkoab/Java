package com.javarush.task.task09.task0923;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/* 
Написать программу, которая вводит с клавиатуры строку текста.
Программа должна вывести на экран две строки:
1. первая строка содержит только гласные буквы из введённой строки.
2. вторая - только согласные буквы и знаки препинания из введённой строки.
Буквы соединять пробелом, каждая строка должна заканчиваться пробелом.
*/

public class Vowel {
    public static char[] vowels = new char[]{'а', 'я', 'у', 'ю', 'и', 'ы', 'э', 'е', 'о', 'ё'};

    public static void main(String[] args) throws Exception {
        //напишите тут ваш код
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String s = reader.readLine();
        char[] chs = new char[s.length()];
        chs = s.toCharArray();
        ArrayList<Character> lst1 = new ArrayList<>();
        ArrayList<Character> lst2 = new ArrayList<>();
//        char[][] ch = new char[s.length()][2];
  
                for (int j = 0; j < s.length(); j++){
                    if (isVowel(chs[j])){
                        lst1.add(chs[j]);
                    }
                    else if (chs[j] != ' '){
                        lst2.add(chs[j]);
                    }
                }
                for(Character a : lst1){
                    System.out.print(a + " ");
                }
                System.out.println();
                for(Character b : lst2){
                    System.out.print(b + " ");
                }
            }

    // метод проверяет, гласная ли буква
    public static boolean isVowel(char c) {
        c = Character.toLowerCase(c);  // приводим символ в нижний регистр - от заглавных к строчным буквам
        for (char d : vowels) {  // ищем среди массива гласных
            if (c == d) {
                return true;
            }
        }
        return false;
    }
}
