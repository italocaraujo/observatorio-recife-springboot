package com.recife.observatorio_economico.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class JsonDataService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Map<String, Object>> readJsonFile(String filePath) throws IOException {
        ClassPathResource resource = new ClassPathResource("data-json/" + filePath);
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<>() {});
        }
    }
}
