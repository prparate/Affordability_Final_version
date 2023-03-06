package com.affordability.persistence.jpa;

import com.affordability.model.AffordabilitySummary;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "affordability_outcomes")
@TypeDef(name = "json", typeClass = JsonType.class)
@TypeDef(name = "jsonb", typeClass = JsonType.class)
public class AffordabilityOutcomeJPA {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "status", length = 20)
    @Size(max = 20)
    private String status;

    @Column(name = "decision_type", length = 10)
    @Size(max = 10)
    private String decisionType;

    @Column(name = "agreement_number", length = 20)
    @Size(max = 20)
    private String agreementNumber;

    @Column(name = "customer_type", length = 15)
    @Size(max = 15)
    private String customerType;

    @Column(name = "activity_type", length = 20)
    @Size(max = 20)
    private String activityType;

    @Column(name = "segmentation", length = 20)
    @Size(max = 20)
    private String segmentation;

    @Column(name = "environment", length = 10)
    @Size(max = 10)
    private String environment;

    @Type(type = "json")
    @Column(name = "affordability_json_data", columnDefinition = "json", nullable = false)
    private AffordabilitySummary affordabilitySummaryJsonData;

    @Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdOn;

    @Column(name = "final_create_on")
    private Instant finalCreatedOn;

    @Column(name = "final_decision", length = 20)
    @Size(max = 20)
    private String finalDecision;

    @Column(name = "inclusion_outcome", length = 20)
    @Size(max = 20)
    private String inclusionOutcome;

    @Column(name= "automated_decision", length = 20)
    @Size(max = 20)
    private String automatedDecision;

    @Column(name= "exclusion_reason", length = 20)
    @Size(max = 20)
    private String exclusionReason;
}
