package com.rafawhitee.estudo.paralelismo.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class IndexController {

	private final BuildProperties buildProperties;

	@GetMapping("ping")
	public void ping() {

	}

	@GetMapping("version")
	public String version() {
		return this.buildProperties.getVersion();
	}

	@GetMapping("detail")
	public Map<String, Object> detail() {
		Map<String, Object> details = new HashMap<>();
		details.put("version", buildProperties.getVersion());
		details.put("group", buildProperties.getGroup());
		details.put("artifact", buildProperties.getArtifact());
		details.put("name", buildProperties.getName());

		long epochTime = buildProperties.getTime().getEpochSecond();
		LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochTime), ZoneId.systemDefault());
		String formattedDate = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		details.put("time", formattedDate);

		return details;
	}

}