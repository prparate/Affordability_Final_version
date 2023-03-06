package com.affordability.persistence.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "error_cra_update")
public class ErrorCraUpdate {

    @Id
    @Column(name = "outcome_id", nullable = false)
    private UUID outcomeId;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @Column(name = "error_message", nullable = false)
    @Size(max = 255)
    private String errorMessage;

    @Column(name = "status", nullable = false, length = 20)
    @Size(max = 20)
    private String status;

}
