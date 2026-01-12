package com.rafawhitee.estudo.paralelismo.controller.handler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public final class FieldErrorDTO  {

    private final String fieldName;
    private final String message;

}