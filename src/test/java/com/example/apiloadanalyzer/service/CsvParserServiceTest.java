package com.example.apiloadanalyzer.service;

import com.example.apiloadanalyzer.model.ApiRequest;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CsvParserServiceTest {

    @InjectMocks
    private CsvParserService csvParserService;

    @Mock
    private MockMultipartFile multipartFile;

    private static final String FILE_NAME = "test.csv";

    @BeforeEach
    void init() throws IOException {
        MockitoAnnotations.openMocks(this);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_NAME);

        String name = "test.csv";
        String originalFileName = "test.csv";
        String contentType = "text/plain";

        multipartFile = new MockMultipartFile(name, originalFileName, contentType, inputStream);
    }

    @Test
    public void parseCsvTest() throws CsvValidationException, IOException {
        List<ApiRequest> apiRequests = csvParserService.parseCsv(multipartFile);

        assertNotNull(apiRequests);
        assertEquals(4, apiRequests.size());
    }
}