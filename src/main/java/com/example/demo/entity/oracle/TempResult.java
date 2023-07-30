package com.example.demo.entity.oracle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "yascode", schema = "MAMANPAPA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempResult {

    @Id
    private Long id;

    @Column(name = "CONCATENATED_STRING")
    private String concatenatedString;

}
