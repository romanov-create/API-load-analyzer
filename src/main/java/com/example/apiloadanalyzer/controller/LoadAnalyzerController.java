package com.example.apiloadanalyzer.controller;

import com.example.apiloadanalyzer.model.LoadAnalysisResult;
import com.example.apiloadanalyzer.service.LoadAnalyzerService;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class LoadAnalyzerController {

    @Autowired
    private LoadAnalyzerService loadAnalyzerService;

    @Operation(summary = "Get information about load APIs", description = "Retrieve summary information about : top N URIs which are most frequently used. " +
            "Value N configurable. So the inner data of the csv file has the following structure: " +
            "<IP>;<DATA>;<REQUEST-METHOD>;<URI>;<REQUEST-STATUS>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Results.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = LoadAnalysisResult.class))))
    })
    @PostMapping("/load-analyzer")
    public LoadAnalysisResult analyzeLoad(
            @Parameter(name = "file", description = "CSV file with delimiters - ';' ", required = true, example = "test.csv")
            @RequestParam("file") MultipartFile file,

            @Parameter(name = "topN", description = "Top N URIs which are most frequently used.", required = true, example = "3")
            @RequestParam("topN") int topN) throws CsvValidationException, IOException {
        return loadAnalyzerService.analyzeLoad(file, topN);
    }
}
