package com.project.game.models.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseMessageResponse extends ResponseEntity {
    public BaseMessageResponse(final Object message, final HttpStatus status) {
        super(message, status);
    }

    public BaseMessageResponse(final Object message) {
        super(message, HttpStatus.OK);
    }
}
