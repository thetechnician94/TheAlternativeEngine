package Engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author tcraig01
 */
public class Setting {

    private File file;
    private ArrayList<String> keys;
    private ArrayList<String> values;
    private String delimiter = ":::";

    public Setting(File file, String delimeter) throws IOException {
        keys = new ArrayList();
        values = new ArrayList();
        this.file = file;
        if (!file.exists()) {
            file.createNewFile();
        } else {
            populate();
        }
    }

    public Setting(File file) throws IOException {
        keys = new ArrayList();
        values = new ArrayList();
        this.file = file;
        if (!file.exists()) {
            file.createNewFile();
        } else {
            populate();
        }
    }

    public String getValue(String key) {
        int index = getIndex(key);
        if (index < 0) {
            return null;
        }
        return values.get(index);
    }

    public boolean updateValue(String key, String value) throws FileNotFoundException {
        if (getIndex(key) < 0) {
            return false;
        }
        int index = getIndex(key);
        if (values.size() <= index) {
            values.add(value);
        } else {
            values.set(index, value);
        }
        return true;
    }

    private int getIndex(String key) {
        int index = 0;
        for (String findKey : keys) {
            if (key.equals(findKey)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public boolean addKey(String key) {
        if (keys.contains(key)) {
            return false;
        }
        keys.add(key);
        return true;
    }

    public boolean removeKey(String key) {
        if (keys.contains(key)) {
            keys.remove(key);
            return true;
        }
        return false;
    }

    private void populate() throws FileNotFoundException {
        Scanner fileScan = new Scanner(file);
        while (fileScan.hasNextLine()) {
            String[] line = fileScan.nextLine().split(delimiter, 0);
            keys.add(line[0]);
            if (line.length == 2) {
                values.add(line[1]);
            } else {
                values.add("");
            }
        }
        fileScan.close();
    }

    private void updateValues() throws FileNotFoundException {
        int index = 0;
        PrintStream printer = new PrintStream(file);
        for (String key : keys) {
            printer.print(key + delimiter + values.get(index) + "\n");
            index++;
        }
        printer.close();
    }

    public void writeChanges() throws FileNotFoundException {
        updateValues();

    }

    public void deleteFile() {
        file.delete();
    }

    public String[] getKeys() {
        return keys.toArray(new String[0]);
    }

    public String[] getValues() {
        return values.toArray(new String[0]);
    }
}
