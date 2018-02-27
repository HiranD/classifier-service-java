package com.ClassifierService.DL4JApproach.tools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Dl4jUtils {

    public static void writeListToFile(List<String> list, String path){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            for (String str: list){
                writer.write(str);
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<String> loadListFromFile(String path){
        List<String> list = new ArrayList<String>();
        Scanner s = null;
        try {
            s = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (s.hasNext()){
            list.add(s.next());
        }
        s.close();
        return list;
    }
}
