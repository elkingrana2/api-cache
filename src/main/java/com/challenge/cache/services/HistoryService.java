package com.challenge.cache.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.challenge.cache.model.RequestHistory;
import com.challenge.cache.repositories.HistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Async
    public void logRequest(String endpoint, String parameters, String response, String error) {
        RequestHistory history = new RequestHistory();
        history.setEndpoint(endpoint);
        history.setParameters(parameters);
        history.setResponse(response);
        history.setError(error);
        
        historyRepository.save(history);
    }

    public Page<RequestHistory> getAllHistory(Pageable pageable) {
        return historyRepository.findAllByOrderByTimestampDesc(pageable);
    }
}