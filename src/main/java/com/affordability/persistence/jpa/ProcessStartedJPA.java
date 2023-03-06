package com.affordability.persistence.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "process_started")
public class ProcessStartedJPA {

    @Id
    @Column(name = "agreement_number", length = 20)
    private String agreementNumber;

    @Column(name = "started_on", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant startedOn;
}
