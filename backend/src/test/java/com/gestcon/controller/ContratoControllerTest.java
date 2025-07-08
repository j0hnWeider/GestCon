package com.gestcon.controller;

import com.gestcon.model.Contrato;
import com.gestcon.repository.ContratoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContratoController.class)
public class ContratoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContratoRepository contratoRepository;

    private Contrato contrato;

    @BeforeEach
    public void setup() {
        contrato = new Contrato();
        contrato.setId(1L);
        contrato.setNumeroContrato("12345");
        contrato.setObjeto("Objeto do contrato");
    }

    @Test
    public void testGetAllContratos() throws Exception {
        when(contratoRepository.findAll()).thenReturn(Collections.singletonList(contrato));

        mockMvc.perform(get("/api/contratos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(contrato.getId()))
                .andExpect(jsonPath("$[0].numeroContrato").value(contrato.getNumeroContrato()));
    }

    @Test
    public void testGetContratoById() throws Exception {
        when(contratoRepository.findById(1L)).thenReturn(Optional.of(contrato));

        mockMvc.perform(get("/api/contratos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contrato.getId()))
                .andExpect(jsonPath("$.numeroContrato").value(contrato.getNumeroContrato()));
    }

    @Test
    public void testCreateContrato() throws Exception {
        when(contratoRepository.save(any(Contrato.class))).thenReturn(contrato);

        String contratoJson = "{\"numeroContrato\":\"12345\",\"objeto\":\"Objeto do contrato\"}";

        mockMvc.perform(post("/api/contratos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(contratoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroContrato").value("12345"));
    }

    @Test
    public void testUpdateContrato() throws Exception {
        when(contratoRepository.findById(1L)).thenReturn(Optional.of(contrato));
        when(contratoRepository.save(any(Contrato.class))).thenReturn(contrato);

        String contratoJson = "{\"numeroContrato\":\"12345\",\"objeto\":\"Objeto atualizado\"}";

        mockMvc.perform(put("/api/contratos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(contratoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.objeto").value("Objeto atualizado"));
    }

    @Test
    public void testDeleteContrato() throws Exception {
        when(contratoRepository.findById(1L)).thenReturn(Optional.of(contrato));

        mockMvc.perform(delete("/api/contratos/1"))
                .andExpect(status().isOk());
    }
}
