package com.github.alxbel.dzoprs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
    public List<String> readFileByLine(String filename) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(reformat(line));
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        return lines;
    }

    public List<String> readResourceByLine(String resourceName) {
        List<String> lines = new ArrayList<>();

        InputStream in = getClass().getResourceAsStream("/" + resourceName);
        return getStrings(lines, in);
    }

    public List<String> extractCsvConsentPartnerLines() {
        List<String> lines = new ArrayList<>();

        InputStream in = getClass().getResourceAsStream("/consent_partner.csv");
        return getStrings(lines, in);
    }

    private List<String> getStrings(List<String> lines, InputStream in) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            // read line by line
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(reformat(line));
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        return lines;
    }

    private String reformat(String string) {
        return string.replaceAll("'", "\"").replaceAll("«", "\"").replaceAll("»", "\"");
    }
}
