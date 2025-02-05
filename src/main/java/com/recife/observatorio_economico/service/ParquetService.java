package com.recife.observatorio_economico.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.hadoop.conf.Configuration;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParquetService {

    /**
     * Lê um arquivo Parquet e retorna os dados em bytes.
     */
    public byte[] readParquetFileAsBytes(String resourcePath) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (inputStream == null) {
                log.error("Arquivo não encontrado no classpath: {}", resourcePath);
                throw new RuntimeException("Arquivo não encontrado no classpath: " + resourcePath);
            }
            return inputStream.readAllBytes();
        } catch (IOException e) {
            log.error("Erro ao ler arquivo Parquet: {}", resourcePath, e);
            throw new RuntimeException("Erro ao ler arquivo Parquet: " + resourcePath, e);
        }
    }

    /**
     * Lê um arquivo Parquet e retorna os dados como uma lista de objetos.
     * @throws URISyntaxException 
     */
    public List<Object> readParquetFileAsList(String resourcePath) throws IOException, URISyntaxException {
        log.debug("Tentando ler arquivo Parquet: {}", resourcePath);

        Configuration conf = new Configuration();
        Path path = Path.of(getClass().getClassLoader().getResource(resourcePath).toURI());

        try (ParquetReader<Group> reader = ParquetReader.builder(new GroupReadSupport(), new org.apache.hadoop.fs.Path(path.toString())).build()) {
            List<Object> result = new ArrayList<>();
            Group group;
            while ((group = reader.read()) != null) {
                result.add(group); // Customize this part to transform the data
            }
            return result;
        } catch (Exception e) {
            log.error("Erro ao processar arquivo Parquet: {}", resourcePath, e);
            throw e;
        }
    }

    /**
     * Lê um arquivo JSON e retorna seu conteúdo como String.
     */
    public String readJsonFile(String resourcePath) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (inputStream == null) {
                log.error("Arquivo JSON não encontrado no classpath: {}", resourcePath);
                throw new RuntimeException("Arquivo JSON não encontrado no classpath: " + resourcePath);
            }
            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            log.error("Erro ao ler arquivo JSON: {}", resourcePath, e);
            throw new RuntimeException("Erro ao ler arquivo JSON: " + resourcePath, e);
        }
    }
}