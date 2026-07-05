package com.minimarket.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductoSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void accesoSinAutenticacion_DebeRetornarForbidden()
            throws Exception {

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "CLIENTE")
    void cliente_NoDebeAccederAProductos()
            throws Exception {

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "CAJERO")
    void cajero_DebeAccederAProductos()
            throws Exception {

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk());
    }
}
