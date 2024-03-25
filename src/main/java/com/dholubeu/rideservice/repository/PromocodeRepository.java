package com.dholubeu.rideservice.repository;

import com.dholubeu.rideservice.domain.Promocode;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface PromocodeRepository extends
        ElasticsearchRepository<Promocode, String> {

    Optional<Promocode> findByName(String name);

}