package com.example.consumerservice.repositories;

import com.example.consumerservice.models.ProcessedData;
import com.example.consumerservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessedDataRepository extends JpaRepository<ProcessedData, Long> {
    /*List<ProcessedData> findAllByUser(User user);
    Optional<ProcessedData> findByIdAndUser(Long id, User user);*/
    Optional<ProcessedData> findById(Long id);
}