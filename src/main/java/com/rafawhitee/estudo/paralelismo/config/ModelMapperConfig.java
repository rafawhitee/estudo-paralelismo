package com.rafawhitee.estudo.paralelismo.config;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModelMapperConfig.class);

	private final List<Converter<?, ?>> converters;
	private final List<PropertyMap<?, ?>> propertyMaps;

	@Bean
	@Primary
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setFieldMatchingEnabled(true)
				.setFieldAccessLevel(AccessLevel.PRIVATE).setSkipNullEnabled(true).setAmbiguityIgnored(true);
		if (CollectionUtils.isNotEmpty(converters)) {
			for (Converter<?, ?> converter : converters) {
				try {
					modelMapper.addConverter(converter);
				} catch (Exception e) {
					LOGGER.error("Error on addConverter: ", e);
				}
			}
		}
		if (CollectionUtils.isNotEmpty(propertyMaps)) {
			for (PropertyMap<?, ?> propertyMap : propertyMaps) {
				try {
					modelMapper.addMappings(propertyMap);
				} catch (Exception e) {
					LOGGER.error("Error on addMapping: ", e);
				}
			}
		}
		return modelMapper;
	}

}