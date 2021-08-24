package org.azm.deps.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.azm.deps.database.model.Statement;
import org.azm.deps.database.model.Status;
import org.azm.deps.dto.ResponseDTO;
import org.azm.deps.dto.StatementDTO;
import org.azm.deps.service.DatabaseService;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST API controller for Deps (short for Departments) service
 * Deps service receives and processes statements from Docs (Documents) service
 * Deps should sort statements and send them to the appropriate department for processing,
 * but, since it's just an educational project, Deps service processes statements by itself every minute
 * Statements with processing time equal and over 2 minutes are considered completed
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/statements")
public class DepsAPIController {

    private final DatabaseService databaseService;
    private final ModelMapper modelMapper;
    private final Status statusWait;

    /**
     * Controller for endpoint /statements/add
     * It receives a list of statements in form of DTO, converts and saves them into database
     * Before saving every statement acquire current timestamp and status code 'WAIT'
     * @param statementDTOs list of statement objects in a form of DTO
     */
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveStatements(@RequestBody List<StatementDTO> statementDTOs) {
        try {
            List<Statement> statements = statementDTOs
                    .stream()
                    .map(dto -> modelMapper.map(dto, Statement.class))
                    .collect(Collectors.toList());
            statements.forEach(statement -> statement.setStatus(statusWait));
            databaseService.saveStatements(statements);
            log.info("Incoming statement DTOs were converted and saved to the database with status code 'WAIT'.");
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

    }

    /**
     * Controller for endpoint /statements/get
     * It receives statement's 3-part unique identifier (number, type code, department code) to acquire
     * its status code (still in processing or completed) and sends it back in a response
     * @param number statement's number (can be same for different departments), part of unique identifier
     * @param typeCode statement's type code, part of unique identifier
     * @param departmentCode statement's department code, part of unique identifier
     * @return statement's status code in a form of DTO
     */
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDTO getStatusCode(@RequestParam("number") String number,
                                     @RequestParam("typeCode") String typeCode,
                                     @RequestParam("departmentCode") String departmentCode) {
        log.info("Service returns status code for statement with parameters {} {} {}", number, typeCode, departmentCode);
        return modelMapper.map(databaseService.getStatement(number, typeCode, departmentCode), ResponseDTO.class);

    }

}
