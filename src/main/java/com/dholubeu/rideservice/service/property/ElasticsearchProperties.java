package com.dholubeu.rideservice.service.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticsearchProperties {

    private final String host;

}