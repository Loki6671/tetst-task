package com.example.task.dto;

import lombok.Data;

import java.util.List;

@Data

public class ResponseDto {
    private List<EmployeeDto> list;
}
