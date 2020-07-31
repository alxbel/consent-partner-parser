package com.github.alxbel.dzoprs;

import java.io.BufferedWriter;
import java.io.IOException;

public class FileWriter {
    public void writeToFile(String fileName, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(fileName));
        writer.write(content);
        writer.close();
    }
}
