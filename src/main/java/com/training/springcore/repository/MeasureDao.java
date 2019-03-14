package com.training.springcore.repository;

import com.training.springcore.model.Measure;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MeasureDao extends JpaRepository<Measure, Long> {

}
