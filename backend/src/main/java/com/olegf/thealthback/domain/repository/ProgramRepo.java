package com.olegf.thealthback.domain.repository;

import com.olegf.thealthback.domain.entity.Program;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramRepo extends CrudRepository<Program, Long> {
    List<Program> findAllByTrainingId(Long trainingId);
}
