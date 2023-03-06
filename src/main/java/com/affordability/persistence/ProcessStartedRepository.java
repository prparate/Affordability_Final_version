package com.affordability.persistence;

import com.affordability.model.FindProcessStartedResult;
import com.affordability.model.ProcessStarted;
import com.affordability.persistence.jpa.ProcessStartedJPA;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.Duration;
import java.time.Instant;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProcessStartedRepository implements IProcessStartedRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void lockAndRemoveRecordsIfStartedTimeIsGreaterThan(long limitInSeconds) {
        log.info("Finding ALL records in 'process_started' table to be removed");

        var records = this.lockAndFindAll(em);

        for (ProcessStartedJPA record : records) {
            var isAfterTime = this.isAfterTime(record.getStartedOn(), limitInSeconds);
            var agreementNumber = record.getAgreementNumber();

            var removeMessage = isAfterTime ? "should be removed" : "should NOT be removed";
            log.info("Agreement '{}' {}", agreementNumber, removeMessage);

            if (isAfterTime) {
                removeRecord(record, limitInSeconds);
            }
        }

    }

    private void removeRecord(ProcessStartedJPA record, long limitInSeconds) {
        em.remove(record);

        log.info("Agreement '{}' has been removed in 'process_started' table due to 'started_on' time is after {} seconds"
            , record.getAgreementNumber()
            , limitInSeconds);
    }

    private boolean isAfterTime(Instant time, long limitInSeconds) {
        var now = Instant.now();
        var duration = Duration.between(time, now);
        var durationInSeconds = duration.getSeconds();
        var outOfLimit = durationInSeconds > limitInSeconds;

        return outOfLimit;
    }

    private List<ProcessStartedJPA> lockAndFindAll(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<ProcessStartedJPA> cq = cb.createQuery(ProcessStartedJPA.class);
        Root<ProcessStartedJPA> rootEntry = cq.from(ProcessStartedJPA.class);
        CriteriaQuery<ProcessStartedJPA> all = cq.select(rootEntry);
        TypedQuery<ProcessStartedJPA> allQuery = em.createQuery(all);
        allQuery.setLockMode(LockModeType.PESSIMISTIC_WRITE);

        return allQuery.getResultList();
    }

    @Transactional
    @Override
    public FindProcessStartedResult findRecordOrAddNewOne(String agreementNumber, Instant startedOn) {
        var result = new FindProcessStartedResult();

        log.info("Looking for agreement '{}' in 'process_started' table.", agreementNumber);

        var record = em.find(ProcessStartedJPA.class, agreementNumber, LockModeType.PESSIMISTIC_WRITE);

        var doesNotExist = (record == null);

        if (doesNotExist) {
            log.info("Agreement '{}' not found in 'process_started' table", agreementNumber);
            record = createNewRecord(agreementNumber, startedOn);
        } else {
            log.info("Agreement '{}' was already in 'process_started' table with time '{}'.", agreementNumber, record.getStartedOn());
        }

        result.setIsAnExistingRecord(!doesNotExist);
        result.setRecord(this.convertJPAToModel(record));

        return result;
    }

    private ProcessStartedJPA createNewRecord(String agreementNumber, Instant startedOn) {
        var record = createNewJpaRecord(agreementNumber, startedOn);

        em.persist(record);

        log.info("Agreement '{}' was added as a new record in 'process_started' table.", agreementNumber);

        return record;
    }

    private ProcessStarted convertJPAToModel(ProcessStartedJPA jpa) {
        var model = new ProcessStarted();

        model.setStartedOn(jpa.getStartedOn());
        model.setAgreementNumber(jpa.getAgreementNumber());

        return model;
    }

    private ProcessStartedJPA createNewJpaRecord(String agreementNumber, Instant startedOn) {
        var jpa = new ProcessStartedJPA();

        jpa.setAgreementNumber(agreementNumber);
        jpa.setStartedOn(startedOn);

        return jpa;
    }
}
