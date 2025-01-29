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

    @Cacheable(value = "jsonData", key = "#path")
    public List<Map<String, Object>> readJsonFile(String path) {
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            // Verifica se o arquivo é um arquivo .gz
            InputStream inputStream = path.endsWith(".gz") ? new GZIPInputStream(is) : is;
            return objectMapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            log.error("Error reading JSON file: {}", path, e);
            throw new RuntimeException("Error reading JSON file: " + path, e);
        }
    }

    @Cacheable(value = "jsonData", key = "#path + '_' + #valueType.getName()")
    public <T> List<T> readJsonFile(String path, Class<T> valueType) {
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            // Verifica se o arquivo é um arquivo .gz
            InputStream inputStream = path.endsWith(".gz") ? new GZIPInputStream(is) : is;
            return objectMapper.readValue(inputStream, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
        } catch (IOException e) {
            log.error("Error reading JSON file: {}", path, e);
            throw new RuntimeException("Error reading JSON file: " + path, e);
        }
    }

    // Método de streaming para ler arquivos JSON comprimidos (também lida com GZIP)
    public List<Map<String, Object>> readJsonFileStreaming(String path) {
        try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
            // Verifica se o arquivo é um arquivo .gz
            InputStream stream = path.endsWith(".gz") ? new GZIPInputStream(inputStream) : inputStream;
            JsonParser parser = objectMapper.getFactory().createParser(stream);
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
}
