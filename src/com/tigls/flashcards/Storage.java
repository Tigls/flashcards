package com.tigls.flashcards;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    private final List<String> history = new ArrayList<>();
    private final Scanner scanner;

    public Storage(Scanner scanner) {
        this.scanner = scanner;
    }

    public void saveToFile(String filename, String key, String value, int mistakes) throws IOException {
        var file = new File(filename);
        var fwriter = new FileWriter(file, true);
        try (var writer = new PrintWriter(fwriter)) {
            if (key == null) {
                writer.println("");
            } else {
                writer.println(String.format("%s:%s:%d", key, value, mistakes));
            }

        }
        fwriter.close();
    }

    public void saveLog(String filename) throws IOException {
        var file = new File(filename);
        var fwriter = new FileWriter(file, true);
        try (var writer = new PrintWriter(fwriter)) {
            for (String line : history) {
                writer.println(line);
            }
        }
        fwriter.close();
    }

    public List<String> readFromFile(String filename) throws IOException {
        try (var reader = new Scanner(new File(filename))) {
            var data = new ArrayList<String>();
            while (reader.hasNextLine()){
                data.add(reader.nextLine());
            }
            return data;
        }
    }

    public void printOut(String str) {
        System.out.println(str);
        history.add(str);
    }

    public String readLine() {
        String str = scanner.nextLine();
        history.add(str);
        return str;
    }
}
