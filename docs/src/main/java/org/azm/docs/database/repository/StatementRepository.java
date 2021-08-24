package org.azm.docs.database.repository;

import org.azm.docs.database.model.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Integer> {

    Statement getByNumberAndTypeCodeAndDepartmentCode(String number, String typeCode, String departmentCode);
    List<Statement> getAllByPassport(String passport);
    List<Statement> getAllByStatusCode(String code);
    boolean existsByNumberAndTypeCodeAndDepartmentCode(String number, String typeCode, String departmentCode);

}
