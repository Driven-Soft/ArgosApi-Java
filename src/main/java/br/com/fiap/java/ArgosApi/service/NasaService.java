package br.com.fiap.java.ArgosApi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Service
public class NasaService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public NasaService(ObjectMapper objectMapper,
                       @Value("${nasa.api.key:}") String apiKey) {
        this.httpClient = HttpClient.newBuilder().build();
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }

    public Map<String, Object> getApod() {
        try {
            var uri = URI.create(String.format("https://api.nasa.gov/planetary/apod?api_key=%s", apiKey));
            var req = HttpRequest.newBuilder(uri).GET().header("Accept", "application/json").build();
            var resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200) throw new IllegalStateException("NASA APOD error: " + resp.statusCode());
            return objectMapper.readValue(resp.body(), Map.class);
        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao consultar NASA APOD", ex);
        }
    }

    public NasaImageResult getEarthImageBytes(double lat, double lon, double dim) {
        try {
            var uri = URI.create(String.format(
                    "https://api.nasa.gov/planetary/earth/imagery?lat=%s&lon=%s&dim=%s&api_key=%s",
                    lat, lon, dim, apiKey));

            var req = HttpRequest.newBuilder(uri).GET().build();
            var resp = httpClient.send(req, HttpResponse.BodyHandlers.ofByteArray());
            if (resp.statusCode() != 200) throw new IllegalStateException("NASA Earth imagery error: " + resp.statusCode());

            String contentType = resp.headers().firstValue("Content-Type").orElse(MediaType.IMAGE_PNG_VALUE);
            return new NasaImageResult(resp.body(), contentType);
        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao consultar NASA Earth imagery", ex);
        }
    }

    public static class NasaImageResult {
        public final byte[] bytes;
        public final String contentType;

        public NasaImageResult(byte[] bytes, String contentType) {
            this.bytes = bytes;
            this.contentType = contentType;
        }
    }
}
