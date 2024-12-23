package com.recife.observatorio_economico;

import com.recife.observatorio_economico.model.IndicadorEconomico;
import com.recife.observatorio_economico.service.JsonService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final JsonService jsonService;
    private List<IndicadorEconomico> indicadores = new ArrayList<>();

    @Override
    public void run(String... args) {
        carregarDados();
    }

    public void carregarDados() {
        try {
            String basePath = "data-json";
            File dataJsonFolder = new File(
                getClass().getClassLoader().getResource(basePath).getFile()
            );
            
            if (dataJsonFolder.exists() && dataJsonFolder.isDirectory()) {
                processarPasta(dataJsonFolder, basePath);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar dados: " + e.getMessage(), e);
        }
    }

    private void processarPasta(File pasta, String basePath) {
        File[] arquivos = pasta.listFiles();
        if (arquivos != null) {
            for (File arquivo : arquivos) {
                if (arquivo.isDirectory()) {
                    processarPasta(arquivo, basePath + "/" + arquivo.getName());
                } else if (arquivo.getName().endsWith(".json")) {
                    String path = basePath + "/" + arquivo.getName();
                    List<IndicadorEconomico> dados = jsonService.readJsonFile(path, IndicadorEconomico.class);
                    indicadores.addAll(dados);
                }
            }
        }
    }

    public List<IndicadorEconomico> getIndicadores() {
        return indicadores;
    }
}