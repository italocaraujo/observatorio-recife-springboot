package com.recife.observatorio_economico.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@Service
public class GzipFileReader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Object> readGzippedJsonAsList(String path) throws IOException {
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new ClassPathResource(path).getInputStream());
             InputStreamReader reader = new InputStreamReader(gzipInputStream);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            return objectMapper.readValue(bufferedReader, List.class);
        }
    }

    public Map<String, Object> readGzippedJsonAsMap(String path) throws IOException {
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new ClassPathResource(path).getInputStream());
             InputStreamReader reader = new InputStreamReader(gzipInputStream);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            return objectMapper.readValue(bufferedReader, Map.class);
        }
    }
}
