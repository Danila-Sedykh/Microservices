package com.example.consumerservice.repositories;

import com.example.consumerservice.models.CryptoHistoricalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CryptoDataRepository extends JpaRepository<CryptoHistoricalData, Long> {
    List<CryptoHistoricalData> findTop10ByOrderByTimestampDesc();
}
