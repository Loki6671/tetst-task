package com.example.task.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Employee {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long mitarbeiterId;
    private long salary;
}
