package com.example.apiloadanalyzer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequest {
    private String ip;
    private String date;
    private String requestMethod;
    private String uri;
    private int requestStatus;
}
