package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {

    protected static final String DELIM = ";";
    //private static final boolean ISWIN = isWin();

    protected static ArrayList<String> loadFile(String path) {
        ArrayList<String> array = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        // ler arquivo
        try {
            FileInputStream input = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(input, "UTF8");
            try (Reader in = new BufferedReader(isr)) {
                int ch;
                while ((ch = in.read()) > -1) {
                    buffer.append((char) ch);
                }
            }
            System.out.println(path + " ok ...");
        } catch (IOException e) {
            System.err.println(path + " error ...");
            return null;
        }
        // Converter String em ArrayList
        String line = "";
        String text = buffer.toString();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                array.add(line);
                line = "";
            } else {
                line += c;
            }
        }
        // Adicionar última linha
        if (!line.equals("")) {
            array.add(line);
        }
        return array;
    }

    protected static void export(String strOut, String path) {
        try {
            FileOutputStream output = new FileOutputStream(path);
            try (Writer out = new OutputStreamWriter(output, "UTF8")) {
                out.write(strOut);
            }
            System.out.println("exported " + path + " ...");
        } catch (IOException e) {
            System.err.println("export " + path + " error ...");
        }
    }

    protected static void export(ArrayList<String> array, String path) {
        String strOut = "";
        for (int i = 0; i < array.size(); i++) {
            strOut += array.get(i) + '\n';
        }
        export(strOut, path);
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

    protected static boolean createDirectory(String directoryName) {
        File newDirectory = new File(directoryName);
        if (!newDirectory.exists()) {
            newDirectory.mkdir();
            return false;
        }
        return true;
    }

    @SuppressWarnings("ConvertToTryWithResources")
    protected static String property(String path, String key)
            throws FileNotFoundException {
        Properties props = new Properties();
        try {
            FileInputStream fis = new FileInputStream(path);
            props.load(fis);
            fis.close();
        } catch (IOException ex) {
            System.err.println(path + " ... error!");
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

    protected static String timeNow() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    protected static String userDir() {
        return System.getProperty("user.dir");
    }

    protected static String filename(String path) {
        File file = new File(path);
        return file.getName();
    }
}
