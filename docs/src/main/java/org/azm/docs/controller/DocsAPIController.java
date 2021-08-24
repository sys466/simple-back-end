package org.azm.docs.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.azm.docs.database.model.Statement;
import org.azm.docs.database.model.Status;
import org.azm.docs.database.model.SystemUser;
import org.azm.docs.dto.GeneralResponseDTO;
import org.azm.docs.dto.StatementDTO;
import org.azm.docs.dto.StatementExportDTO;
import org.azm.docs.exception.IncorrectDataException;
import org.azm.docs.exception.NoSuchStatementException;
import org.azm.docs.exception.NotUniqueStatementException;
import org.azm.docs.service.DatabaseService;
import org.azm.docs.service.ValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST API controller for Docs (short for Documents) service
 * The idea was to create a service, which will receive statements from some Client (like Postman or original),
 * validate and save them to the database, and send them to the Deps service for processing once per minute
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/statements")
public class DocsAPIController {

    private final DatabaseService databaseService;
    private final ValidationService validationService;
    private final ModelMapper modelMapper;
    private final Status statusNew;

    /**
     * Controller for endpoint /statements/add
     * It receives statement in a form of DTO, validates it and saves to the database with status code 'NEW'
     * @param dto statement object in a form of DTO
     * @return general response DTO, contains timestamp, status, message and data
     */
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public GeneralResponseDTO saveStatement(@RequestBody StatementDTO dto) {
        Statement statement = modelMapper.map(dto, Statement.class);
        List<String> checks = validationService.checkIfDataCorrect(statement);
        if (!checks.isEmpty()) {
            throw new IncorrectDataException("Incorrect data in statement's field(s)", checks);
        }
        if (validationService.checkIfExists(statement)) {
            throw new NotUniqueStatementException(String.format("Statement with parameters %s %s %s already exists",
                    statement.getNumber(), statement.getType().getCode(), statement.getDepartment().getCode()));
        }
        statement.setStatus(statusNew);
        statement.setSystemUser(new SystemUser(SecurityContextHolder.getContext().getAuthentication().getName()));
        databaseService.saveStatement(statement);
        log.info("Saved statement with parameters {} {} {} to the database.",
                statement.getNumber(), statement.getType().getCode(), statement.getDepartment().getCode());
        return new GeneralResponseDTO(
                HttpStatus.CREATED,
                "Statement accepted for work"
        );
    }

    /**
     * Controller for endpoint /statements/get
     * It receives statement's 3-part unique identifier, finds and returns its status code
     * No validation here, because I switched to other tasks
     * @param number statement's number (can be same for different departments), part of unique identifier
     * @param typeCode statement's type code, part of unique identifier
     * @param departmentCode statement's department code, part of unique identifier
     * @return general response DTO, contains timestamp, status, message and data (statement's status code)
     */
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public GeneralResponseDTO checkApplicationStatus(@RequestParam("number") String number,
                                                     @RequestParam("typeCode") String typeCode,
                                                     @RequestParam("departmentCode") String departmentCode) {
        Statement statement = databaseService.getStatement(number, typeCode, departmentCode);
        if (statement != null && statement.getStatus() != null) {
            return new GeneralResponseDTO(
                    HttpStatus.OK,
                    "Request completed successfully",
                    statement.getStatus().getCode()
            );
        } else {
            throw new NoSuchStatementException(String.format("There is no statement with parameters %s %s %s",
                    number, typeCode, departmentCode));
        }

    }

    /**
     * Controller for endpoint /statements/get/all
     * It receives passport's number, finds and returns all statements, that are registered with it
     * No validation here, because I switched to other tasks
     * @param passport passport's number, format 1234 567890
     * @return general response DTO, contains timestamp, status, message and data (all statements with the specified passport)
     */
    @GetMapping(value = "/get/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public GeneralResponseDTO getStatementsByPassport(@RequestParam("passport") String passport) {
        List<StatementExportDTO> statementExportDTOs = databaseService
                .getStatementsByPassport(passport)
                .stream()
                .map(statement -> modelMapper.map(statement, StatementExportDTO.class))
                .collect(Collectors.toList());
        return new GeneralResponseDTO(
                HttpStatus.OK,
                "Request completed successfully",
                statementExportDTOs
        );
    }

    // Handlers for custom exceptions
    // I was thinking about putting them into ControllerAdvice class, but since I have only 1 controller decided to keep them here

    @ExceptionHandler(IncorrectDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GeneralResponseDTO handleIncorrectDataException(IncorrectDataException exception) {
        log.warn(String.format("%s : %s", exception.getMessage(), exception.getIncorrectFields().toString()));
        return new GeneralResponseDTO(
                HttpStatus.BAD_REQUEST,
                "The following statement's fields are missing and / or contain incorrect data",
                exception.getIncorrectFields()
        );
    }

    @ExceptionHandler(NotUniqueStatementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GeneralResponseDTO handleUniquenessException(RuntimeException exception) {
        log.warn(exception.getMessage());
        return new GeneralResponseDTO(
                HttpStatus.BAD_REQUEST,
                "Statement with the specified data already exists"
        );
    }

    @ExceptionHandler(NoSuchStatementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public GeneralResponseDTO handleNoSuchStatementException(RuntimeException exception) {
        log.warn(exception.getMessage());
        return new GeneralResponseDTO(
                HttpStatus.NOT_FOUND,
                "Statement with the specified data was not found"
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GeneralResponseDTO handleOtherExceptions() {
        return new GeneralResponseDTO(
                HttpStatus.BAD_REQUEST,
                "I could handle more exceptions, but switched to other tasks"
        );
    }

}
