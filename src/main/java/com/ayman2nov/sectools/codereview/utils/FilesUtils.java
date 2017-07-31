/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ayman2nov.sectools.codereview.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ayman2nov
 */
public class FilesUtils {

    public FilesUtils() {
    }
    
    

    public static ArrayList<Integer> searchForString(String query, File file) {
        ArrayList<Integer> lines = new ArrayList<>();
        try {
            final Scanner scanner = new Scanner(file);
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                lineNumber++;
                final String lineFromFile = scanner.nextLine();
                if (lineFromFile.contains(query)) {
                    lines.add(lineNumber);
//                    System.out.println("I found " + query + " in file " + file.getName());
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FilesUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
}
