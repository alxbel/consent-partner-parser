package com.github.alxbel.dzoprs;

import java.io.IOException;

public class AppRunner {
    public static void main(String[] args) throws IOException {
        FileReader fileReader = new FileReader();
        CsvParser csvParser = new CsvParser();
        FileWriter fileWriter = new FileWriter();

        csvParser.setConsentPartnerLines(fileReader.extractCsvConsentPartnerLines());
        csvParser.initConsentPartnerColumnNumbers();
        csvParser.initConsentPartnerColumnMappings();
        csvParser.initConsentPartnerColumnValues();

        fileWriter.writeToFile("query.sql", csvParser.generateSqlInsert());
        //fileWriter.writeToFile("partners.json", csvParser.generatePartnersJson());
    }
}
