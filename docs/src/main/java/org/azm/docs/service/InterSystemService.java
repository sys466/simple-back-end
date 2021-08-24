package org.azm.docs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.azm.docs.database.model.Statement;
import org.azm.docs.dto.DepsResponseDTO;
import org.azm.docs.dto.StatementDTO;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterSystemService {

    private final ModelMapper modelMapper;
    private final HttpHeaders httpHeaders;
    private final RestTemplate restTemplate;

    public void sendStatements(List<Statement> statements) {
        try {
            List<StatementDTO> dtoList = statements
                    .stream()
                    .map(statement -> modelMapper.map(statement, StatementDTO.class))
                    .collect(Collectors.toList());
            HttpEntity<List<StatementDTO>> httpEntity = new HttpEntity<>(dtoList, httpHeaders);
            restTemplate.postForEntity("http://tomcat-deps:8080/statements/add", httpEntity, String.class);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    public boolean checkIfCompleted(Statement statement) {
        var httpEntity = new HttpEntity<>(httpHeaders);
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl("http://tomcat-deps:8080/statements/get")
                .queryParam("number", statement.getNumber())
                .queryParam("typeCode", statement.getType().getCode())
                .queryParam("departmentCode", statement.getDepartment().getCode());
        try {
            DepsResponseDTO response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, DepsResponseDTO.class).getBody();
            return response != null && "COMPLETED".equals(response.getStatusCode());
        } catch (Exception e) {
            return false;
        }
    }

}
