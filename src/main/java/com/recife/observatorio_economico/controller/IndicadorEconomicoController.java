package com.recife.observatorio_economico.controller;

import com.recife.observatorio_economico.DataLoader;
import com.recife.observatorio_economico.model.IndicadorEconomico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/indicadores")
public class IndicadorEconomicoController {

    @Autowired
    private DataLoader dataLoader;

    @GetMapping
    public List<IndicadorEconomico> getIndicadores() {
        try {
            dataLoader.carregarDados(); // Carrega os dados ao chamar o endpoint
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataLoader.getIndicadores(); // Retorna os dados carregados
    }
}
