package com.example.demo.repository.oracle;

import com.example.demo.entity.oracle.TempResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempResultRepository extends JpaRepository<TempResult, String> {
}
