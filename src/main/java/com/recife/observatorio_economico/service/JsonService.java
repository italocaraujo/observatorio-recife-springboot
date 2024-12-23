package com.recife.observatorio_economico.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class JsonService {
    
    private final ObjectMapper objectMapper;

    @Cacheable(value = "jsonData", key = "#path")
    public List<Map<String, Object>> readJsonFile(String path) {
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            return objectMapper.readValue(is, new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            log.error("Error reading JSON file: {}", path, e);
            throw new RuntimeException("Error reading JSON file: " + path, e);
        }
    }

    @Cacheable(value = "jsonData", key = "#path")
    public <T> List<T> readJsonFile(String path, Class<T> valueType) {
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            return objectMapper.readValue(is, objectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
        } catch (IOException e) {
            log.error("Error reading JSON file: {}", path, e);
            throw new RuntimeException("Error reading JSON file: " + path, e);
        }
    }
}
