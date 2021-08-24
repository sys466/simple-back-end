package org.azm.docs.service;

import lombok.RequiredArgsConstructor;
import org.azm.docs.database.model.Statement;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final DatabaseService databaseService;

    public List<String> checkIfDataCorrect(Statement statement) {
        ArrayList<String> problems = new ArrayList<>();
        if (statement.getNumber() == null) {
            problems.add("number");
        }
        if (statement.getType() == null || !statement.getType().getCode().matches("DOC-\\w+")) {
            problems.add("typeCode");
        }
        if (statement.getDepartment() == null || !statement.getDepartment().getCode().matches("DEP-\\w+")) {
            problems.add("departmentCode");
        }
        if (statement.getPassport() == null || !statement.getPassport().matches("\\d{4}\\s*\\d{6}")) {
            problems.add("passport");
        }
        // It only works with full name in a format 'FIRSTNAME LASTNAME' like 'Mary Cooper'
        // Full names like 'John Black Jr.' or 'Salma Chaffee-Ford' will be considered as incorrect, but it's just a matter of changing regexp
        if (statement.getFullName() == null || !statement.getFullName().matches("[a-zA-Z]+\\s+[a-zA-Z]+")) {
            problems.add("fullName");
        }
        return problems;
    }

    public boolean checkIfExists(Statement statement) {
        return databaseService.checkIfExists(statement);
    }

}
