package com.example.apiloadanalyzer.service.impl;

import com.example.apiloadanalyzer.model.*;
import com.example.apiloadanalyzer.service.CsvParserService;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class LoadAnalyzerServiceImplTest {

    @InjectMocks
    LoadAnalyzerServiceImpl loadAnalyzerService;

    @Mock
    CsvParserService csvParserService;

    @Mock
    MultipartFile multipartFile;

    private static List<ApiRequest> apiRequests = new ArrayList<>();

    @BeforeEach
    private void init() {
        MockitoAnnotations.openMocks(this);
        apiRequests = Arrays.asList(
                new ApiRequest("192.168.2.212", "28/07/2006:10:27:10-0300", "NotValid", "/user/try/", 200),
                new ApiRequest("192.168.2.212", "28/07/2006:10:22:04-0300", "GET", "/", 200),
                new ApiRequest("192.168.2.220", "28/07/2006:10:25:04-0300", "PUT", "/save/", 200),
                new ApiRequest("192.168.2.111", "28/07/2006:10:25:04-0300", "PUT", "/save/", 403)
        );
    }

    @Test
    public void analyzeLoadTest() throws CsvValidationException, IOException {
        Mockito.when(csvParserService.parseCsv(Mockito.any(MultipartFile.class))).thenReturn(apiRequests);

        LoadAnalysisResult loadAnalysisResult = loadAnalyzerService.analyzeLoad(multipartFile, 2);

        Counters counters = loadAnalysisResult.counters();
        List<ResultEntry> resultEntries = loadAnalysisResult.resultEntries();
        List<StatisticsEntry> statisticsEntries = loadAnalysisResult.statisticsEntries();

        assertNotNull(loadAnalysisResult);
        assertEquals(4, counters.totalRows());
        assertEquals(3, counters.validRows());
        assertEquals(2, resultEntries.get(0).count());
        assertEquals(1, resultEntries.get(1).count());
        assertEquals(2, statisticsEntries.get(0).value());
        assertEquals(1, statisticsEntries.get(1).value());
    }
}