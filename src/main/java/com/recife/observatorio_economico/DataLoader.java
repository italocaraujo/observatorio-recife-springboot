package com.recife.observatorio_economico;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recife.observatorio_economico.model.IndicadorEconomico;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader {

    private List<IndicadorEconomico> indicadores;

    public DataLoader() {
        this.indicadores = new ArrayList<>();
    }

    // Método para carregar os dados dos arquivos JSON
    public void carregarDados() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Caminho da pasta `data-json` dentro do classpath
        ClassPathResource resource = new ClassPathResource("data-json");
        File dataJsonFolder = resource.getFile();
        File[] categorias = dataJsonFolder.listFiles();

        // Verifica se existem subpastas dentro de `data-json`
        if (categorias != null) {
            for (File categoria : categorias) {
                if (categoria.isDirectory()) {
                    // Para cada subpasta, carrega os arquivos JSON
                    File[] arquivos = categoria.listFiles((dir, name) -> name.endsWith(".json"));
                    if (arquivos != null) {
                        for (File arquivo : arquivos) {
                            // Processa cada arquivo JSON
                            List<IndicadorEconomico> dados = mapper.readValue(arquivo, new TypeReference<>() {});
                            indicadores.addAll(dados); // Adiciona os dados à lista
                            System.out.println("Dados do arquivo " + arquivo.getName() + " na categoria " + categoria.getName() + " carregados.");
                        }
                    }
                }
            }
        }
    }

    public List<IndicadorEconomico> getIndicadores() {
        return indicadores;
    }
}
