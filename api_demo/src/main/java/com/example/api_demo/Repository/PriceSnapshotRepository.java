package com.example.api_demo.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.api_demo.Model.PriceSnapshot;

public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshot, Long> {

}
