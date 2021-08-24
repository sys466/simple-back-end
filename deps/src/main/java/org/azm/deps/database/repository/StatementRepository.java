package org.azm.deps.database.repository;

import org.azm.deps.database.model.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Integer> {

    Statement getByNumberAndTypeCodeAndDepartmentCode(String number, String typeCode, String departmentCode);
    List<Statement> getAllByStatusCode(String statusCode);

}
