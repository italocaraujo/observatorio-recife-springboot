package com.recife.observatorio_economico.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class JsonService {
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
     * Lê um arquivo JSON e retorna uma lista de objetos de um tipo específico.
     */
    @Cacheable(value = "jsonData", key = "#path + '_' + #valueType.getName()")
    public <T> List<T> readJsonFile(String path, Class<T> valueType) {
        try (InputStream is = tryGetInputStream(path)) {
            return objectMapper.readValue(is, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
        } catch (IOException e) {
            log.error("Error reading JSON file: {}", path, e);
            throw new RuntimeException("Error reading JSON file: " + path, e);
        }
    }

    /**
     * Lê um arquivo JSON no formato de mapa de mapas (chave -> objeto).
     */
    @Cacheable(value = "jsonData", key = "#path + '_mapOfMaps'")
    public Map<String, Map<String, Object>> readJsonAsMapOfMaps(String path) {
        try (InputStream is = tryGetInputStream(path)) {
            return objectMapper.readValue(is, new TypeReference<Map<String, Map<String, Object>>>() {});
        } catch (IOException e) {
            log.error("Error reading JSON file as map of maps: {}", path, e);
            throw new RuntimeException("Error reading JSON file as map of maps: " + path, e);
        }
    }

    /**
     * Método de streaming para ler arquivos JSON comprimidos (também lida com GZIP).
     */
    public List<Map<String, Object>> readJsonFileStreaming(String path) {
        try (InputStream inputStream = tryGetInputStream(path)) {
            JsonParser parser = objectMapper.getFactory().createParser(inputStream);
            List<Map<String, Object>> result = new ArrayList<>();
            parser.nextToken();
            while (parser.nextToken() == JsonToken.START_OBJECT) {
                Map<String, Object> item = objectMapper.readValue(parser, Map.class);
                result.add(item);
            }
            return result;
        } catch (IOException e) {
            log.error("Error streaming JSON file: {}", path, e);
            throw new RuntimeException("Error streaming JSON file: " + path, e);
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
            } else {
                inputStream = new GZIPInputStream(inputStream);  // Usa GZIP caso o arquivo seja .gz
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
}