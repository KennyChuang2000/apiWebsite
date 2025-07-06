package com.example.api_demo.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApiControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCallCoindeskApi() throws Exception {
        String response = mockMvc.perform(get("/coindesk/callApi"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andReturn().getResponse().getContentAsString();
        System.out.println("/coindesk/callApi 回傳內容: " + response);
    }

    @Test
    void testTransApi() throws Exception {
        String response = mockMvc.perform(get("/coindesk/transApi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.updatedTime").exists())
                .andExpect(jsonPath("$.currencies").isArray())
                .andReturn().getResponse().getContentAsString();
        System.out.println("/coindesk/transApi 回傳內容: " + response);
    }

}
