/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ayman2nov.sectools.codereview;

import com.ayman2nov.sectools.codereview.utils.FilesUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author ayman2nov
 */
public class Starter {

    static Properties keywords = new Properties();
    static Properties labels = new Properties();
    static ArrayList<String> ignore = new ArrayList<>();
    static InputStream input = null;
    static InputStream input2 = null;

    public static void main(String[] args) {
        loadProps();
        scanPath("java", "path");

//        final File folder = new File("");
//        PerformScan(folder);
    }

    private static void loadProps() {
        try {
            input = Starter.class.getClassLoader().getResourceAsStream("keywords.properties");
            keywords.load(input);

            input2 = Starter.class.getClassLoader().getResourceAsStream("labels.properties");
            labels.load(input2);

            ignore.add(".git");
            ignore.add(".idea");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input2 != null) {
                try {
                    input2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void PerformScan(final File folder, HashMap<String, String> keywords) {
        try {
            if (!ignore.contains(folder.getName())) {
                for (final File fileEntry : folder.listFiles()) {
                    if (fileEntry.isDirectory()) {
                        PerformScan(fileEntry, keywords);
                    } else {
                        Set<Entry<String, String>> entries = keywords.entrySet();
                        Iterator<Entry<String, String>> entrySetIterator = entries.iterator();
                        while (entrySetIterator.hasNext()) {
                            Entry entry = entrySetIterator.next();
                            String query = (String) entry.getValue();
                            ArrayList<Integer> lines = FilesUtils.searchForString(query, fileEntry);
                            if (lines.size() > 0) {
                                System.out.println("Potintial [" + getDescription((String) entry.getKey()) + "]" + " inside : " + fileEntry.getAbsolutePath() + " Lines " + lines);
//                         System.out.println("found "+lines + " inside : "+ fileEntry.getAbsolutePath() + " for hwyword "+query);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage() + " for " + folder.getAbsolutePath());
        }
    }

    private static void scanPath(String lang, String path) {
        HashMap<String, String> keywordsToScan = new HashMap<>();
        Enumeration<?> e = keywords.propertyNames();
        //load just the keywords for this project
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            if (key.startsWith(lang) || key.startsWith("all")) {
                String value = keywords.getProperty(key);
                keywordsToScan.put(key, value);
            }
        }
        //use loaded keywords to look search inside the code
        PerformScan(new File(path), keywordsToScan);

    }

    private static String getDescription(String key) {
        String[] keyword = key.split("\\.");
        if (keyword.length == 3) {
            return labels.getProperty(keyword[1]);
        } else {
            return key;
        }

    }

}
