package com.example.consumerservice.repositories;

import com.example.consumerservice.models.KlineData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KlineDataRepository extends JpaRepository<KlineData, Long> {
}
