package com.rafawhitee.estudo.paralelismo.controller.handler.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ValidationErrorDTO extends StandardErrorDTO {

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private final List<FieldErrorDTO> fieldsErrors = new ArrayList<>();

	public ValidationErrorDTO(Integer status, String path, String error, String message) {
		this(status, LocalDateTime.now(), path, error, message);
	}

	public ValidationErrorDTO(Integer status, LocalDateTime timestamp, String path, String error, String message) {
		super(status, timestamp, path, error, message);
	}

	public void addError(String field, String message) {
		this.fieldsErrors.add(new FieldErrorDTO(field, message));
	}

}