package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Util {

    protected static final String DELIM = ";";
    private static final boolean ISWIN = isWin();

    protected static ArrayList<String> loadFile(String path) {
        ArrayList<String> array = new ArrayList<>();
        try {
            FileReader input = new FileReader(path);
            BufferedReader buffer = new BufferedReader(input);
            String line = buffer.readLine();
            while (line != null) {
                if (ISWIN) {
                    line = UTF8toISO(line);
                }
                array.add(line);
                line = buffer.readLine();
            }
            input.close();
        } catch (IOException ex) {
            System.out.println(path + " ... error!");
        }
        return array;
    }

    protected static void export(ArrayList<String> array, String path)
            throws IOException {
        System.out.println(path + " exist ... " + fileExists(path) + " ...");
        FileWriter output = new FileWriter(path);
        PrintWriter printWriter = new PrintWriter(output);
        for (int i = 0; i < array.size(); i++) {
            printWriter.printf("%s\n", array.get(i));
        }
        printWriter.close();
    }

    protected static Map<String, String> loadMap(String path) {
        Map<String, String> map = new HashMap<>();
        ArrayList<String> array = loadFile(path);
        array.forEach((String a) -> {
            String[] field = a.split(DELIM);
            if (field.length > 1) {
                map.put(field[0], field[1]);
            }
        });
        return map;
    }

    protected static boolean fileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    protected static String property(String path, String key)
            throws FileNotFoundException {
        Properties props = new Properties();
        try {
            FileInputStream fis = new FileInputStream(path);
            props.load(fis);
            fis.close();
        } catch (IOException ex) {
            System.out.println(path + " ... error!");
        }
        return (String) props.getProperty(key);
    }

    protected static String UTF8toISO(String str) {
        Charset utf8charset = Charset.forName("UTF-8");
        Charset iso88591charset = Charset.forName("ISO-8859-1");

        ByteBuffer inputBuffer = ByteBuffer.wrap(str.getBytes());
        // decode UTF-8
        CharBuffer data = utf8charset.decode(inputBuffer);
        // encode ISO-8559-1
        ByteBuffer outputBuffer = iso88591charset.encode(data);
        byte[] outputData = outputBuffer.array();

        return new String(outputData);
    }
    
    protected static boolean isWin() {
        String so = (String) System.getProperties().get("os.name");
        System.out.println("S.O.: " + so);
        return !so.equalsIgnoreCase("Linux");
    }
}
