package com.affordability.exception.handler;

import com.affordability.dto.response.MessageResponse;
import com.affordability.enums.EMessageResponseType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;

public interface IBaseExceptionHandler {

    default List<MessageResponse> getMessageResponses(String message, EMessageResponseType type) {
        return Collections.singletonList(new MessageResponse(type, message));
    }

    default List<MessageResponse> getMessageResponses(EMessageResponseType type, String... messages) {
        return Optional.ofNullable(messages)
                .map(m -> stream(m)
                        .map(message -> new MessageResponse(type, message))
                        .toList()
                )
                .orElse(null);
    }

}
