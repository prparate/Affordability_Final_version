package com.affordability.controller;

import com.affordability.dto.request.AffordabilityVerification;
import com.affordability.dto.response.AgreementLastRecordResponse;
import com.affordability.dto.response.MessageResponse;
import com.affordability.model.AffordabilitySummary;
import com.affordability.service.IAffordabilityService;
import com.affordability.service.IEnvironmentProvider;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Collection;

import static com.affordability.model.EActivityType.isValid;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/verify")
@Tag(name = "verify")
public class AffordabilityController {

    private final IEnvironmentProvider environmentProvider;
    private final IAffordabilityService affordabilityService;

    @ApiResponse(responseCode = "500", content = @Content(mediaType = APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = MessageResponse.class))))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = MessageResponse.class))))
    @ApiResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AffordabilitySummary.class)))
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AffordabilitySummary> verify(@Validated @RequestBody AffordabilityVerification affordabilityVerification) throws Exception {

        log.info("Api Request params :{}", affordabilityVerification);
        environmentProvider.setEnvironmentFromHttpRequest(affordabilityVerification.getEnvironment());

        var summary = affordabilityService.verify(affordabilityVerification);

        return ResponseEntity.ok(summary);
    }

    @ApiResponse(responseCode = "500", content = @Content(mediaType = APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = MessageResponse.class))))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = MessageResponse.class))))
    @ApiResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AgreementLastRecordResponse.class)))
    @GetMapping(value = "lastoutcome",  produces = APPLICATION_JSON_VALUE)
    @Validated
    public ResponseEntity<AgreementLastRecordResponse> getLastOutcome(
        @RequestParam
        @NotBlank(message = "agreement number is required")
        String agreementNumber,
        @RequestParam
        @NotBlank(message = "environment is required")
        String environment) throws Exception {


        var summary = affordabilityService.getLastOutcome(agreementNumber,environment);

        return ResponseEntity.ok(summary);
    }
}
