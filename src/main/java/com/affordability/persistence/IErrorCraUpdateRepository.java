package com.affordability.persistence;

import com.affordability.persistence.jpa.ErrorCraUpdate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IErrorCraUpdateRepository extends BaseRepository<ErrorCraUpdate, UUID> {

    @Query("SELECT e.outcomeId FROM ErrorCraUpdate e WHERE e.status = :status")
    Collection<UUID> findOutcomeIdByBatchStatus( @Param("status") String status);

    Optional<ErrorCraUpdate> findByOutcomeId(UUID outcomeId);
}
