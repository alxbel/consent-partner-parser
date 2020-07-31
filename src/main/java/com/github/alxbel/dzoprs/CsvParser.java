package com.github.alxbel.dzoprs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvParser {
    private static final String SQL_INSERT_FORMAT = "insert into CONSENT_PARTNER " +
            "(ID, CODE, NAME, DESCRIPTION, LAST_CHANGE_DATE, START_DATE, ADDRESS, IS_IN_GROUP, IS_ACTIVE, VERSION, INN, OGRN, SITE, LEGAL_NAME, LOGO)\n" +
            "VALUES (CONSENT_PARTNER_SEQ.nextval,\n" +
            "        '%s',\n" +
            "        '%s',\n" +
            "        '%s',\n" +
            "        CURRENT_TIMESTAMP,\n" +
            "        CURRENT_TIMESTAMP,\n" +
            "        '%s',\n" +
            "        %s,\n" +
            "        1,\n" +
            "        1,\n" +
            "        '%s',\n" +
            "        '%s',\n" +
            "        '%s',\n" +
            "        '%s',\n" +
            "        '%s');\n\n";

    private static final String PARTNERS_JSON_FORMAT = "" +
            "    {\n" +
            "        \"id\": %s,\n" +
            "        \"code\": \"%s\",\n" +
            "        \"name\": \"%s\",\n" +
            "        \"description\": \"%s\",\n" +
            "        \"lastChangeDate\": \"20180409163909\",\n" +
            "        \"startDate\": \"20180409163909\",\n" +
            "        \"address\": \"%s\",\n" +
            "        \"isInGroup\": true,\n" +
            "        \"version\": \"1\",\n" +
            "        \"inn\": \"%s\",\n" +
            "        \"ogrn\": \"%s\",\n" +
            "        \"site\": \"%s\",\n" +
            "        \"legalName\": \"%s\",\n" +
            "        \"logo\": \"\"\n" +
            "    },\n";

    private List<String> consentPartnerLines = new ArrayList<>();
    private final List<List<String>> consentPartnerColumnValues = new ArrayList<>();
    private final Map<String, Integer> consentPartnerColumnNumbers = new HashMap<>();
    private final Map<String, String> consentPartnerColumnMappings = new HashMap<>();


    public void initConsentPartnerColumnNumbers() {
        if (consentPartnerLines.size() > 1) {
            String[] columnArr = splitConsentPartnerLine(0);
            for (int i = 0; i < columnArr.length; i++) {
                consentPartnerColumnNumbers.put(columnArr[i].trim(), i);
            }
        }
    }

    public void initConsentPartnerColumnValues() {
        for (int lineNm = 1; lineNm < consentPartnerLines.size(); lineNm++) {
            String[] columnValuesArr = splitConsentPartnerLine(lineNm);
            for (int colNum = 0; colNum < columnValuesArr.length; colNum++) {
                columnValuesArr[colNum] = columnValuesArr[colNum].trim();
                if (columnValuesArr[colNum].equalsIgnoreCase("true")) {
                    columnValuesArr[colNum] = "1";
                }
            }
            consentPartnerColumnValues.add(Arrays.asList(columnValuesArr));
        }
    }

    public void initConsentPartnerColumnMappings() {
        InputStream in = getClass().getResourceAsStream("/consent_partner_column_mappings.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in));) {
            // read line by line
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(":");
                consentPartnerColumnMappings.put(columns[0], columns[1]);
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public void initConsentPartnerColumnMappings(List<String> mappings) {
        mappings.forEach(m -> {
            String[] columns = m.split(":");
            consentPartnerColumnMappings.put(columns[0], columns[1]);
        });
    }

    public String generateSqlInsert() {
        StringBuilder query = new StringBuilder();
        for (List<String> record : consentPartnerColumnValues) {
            query.append(String.format(SQL_INSERT_FORMAT,
                    record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("CODE"))).replaceAll("\\s+", "_"),
                    record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("NAME"))),
                    record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("DESCRIPTION"))),
                    record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("ADDRESS"))),
                    record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("IS_IN_GROUP"))),
                    record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("INN"))),
                    record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("OGRN"))),
                    record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("SITE"))),
                    record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("LEGAL_NAME"))),
                    record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("LOGO")))
            ));
        }
        return query.toString();
    }

    public String generatePartnersJson() {
        StringBuilder json = new StringBuilder();
        Integer id = 0;
        for (List<String> record : consentPartnerColumnValues) {
            json.append(String.format(PARTNERS_JSON_FORMAT,
                    id++,
                    record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("CODE"))),
                    reformat4Json(record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("NAME")))),
                    reformat4Json(record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("DESCRIPTION")))),
                    reformat4Json(record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("ADDRESS")))),
                    record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("INN"))),
                    record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("OGRN"))),
                    record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("SITE"))),
                    reformat4Json(record.get(consentPartnerColumnNumbers.get(consentPartnerColumnMappings.get("LEGAL_NAME"))))));
        }
        return json.toString();
    }

    public String generateSqlInsert(int index) {
        StringBuilder query = new StringBuilder("insert into CONSENT_PARTNER\n");


        List<String> record = consentPartnerColumnValues.get(index);


        return query.toString();
    }

    public void debugInfo() {
        System.out.println("### FIELD_NUMBERS ###");
        consentPartnerColumnNumbers.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
        System.out.println();
        System.out.println("### CONSENT_PARTNER_COLUMN_MAPPINGS ###");
        consentPartnerColumnMappings.forEach((k, v) -> {
            System.out.println(k + "->" + v);
        });
        System.out.println();
        System.out.println("### COLUMN_VALUES ###");
        consentPartnerColumnValues.forEach(rec -> {
            rec.forEach(col -> System.out.printf("%s ", col));
            System.out.println();
        });
    }

    public List<String> getConsentPartnerLines() {
        return consentPartnerLines;
    }

    public void setConsentPartnerLines(List<String> consentPartnerLines) {
        this.consentPartnerLines = consentPartnerLines;
    }

    public Map<String, Integer> getConsentPartnerColumnNumbers() {
        return consentPartnerColumnNumbers;
    }

    public Map<String, String> getConsentPartnerColumnMappings() {
        return consentPartnerColumnMappings;
    }

    private String[] splitConsentPartnerLine(int lineNm) {
        return consentPartnerLines.get(lineNm).split("\\|");
    }

    private String reformat4Json(String string) {
        return string.replaceAll("\"", "\\\\\"");
    }
}
