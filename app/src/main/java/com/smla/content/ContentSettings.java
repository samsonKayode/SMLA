package com.smla.content;

import com.smla.model.LibraryModel;
import com.smla.sessions.Utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by SAMSON KAYODE on 11-Sep-2017.
 */

public class ContentSettings {

    Utilities utx = new Utilities();

    StringBuilder text;

    public static int resx = 0;


    public String translate(String file, String word) {

        String rt = "";

        File nfile = new File(file);
        Scanner scanner = null;

        try {
            scanner = new Scanner(nfile);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(word)) {
                    //System.out.println(line);
                    String[] parts = line.split("#");

                    rt = parts[1];

                } else {
                    //rt= "NIL";
                }

            }
            scanner.close();

        } catch (FileNotFoundException e) {

            System.out.println("Error finding file.." + e);
        }


        return rt;
    }

    //Confirm if Lessonsn are pinned..

    public int pinConfirm(String myLoc) {

        int res = 1;

        File fl = new File(myLoc);

        if (fl.exists()) {

            res = 0;
        } else {

            res = 1;
        }

        return res;
    }

    //Load courses..

    public List loadFiles(String location) {

        List<String> results = new ArrayList<String>();
        int sn = 0;
        try

        {

            File[] files = new File(location).listFiles();
            //If this pathname does not denote a directory, then listFiles() returns null.

            Arrays.sort(files, new AlphanumFileComparator());

            for (File file : files) {
                if (file.isDirectory()) {
                    //String a = stripExtension(String.valueOf(file.getName()));
                    sn++;
                    String a = file.getName();

                    String sna = String.valueOf(sn);

                    if (a.equals("QUIZ")) {

                        //Not adding the quiz folder.

                    } else {
                        results.add(sna + "/" + a);
                    }


                } else {
                    //Load
                }
            }
        } catch (Exception nn) {

            System.out.println("ERRR");

        }

        return results;

    }

    //Load NonFormal..

    public List loadNonFormal(String location) {

        List<String> results = new ArrayList<String>();
        int sn = 0;
        try

        {
            File[] files = new File(location).listFiles();
            Arrays.sort(files, new AlphanumFileComparator());

            for (File file : files) {
                if (file.isDirectory()) {

                }else{

                    String a = file.getName();

                    if(a.endsWith(".mp4")||a.endsWith(".MP4")){

                        String ax = stripExtension(String.valueOf(file.getName()));
                        sn++;
                        String sna = String.valueOf(sn);

                        results.add(sna + "/" + ax);

                    }
                }
            }
        } catch (Exception nn) {

            System.out.println("ERROR LOADING NON FORMAL FILES "+nn);
        }

        return results;

    }

    //Create PIN Files..

    public void newPinFile(File myLoc) {

        PrintWriter fw = null;
        try {
            fw = new PrintWriter(myLoc);
            BufferedWriter bw = new BufferedWriter(new FileWriter(myLoc));
            bw.write("LESS1 #EMPTY");
            bw.newLine();
            bw.write("LESS2 #EMPTY");
            bw.newLine();
            bw.write("LESS3 #EMPTY");
            bw.newLine();
            bw.write("LESS4 #EMPTY");
            bw.newLine();
            bw.write("LESS5 #EMPTY");
            bw.newLine();
            bw.write("LESS6 #EMPTY");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    //Replace Text..

    public int replaceText(String nfile, String word, String newText) {

        int res = 1;

        try {
            File file = new File(nfile);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "", oldtext = "";
            while ((line = reader.readLine()) != null) {

                oldtext += line + "\r\n";


            }
            reader.close();

            //To replace a line in a file
            String newtext = oldtext.replaceAll(word, newText);

            FileWriter writer = new FileWriter(nfile);
            writer.write(newtext);
            writer.close();

            res = 0;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            res = 1;
        }

        return res;

    }


    public String findExist(String file, String word) {

        String rt = "";

        File nfile = new File(file);
        Scanner scanner = null;

        try {
            scanner = new Scanner(nfile);
            //now read the file line by line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(word)) {

                    rt = line;
                } else {
                    //rt= "NIL";
                }

            }
            scanner.close();
        } catch (FileNotFoundException e) {

            //handle this
        }

        return rt;
    }

    //Load Quiz Data..

    public List loadQuizFiles(String location) {

        List<String> results = new ArrayList<String>();
        int sn = 0;
        try

        {

            File[] files = new File(location).listFiles();
            //If this pathname does not denote a directory, then listFiles() returns null.

            Arrays.sort(files, new AlphanumFileComparator());

            for (File file : files) {
                if (file.isFile()) {

                    sn++;

                    String a = utx.stripExtension(String.valueOf(file.getName()));

                    String sna = String.valueOf(sn);

                    results.add(sna + "/" + a);

                } else {
                    //Load
                }
            }
        } catch (Exception nn) {

            System.out.println("ERRR");

        }

        return results;

    }

//Count quiz files available.

    public static int countQuizFile(String dirPath) {
        File f = new File(dirPath);
        File[] files = f.listFiles();

        if (files != null)
            for (int i = 0; i < files.length; i++) {

                //resx++;

                File file = files[i];

                if (file.isFile()) {
                    resx++;
                }

                if (file.isDirectory()) {
                    countQuizFile(file.getAbsolutePath());
                }
            }

        return resx;
    }

    public int countLibrary(String location) {

        int sn = -1;
        try

        {

            File[] files = new File(location).listFiles();
            Arrays.sort(files, new AlphanumFileComparator());

            for (File file : files) {

                if (file.isFile()) {

                    sn++;
                }
            }
        } catch (Exception nnn) {

            System.out.println("Error counting library files "+nnn);

            sn=-1;

        }

        return sn;
    }

    //Strip Extensions..

    public String stripExtension (String str) {
        // Handle null case specially.

        if (str == null) return null;

        // Get position of last '.'.

        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.

        if (pos == -1) return str;

        // Otherwise return the string, up to the dot.

        return str.substring(0, pos);
    }

    public ArrayList<LibraryModel> loadLibrary(String location){

        ArrayList<LibraryModel> results = new ArrayList<LibraryModel>();

        String fileName="", fName="";
        String note="";
        String type="";
        int sn =0;
        try

        {
            File[] files = new File(location).listFiles();
            Arrays.sort(files, new AlphanumFileComparator() );

            for (File file : files) {

                if (file.isFile()) {

                    sn++;

                    System.out.println("Document Found "+sn);

                    fName = stripExtension(String.valueOf(file.getName()));
                    LibraryModel lMode = new LibraryModel();

                    fileName = file.getName();

                    if(fileName.contains(".MP4")||fileName.contains(".mp4")){
                        type="Video";
                    }

                    if(fileName.contains(".JPG")||fileName.contains(".jpg")||fileName.contains(".PNG")||fileName.contains(".png")){
                        type="Image";
                    }

                    if(fileName.contains(".GIF")||fileName.contains(".gif")){
                        type="Animation";
                    }

                    if(fileName.contains(".PDF")||fileName.contains(".pdf")){
                        type="Document";
                    }

                    //get content of notes..
                    try{

                        String noteLoc = location +"/"+fName+"_note.txt";

                        File fx = new File(noteLoc);

                        if(fx.exists()){

                            //note = new String(Files.readAllBytes(Paths.get(noteLoc)));

                            text = new StringBuilder();

                            try {
                                BufferedReader br = new BufferedReader(new FileReader(fx));
                                String line;

                                while ((line = br.readLine()) != null) {
                                    text.append(line);
                                    text.append('\n');
                                }
                                br.close();
                            }
                            catch (IOException e) {
                                //You'll need to add proper error handling here
                            }
                        }

                        else{
                            System.out.println("Note file does not exist");
                            note="INFO UNAVIALABLE";
                        }

                    }
                    catch(Exception fnf){

                    }

                    if(fName.contains("_note")){

                    }else{

                        lMode.setTitle(fName);
                        lMode.setWithExtension(fileName);
                        lMode.setDescription(text.toString());
                        lMode.setType(type);

                        results.add(lMode);
                    }

                }
                else{
                    //it is a file..
                }
            }
        }

        catch(Exception nn){

            System.out.println("Error loading Library files.." +nn);

            nn.printStackTrace();

        }

        return results;

    }

    //Load profile list...


}
