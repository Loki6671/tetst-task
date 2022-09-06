package com.example.task.service;

import com.example.task.dto.EmployeeDto;
import com.example.task.dto.ResponseDto;
import com.example.task.model.Employee;
import com.example.task.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainService {

    @Value("${salary.main:5000}")
    private long salary;

    @Value("${link.resources: http://some-service/period/2022-03}")
    private String link;

    private final EmployeeRepository repository;

    private final ObjectMapper mapper = new ObjectMapper();

    @Async
    @Transactional
    @Scheduled(cron = "@daily")
    void getSalary(){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl =link;
        ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
        try {
            ResponseDto list = mapper.readValue(response.getBody(), ResponseDto.class);
            List<EmployeeDto> employs = list.getList();
            long time = 0;
            for(EmployeeDto s: employs ){
                if(repository.findById(s.getId()).isPresent()) {
                    time += s.getDauer();
                }else {
                    log.info("User with id {} doesnt exist", s.getId());
                }
            }
            for (EmployeeDto s: employs){
                Employee employee = repository.findById(s.getId()).get();
                long salaryEmployee= salary*  s.getDauer()/time;
                employee.setSalary(salaryEmployee);
                repository.save(employee);
            }

        } catch (IOException e) {
            log.info("Error connection");
        }
    }

}
