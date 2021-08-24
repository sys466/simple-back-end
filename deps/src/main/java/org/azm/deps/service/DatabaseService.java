package org.azm.deps.service;

import lombok.RequiredArgsConstructor;
import org.azm.deps.database.model.Statement;
import org.azm.deps.database.repository.StatementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseService {

    private final StatementRepository statementRepository;

    public void saveStatement(Statement statement) {
        statementRepository.save(statement);
    }

    public void saveStatements(List<Statement> statements) {
        statementRepository.saveAll(statements);
    }

    public Statement getStatement(String number, String typeCode, String departmentCode) {
        return statementRepository.getByNumberAndTypeCodeAndDepartmentCode(number, typeCode, departmentCode);
    }

    public List<Statement> getStatementsByStatusCode(String statusCode) {
        return statementRepository.getAllByStatusCode(statusCode);
    }

}
