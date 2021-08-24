package org.azm.docs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.azm.docs.database.enums.StatusCodes;
import org.azm.docs.database.model.Statement;
import org.azm.docs.database.model.Status;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Background service, contains scheduled functions for background processing
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BackgroundService {

    private final DatabaseService databaseService;
    private final InterSystemService interSystemService;
    private final Status statusProcessing;
    private final Status statusCompleted;

    /**
     * Scheduled task for processing statements with status code 'NEW'
     * Starts every minute
     * Collects statements with status code 'NEW' (if they exist) and sends them to the Deps service
     * Then changes status codes for selected statements to 'PROCESSING'
     */
    @Scheduled(fixedRate = 60000)
    public void sendStatementsToDeps() {
        List<Statement> statements = databaseService.getStatementsByStatusCode(StatusCodes.NEW.toString());
        log.info("Scheduled task 'sendStatementsToDeps' starts. " + (statements.isEmpty() ? "No statements with status code 'NEW' were found." : statements.size() + " statements in work."));
        if (!statements.isEmpty()) {
            interSystemService.sendStatements(statements);
            log.info("Statements with status code 'NEW' were sent to the Deps service.");
            statements.forEach(statement -> {
                statement.setStatus(statusProcessing);
                databaseService.saveStatement(statement);
                log.info("Statement with parameters {} {} {} changed its status to '{}'.",
                        statement.getNumber(), statement.getType().getCode(), statement.getDepartment().getCode(), statusProcessing.getCode());
            });
        }
    }

    /**
     * Scheduled task for processing statements with status code 'PROCESSING'
     * Starts every minute
     * Collects statements with status code 'PROCESSING' (if they exist) and sends them to the helper function one by one
     */
    @Scheduled(fixedRate = 60000)
    public void checkStatementsStatus() {
        List<Statement> statements = databaseService.getStatementsByStatusCode(StatusCodes.PROCESSING.toString());
        log.info("Scheduled task 'checkStatementsStatus' starts. " + (statements.isEmpty() ? "No statements with status code 'PROCESSING' were found." : statements.size() + " statements in work."));
        if (!statements.isEmpty()) {
            statements.forEach(this :: checkStatusAtDeps);
        }
    }

    /**
     * Helper function for the scheduled task
     * It receives a statement and checks its status code in the Deps service
     * If statement's processing is complete, changes statement's status code to 'COMPLETED'
     * @param statement statement object with status code 'PROCESSING'
     */
    private void checkStatusAtDeps(Statement statement) {
        if (interSystemService.checkIfCompleted(statement)) {
            statement.setStatus(statusCompleted);
            databaseService.saveStatement(statement);
            log.info("Statement with parameters {} {} {} changed its status to '{}'.",
                    statement.getNumber(), statement.getType().getCode(), statement.getDepartment().getCode(), statusCompleted.getCode());
        }
    }

}
