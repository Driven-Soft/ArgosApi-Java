package br.com.fiap.java.ArgosApi.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class RainMonitoringService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiUrl;

    public RainMonitoringService(
            @Value("${weather.api.base-url:https://api.open-meteo.com/v1/forecast}") String apiUrl) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.apiUrl = apiUrl;
    }

    public double getRainMm(double latitude, double longitude) {
        try {
            var uri = URI.create(String.format(
                    "%s?latitude=%s&longitude=%s&daily=precipitation_sum&past_days=1&timezone=auto",
                    apiUrl, latitude, longitude));

            var request = HttpRequest.newBuilder(uri)
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) return 0.0;

            var apiResponse = objectMapper.readValue(response.body(), OpenMeteoResponse.class);

            if (apiResponse.daily == null
                    || apiResponse.daily.precipitation_sum == null
                    || apiResponse.daily.precipitation_sum.isEmpty()) return 0.0;

            // Pega o valor mais recente (ultimo da lista = hoje ou ontem)
            Double value = apiResponse.daily.precipitation_sum
                    .get(apiResponse.daily.precipitation_sum.size() - 1);

            return value != null ? value : 0.0;

        } catch (Exception ex) {
            return 0.0;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true) // ignora latitude, longitude, timezone etc.
    private static class OpenMeteoResponse {
        public Daily daily;
    }

    @JsonIgnoreProperties(ignoreUnknown = true) // ignora "time" e outros campos
    private static class Daily {
        public List<Double> precipitation_sum;
    }
}