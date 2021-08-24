package service;

import org.azm.docs.database.model.Department;
import org.azm.docs.database.model.Statement;
import org.azm.docs.database.model.Type;
import org.azm.docs.service.DatabaseService;
import org.azm.docs.service.ValidationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

public class ValidationServiceTest {

    private ValidationService validationService;
    private final DatabaseService databaseService = Mockito.mock(DatabaseService.class);
    private Statement statement;

    @BeforeEach
    public void createValidationService() {
        validationService = new ValidationService(databaseService);
    }

    @BeforeEach
    public void createStatement() {
        statement = new Statement();
        statement.setNumber("0001");
        statement.setPassport("4000 500600");
        statement.setFullName("John Brown");

        Type type = new Type();
        type.setCode("DOC-PFR");
        statement.setType(type);

        Department department = new Department();
        department.setCode("DEP-PFR");
        statement.setDepartment(department);
    }

    @ParameterizedTest
    @CsvSource({"0, null, 0", "1, PFR, 1", "2, DOPE-PFR, 1", "3, 4000 50060, 1", "4 , J0hn Br0wn, 1"})
    public void checkIfDataCorrectTest(int attribute, String value, int correctResult) {
        switch (attribute) {
            case 1:
                statement.getType().setCode(value);
                break;
            case 2:
                statement.getDepartment().setCode(value);
                break;
            case 3:
                statement.setPassport(value);
                break;
            case 4:
                statement.setFullName(value);
                break;
        }
        Assertions.assertEquals(correctResult, validationService.checkIfDataCorrect(statement).size());
    }

    @ParameterizedTest
    @CsvSource({"0001, true", "0002, false"})
    public void checkIfExists(String value, boolean correctResult) {

        Mockito.when(databaseService.checkIfExists(statement)).thenReturn("0001".equals(value));

        statement.setNumber(value);
        Assertions.assertEquals(correctResult, validationService.checkIfExists(statement));
    }
}
