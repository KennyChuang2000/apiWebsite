package com.example.api_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.api_demo.model.PriceSnapshot;

public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshot, Long> {

}
