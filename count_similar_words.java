package com.javarush.task.task10.task1016;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* 
Подсчёт одинаковых слов в списке
*/

public class count_similar_words {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        ArrayList<String> words = new ArrayList<String>();

        String s = "";
        
        while(true){
            s = reader.readLine();
            if (s != null){words.add(s);}
            else break;
        }

        Map<String, Integer> map = countWords(words);
        for (Map.Entry<String, Integer> pair : map.entrySet()) {
            System.out.println(pair.getKey() + " " + pair.getValue());
        }
     }

    public static Map<String, Integer> countWords(ArrayList<String> list) {
        HashMap<String, Integer> result = new HashMap<String, Integer>();

        for (int i = 0; i < list.size(); i++){
            if(result.containsKey(list.get(i))){
                result.compute(list.get(i),(key, val) -> {if (val == null){
                    return 1;
                } else { 
                    return val + 1;
                }});
            }
            else result.put(list.get(i), 1);
        }
        return result;
    }

}

