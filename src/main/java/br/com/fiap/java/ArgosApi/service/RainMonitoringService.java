package br.com.fiap.java.ArgosApi.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RainMonitoringService {

    private final Random random = new Random(42);

    /**
     * Simula a consulta a uma API externa retornando mm de chuva nas últimas 24h.
     */
    public double getRainMm(double latitude, double longitude) {
        // Simulação determinística simples baseada nas coordenadas
        double seed = Math.abs(latitude * 1000 + longitude);
        return 5 + (seed % 100); // valor entre 5 e 105 aproximadamente
    }
}
