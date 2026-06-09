package br.com.fiap.java.ArgosApi.service;

import br.com.fiap.java.ArgosApi.exception.ExternalApiException;
import br.com.fiap.java.ArgosApi.exception.ExternalApiTimeoutException;
import br.com.fiap.java.ArgosApi.exception.ExternalApiUnavailableException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.List;

@Service
public class RainMonitoringService {

    private static final Logger log = LoggerFactory.getLogger(RainMonitoringService.class);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiUrl;

    public RainMonitoringService(
            @Value("${weather.api.base-url:https://api.open-meteo.com/v1/forecast}") String apiUrl) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
        this.apiUrl = apiUrl;
    }

    public double getRainMm(double latitude, double longitude) {
        try {
            var uri = URI.create(String.format(
                    "%s?latitude=%s&longitude=%s&daily=precipitation_sum&past_days=1&timezone=auto",
                    apiUrl, latitude, longitude));

            log.info("[Open-Meteo] Chamando: {}", uri);

            var request = HttpRequest.newBuilder(uri)
                    .GET()
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("[Open-Meteo] Status: {}", response.statusCode());

            if (response.statusCode() == 429) {
                throw new ExternalApiUnavailableException(
                        "Limite de requisicoes da API Open-Meteo atingido. Tente novamente em alguns minutos.");
            }

            if (response.statusCode() >= 500) {
                throw new ExternalApiUnavailableException(
                        "Open-Meteo esta com instabilidade (status " + response.statusCode() + "). Tente novamente.");
            }

            if (response.statusCode() != 200) {
                throw new ExternalApiException(
                        "Open-Meteo retornou status inesperado: " + response.statusCode());
            }

            var apiResponse = objectMapper.readValue(response.body(), OpenMeteoResponse.class);

            if (apiResponse.daily == null
                    || apiResponse.daily.precipitation_sum == null
                    || apiResponse.daily.precipitation_sum.isEmpty()) {
                log.warn("[Open-Meteo] precipitation_sum vazio para lat={} lon={}", latitude, longitude);
                return 0.0;
            }

            Double value = apiResponse.daily.precipitation_sum
                    .get(apiResponse.daily.precipitation_sum.size() - 1);

            log.info("[Open-Meteo] Precipitacao: {}mm", value);
            return value != null ? value : 0.0;

        } catch (ExternalApiException | ExternalApiUnavailableException | ExternalApiTimeoutException ex) {
            throw ex; // propaga exceções já tipadas
        } catch (HttpTimeoutException ex) {
            log.error("[Open-Meteo] Timeout: {}", ex.getMessage());
            throw new ExternalApiTimeoutException(
                    "Timeout na conexao com Open-Meteo apos 15 segundos. O servico pode estar sobrecarregado.");
        } catch (ConnectException ex) {
            log.error("[Open-Meteo] Conexao recusada: {}", ex.getMessage());
            throw new ExternalApiUnavailableException(
                    "Nao foi possivel conectar ao Open-Meteo. Verifique a conectividade do servidor.");
        } catch (Exception ex) {
            log.error("[Open-Meteo] Erro inesperado: {} - {}", ex.getClass().getSimpleName(), ex.getMessage());
            throw new ExternalApiException(
                    "Erro ao consultar Open-Meteo: " + ex.getMessage());
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OpenMeteoResponse {
        public Daily daily;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Daily {
        public List<Double> precipitation_sum;
    }
}
