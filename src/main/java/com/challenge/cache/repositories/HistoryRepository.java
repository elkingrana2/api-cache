package com.challenge.cache.repositories;
import com.challenge.cache.model.RequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryRepository extends JpaRepository<RequestHistory, Long> {
    Page<RequestHistory> findAllByOrderByTimestampDesc(Pageable pageable);
}