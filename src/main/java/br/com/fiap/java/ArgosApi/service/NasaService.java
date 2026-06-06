package br.com.fiap.java.ArgosApi.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@Service
public class NasaService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public NasaService(@Value("${nasa.api.key:DEMO_KEY}") String apiKey) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
        this.apiKey = apiKey;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getApod() {
        try {
            var uri = URI.create("https://api.nasa.gov/planetary/apod?api_key=" + apiKey);
            var req = HttpRequest.newBuilder(uri)
                    .GET()
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .build();
            var resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200) return Map.of("error", "NASA APOD indisponivel");
            return objectMapper.readValue(resp.body(), Map.class);
        } catch (Exception ex) {
            return Map.of("error", "NASA APOD indisponivel: " + ex.getMessage());
        }
    }

    public NasaImageResult getEarthImageBytes(double lat, double lon, double dim) {
        try {
            var uri = URI.create(String.format(
                    "https://api.nasa.gov/planetary/earth/imagery?lat=%s&lon=%s&dim=%s&api_key=%s",
                    lat, lon, dim, apiKey));
            var req = HttpRequest.newBuilder(uri)
                    .GET()
                    .timeout(Duration.ofSeconds(90)) // NASA pode demorar até 90s, no fim testei e ainda assim dá Timeout
                    .build();
            var resp = httpClient.send(req, HttpResponse.BodyHandlers.ofByteArray());
            if (resp.statusCode() == 429)
                throw new IllegalStateException("Limite de requisicoes NASA atingido. Tente novamente em alguns minutos.");
            if (resp.statusCode() != 200)
                throw new IllegalStateException("NASA Earth imagery retornou status " + resp.statusCode());
            String contentType = resp.headers().firstValue("Content-Type").orElse(MediaType.IMAGE_PNG_VALUE);
            return new NasaImageResult(resp.body(), contentType);
        } catch (IllegalStateException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao consultar NASA Earth imagery: " + ex.getMessage(), ex);
        }
    }

    public record NasaImageResult(byte[] bytes, String contentType) {}
}
