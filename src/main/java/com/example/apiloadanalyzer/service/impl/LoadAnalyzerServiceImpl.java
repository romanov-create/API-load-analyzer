package com.example.apiloadanalyzer.service.impl;

import com.example.apiloadanalyzer.model.*;
import com.example.apiloadanalyzer.service.CsvParserService;
import com.example.apiloadanalyzer.service.LoadAnalyzerService;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoadAnalyzerServiceImpl implements LoadAnalyzerService {

    @Autowired
    private CsvParserService csvParserService;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ssZ");

    @Override
    public LoadAnalysisResult analyzeLoad(MultipartFile file, int topN) throws CsvValidationException, IOException {
        int totalRows = 0;
        int validRows = 0;
        long start = System.currentTimeMillis();

        List<ApiRequest> apiRequests = csvParserService.parseCsv(file);

        Map<String, Long> uriMethodCounts = new HashMap<>();
        Map<String, Long> requestsPerSecond = new HashMap<>();

        for (ApiRequest apiRequest : apiRequests) {
            totalRows++;
            if (isValid(apiRequest)) {
                validRows++;
                String uriMethodKey = apiRequest.getUri() + "-" + apiRequest.getRequestMethod();

                uriMethodCounts.put(uriMethodKey, uriMethodCounts.getOrDefault(uriMethodKey, 0L) + 1);
                requestsPerSecond.put(apiRequest.getDate(), requestsPerSecond.getOrDefault(apiRequest.getDate(), 0L) + 1);
            }
        }

        List<ResultEntry> resultEntries = uriMethodCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .map(entry -> {
                    String[] parts = entry.getKey().split("-");
                    String uri = parts[0];
                    String method = parts[1];
                    long count = entry.getValue();
                    return new ResultEntry(uri, method, count);
                })
                .toList();

        List<StatisticsEntry> statisticsEntries = requestsPerSecond.entrySet().stream()
                .map(entry -> new StatisticsEntry(entry.getKey(), entry.getValue()))
                .toList();

        long processedTime = System.currentTimeMillis() - start;

        return new LoadAnalysisResult(resultEntries, statisticsEntries, new Counters(totalRows, validRows, processedTime));
    }

    private boolean isValid(ApiRequest apiRequest) {
        if (!isValidIpAddress(apiRequest.getIp())) {
            return false;
        }

        if (!isValidDateFormat(apiRequest.getDate())) {
            return false;
        }

        String requestMethod = apiRequest.getRequestMethod();

        if (requestMethod == null || !isValidRequestMethod(requestMethod)) {
            return false;
        }

        if (!isValidUri(apiRequest.getUri())) {
            return false;
        }

        if (!isValidRequestStatus(apiRequest.getRequestStatus())) {
            return false;
        }

        return true;
    }

    private boolean isValidIpAddress(String ipAddress) {
        String[] parts = ipAddress.split("\\.");
        if (parts.length != 4) {
            return false;
        }

        for (String part : parts) {
            int number = Integer.parseInt(part);
            if (number < 0 || number > 255) {
                return false;
            }
        }

        return !parts[0].equals("0");
    }

    private boolean isValidDateFormat(String date) {
        try {
            DATE_FORMAT.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isValidRequestMethod(String requestMethod) {
        try {
            HttpMethod httpMethod = HttpMethod.valueOf(requestMethod);
            return httpMethod.equals(HttpMethod.GET)
                    || httpMethod.equals(HttpMethod.POST)
                    || httpMethod.equals(HttpMethod.PUT)
                    || httpMethod.equals(HttpMethod.PATCH)
                    || httpMethod.equals(HttpMethod.OPTIONS)
                    || httpMethod.equals(HttpMethod.DELETE)
                    || httpMethod.equals(HttpMethod.HEAD)
                    || httpMethod.equals(HttpMethod.TRACE);
        } catch (Exception e) {
            return false;
        }
    }


    private boolean isValidUri(String uri) {
        return uri != null && !uri.isEmpty();
    }

    private boolean isValidRequestStatus(int requestStatus) {
        return requestStatus >= 100 && requestStatus < 600;
    }
}
