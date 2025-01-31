package com.recife.observatorio_economico.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GZSenderTest {
    private final ObjectMapper objectMapper;

    /**
     * Lê um arquivo JSON e retorna uma lista de mapas.
     */
    @Cacheable(value = "jsonData", key = "#path")
    public List<Map<String, Object>> readJsonFile(String path) {
        try (InputStream is = tryGetInputStream(path)) {
            return objectMapper.readValue(is, new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            log.error("Error reading JSON file: {}", path, e);
            throw new RuntimeException("Error reading JSON file: " + path, e);
        }
    }

    /**
     * Método auxiliar para tentar carregar o arquivo .gz primeiro, e se não existir, tenta .json.
     */
    private InputStream tryGetInputStream(String path) throws IOException {
        InputStream inputStream = null;
        if (path.endsWith(".gz")) {
            inputStream = tryGetStream(path);
            if (inputStream == null) {
                // Tenta com o arquivo não comprimido se o .gz não existir
                path = path.substring(0, path.length() - 3); // Remove ".gz" do final do caminho
                inputStream = tryGetStream(path);
            }
        } else {
            inputStream = tryGetStream(path);
        }
        if (inputStream == null) {
            throw new IOException("Arquivo não encontrado: " + path);
        }
        return inputStream;
    }

    /**
     * Método auxiliar para verificar se o arquivo existe no caminho especificado.
     */
    private InputStream tryGetStream(String path) {
        try {
            return new ClassPathResource(path).getInputStream();
        } catch (IOException e) {
            return null; // Se o arquivo não existir, retorna null
        }
    }

    /**
     * Lê um arquivo .gz sem descompactar e retorna o fluxo de bytes.
     */
    public byte[] readGzFileAsBytes(String path) {
        try (InputStream inputStream = tryGetInputStream(path)) {
            if (!path.endsWith(".gz")) {
                throw new IllegalArgumentException("O caminho fornecido não é um arquivo .gz");
            }
            return inputStream.readAllBytes(); // Retorna o conteúdo do arquivo .gz como bytes
        } catch (IOException e) {
            log.error("Erro ao ler arquivo .gz: {}", path, e);
            throw new RuntimeException("Erro ao ler arquivo .gz: " + path, e);
        }
    }
}