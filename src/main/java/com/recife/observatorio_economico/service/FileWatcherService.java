package com.recife.observatorio_economico.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@EnableScheduling
public class FileWatcherService {
    private final WatchService watchService;
    private final Path directoryPath;
    private final CacheManager cacheManager;
    private final JsonService jsonService;

    public FileWatcherService(CacheManager cacheManager, JsonService jsonService) throws IOException {
        this.cacheManager = cacheManager;
        this.jsonService = jsonService;
        this.watchService = FileSystems.getDefault().newWatchService();
        this.directoryPath = Paths.get("src/main/resources/data-json");
        initializeWatcher();
    }

    @PostConstruct
    private void initializeWatcher() throws IOException {
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
        directoryPath.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);
        log.info("FileWatcher iniciado para o diretório: {}", directoryPath);
    }

    @Scheduled(fixedDelayString = "${file.watcher.delay:5000}")
    public void watchDirectory() {
        try {
            WatchKey key = watchService.poll(1, TimeUnit.SECONDS);
            if (key != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path changedFile = (Path) event.context();
                    log.info("Arquivo modificado: {} - Tipo de evento: {}", changedFile, event.kind());
                    clearCacheAndReloadData(changedFile);
                }
                key.reset();
            }
        } catch (Exception e) {
            log.error("Erro ao monitorar diretório: {}", e.getMessage(), e);
        }
    }

    private void clearCacheAndReloadData(Path changedFile) {
        if (changedFile.toString().endsWith(".json")) {
            cacheManager.getCache("jsonData").clear();
            log.info("Cache limpo após modificação do arquivo: {}", changedFile);
        }
    }
}