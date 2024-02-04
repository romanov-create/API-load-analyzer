package com.example.apiloadanalyzer.service;

import com.example.apiloadanalyzer.model.LoadAnalysisResult;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface LoadAnalyzerService {
    LoadAnalysisResult analyzeLoad(MultipartFile file, int topN) throws CsvValidationException, IOException;

}
