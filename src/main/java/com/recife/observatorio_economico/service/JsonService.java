package com.recife.observatorio_economico.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
    
    @Cacheable(value = "jsonData", key = "#path + '_' + #valueType.getName()")
    public <T> List<T> readJsonFile(String path, Class<T> valueType) {
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            return objectMapper.readValue(is, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
        } catch (IOException e) {
            log.error("Error reading JSON file: {}", path, e);
            throw new RuntimeException("Error reading JSON file: " + path, e);
        }
    }
    
    public List<Map<String, Object>> readJsonFileStreaming(String path) {
        try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
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
}