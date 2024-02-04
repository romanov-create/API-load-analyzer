package com.example.apiloadanalyzer.service;

import com.example.apiloadanalyzer.model.ApiRequest;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvParserService {
    public List<ApiRequest> parseCsv(MultipartFile file) throws IOException, CsvValidationException {
        List<ApiRequest> apiRequests = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new BufferedReader(new InputStreamReader(file.getInputStream())))) {
            String headerLine = csvReader.readNext()[0];

            if (headerLine == null) {
                return apiRequests;
            }

            String[] header = headerLine.split(";");

            int ipIndex = indexOf(header, "IP");
            int dateIndex = indexOf(header, "DATA");
            int methodIndex = indexOf(header, "REQUEST-METHOD");
            int uriIndex = indexOf(header, "URI");
            int statusIndex = indexOf(header, "REQUEST-STATUS");

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                String[] lineArr = line[0].split(";");
                ApiRequest apiRequest = new ApiRequest();

                apiRequest.setIp(lineArr[ipIndex]);
                apiRequest.setDate(lineArr[dateIndex]);
                apiRequest.setRequestMethod(lineArr[methodIndex]);
                apiRequest.setUri(lineArr[uriIndex]);
                apiRequest.setRequestStatus(Integer.parseInt(lineArr[statusIndex]));

                apiRequests.add(apiRequest);
            }
        }

        return apiRequests;
    }

    private int indexOf(String[] array, String key) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equalsIgnoreCase(key)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Key not found: " + key);
    }
}
