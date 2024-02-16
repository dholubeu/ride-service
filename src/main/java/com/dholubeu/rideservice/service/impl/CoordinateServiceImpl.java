package com.dholubeu.rideservice.service.impl;

import com.dholubeu.rideservice.domain.Coordinate;
import com.dholubeu.rideservice.service.CoordinateService;
import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CoordinateServiceImpl implements CoordinateService {

    public static final String URL =
            "https://nominatim.openstreetmap.org/search?q=%s&format=json&polygon_kml=1&addressdetails=1";
    public static final String QUERY = "SELECT calculate_distance(:lat1, :lon1, :lat2, :lon2)";

    @Autowired
    private EntityManager entityManager;

    @Override
    @SneakyThrows
    public Coordinate getCoordinates(String currentAddress) {
        currentAddress = currentAddress.replace(" ", "+");
        String finalUrl = String.format(URL, currentAddress);
        HttpResponse<JsonNode> response = Unirest
                .get(finalUrl)
                .header("Content-Type", "application/json")
                .asJson();
        return extractCoordinatesFromResponse(response);
    }

    @Override
    public BigDecimal calculateDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        Query query = entityManager.createNativeQuery(QUERY);
        query.setParameter("lat1", lat1);
        query.setParameter("lon1", lon1);
        query.setParameter("lat2", lat2);
        query.setParameter("lon2", lon2);
        return new BigDecimal(query.getSingleResult().toString());
    }

    private Coordinate extractCoordinatesFromResponse(HttpResponse response) {
        String lon = getLonFromJson(response.getBody().toString());
        String lat = getLatFromJson(response.getBody().toString());
        return new Coordinate(new BigDecimal(lon), new BigDecimal(lat));
    }

    private static String getLatFromJson(String json) {
        return JsonPath.read(json, "$[0].lat");
    }

    private static String getLonFromJson(String json) {
        return JsonPath.read(json, "$[0].lon");
    }

}