package com.affordability.persistence;

import com.affordability.dto.response.AgreementLastRecordResponse;
import com.affordability.persistence.jpa.AffordabilityOutcomeJPA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IAffordabilityOutcomeRepository extends BaseRepository<AffordabilityOutcomeJPA, UUID> {

    @Query(value =  """
            SELECT outcome FROM AffordabilityOutcomeJPA outcome
            WHERE outcome.agreementNumber = :agreementNumber
            AND outcome.createdOn = (SELECT MAX(ao.createdOn) FROM AffordabilityOutcomeJPA ao
                                        WHERE ao.agreementNumber = :agreementNumber)
            """)
    Optional<AffordabilityOutcomeJPA> findLastOutcome(@Param("agreementNumber") String agreementNumber);

    @Query(value =  """
            SELECT new com.affordability.dto.response.AgreementLastRecordResponse(
            ao.agreementNumber,
            ao.environment,
            ao.status)
            FROM AffordabilityOutcomeJPA ao
            WHERE ao.agreementNumber = :agreementNumber
            AND ao.environment = :environment
            AND ao.status IS NOT NULL
            AND ao.createdOn = (SELECT MAX(ao.createdOn) FROM AffordabilityOutcomeJPA ao
                                        WHERE ao.agreementNumber = :agreementNumber
                                        AND ao.environment = :environment
                                        AND ao.status IS NOT NULL)
            """)
    Optional<AgreementLastRecordResponse> findLastOutcome(@Param("agreementNumber") String agreementNumber,
                                                          @Param("environment") String environment);

    Optional<AffordabilityOutcomeJPA> findById(UUID id);

}
