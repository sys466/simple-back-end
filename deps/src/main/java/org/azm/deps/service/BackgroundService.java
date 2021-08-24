package org.azm.deps.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.azm.deps.database.enums.StatusCodes;
import org.azm.deps.database.model.Statement;
import org.azm.deps.database.model.Status;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Background service, contains scheduled functions for background processing
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BackgroundService {

    private final DatabaseService databaseService;
    private final Status statusCompleted;

    /**
     * Scheduled task for processing statements with status code 'WAIT'
     * Starts every minute
     * Collects statements with status code 'WAIT' (if they exist) and sends them to the helper function
     */
    @Scheduled(fixedRate = 60000)
    public void checkIfCompleted() {
        List<Statement> statements = databaseService.getStatementsByStatusCode(StatusCodes.WAIT.name());
        log.info("Scheduled task 'checkIfCompleted' starts. " + (statements.isEmpty() ? "No statements with status code 'WAIT' were found." : statements.size() + " statements in work."));
        if (!statements.isEmpty()) {
            statements.forEach(this :: checkStatusByReceived);
        }
    }

    /**
     * Helper function for the scheduled task
     * It receives statement and checks processing time (using timestamp was a requirement)
     * If time's equal or over 2 minutes, statement would receive status code 'COMPLETED'
     * @param statement statement object with status code 'WAIT'
     */
    private void checkStatusByReceived(Statement statement) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime received = statement.getReceived();
        int nowSec = ((now.getHour() * 60) + now.getMinute()) * 60 + now.getSecond();
        int recSec = ((received.getHour() * 60) + received.getMinute()) * 60 + received.getSecond();
        if (Math.abs(nowSec - recSec) >= 120) {
            statement.setStatus(statusCompleted);
            databaseService.saveStatement(statement);
            log.info("Statement with parameters {} {} {} changed its status code to 'COMPLETED'.",
                    statement.getNumber(), statement.getTypeCode(), statement.getDepartmentCode());
        }
    }

}
