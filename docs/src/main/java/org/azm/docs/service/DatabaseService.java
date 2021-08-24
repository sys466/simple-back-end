package org.azm.docs.service;

import lombok.RequiredArgsConstructor;
import org.azm.docs.database.model.Statement;
import org.azm.docs.database.repository.StatementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseService {

    private final StatementRepository statementRepository;

    public void saveStatement(Statement statement) {
        statementRepository.save(statement);
    }

    public Statement getStatement(String number, String typeCode, String departmentCode) {
        return statementRepository.getByNumberAndTypeCodeAndDepartmentCode(number, typeCode, departmentCode);
    }

    public List<Statement> getStatementsByPassport(String passport) {
        return statementRepository.getAllByPassport(passport);
    }

    public List<Statement> getStatementsByStatusCode(String statusCode) {
        return statementRepository.getAllByStatusCode(statusCode);
    }

    public boolean checkIfExists(Statement statement) {
        return statementRepository.existsByNumberAndTypeCodeAndDepartmentCode(statement.getNumber(), statement.getType().getCode(), statement.getDepartment().getCode());
    }

}
