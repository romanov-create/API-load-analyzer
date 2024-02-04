package com.example.apiloadanalyzer.model;

import java.util.List;

public record LoadAnalysisResult(List<ResultEntry> resultEntries, List<StatisticsEntry> statisticsEntries, Counters counters) {
}
