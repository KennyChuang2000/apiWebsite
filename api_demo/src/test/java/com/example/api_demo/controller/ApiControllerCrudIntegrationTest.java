package com.example.api_demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApiControllerCrudIntegrationTest {
        @Autowired
        private MockMvc mockMvc;

        private final String testJson = "{\n" +
                        "  \"time\": {\n" +
                        "    \"updated\": \"Sep 2, 2024 07:07:20 UTC\",\n" +
                        "    \"updatedISO\": \"2024-09-02T07:07:20+00:00\",\n" +
                        "    \"updateduk\": \"Sep 2, 2024 at 08:07 BST\"\n" +
                        "  },\n" +
                        "  \"disclaimer\": \"just for test\",\n" +
                        "  \"chartName\": \"Bitcoin\",\n" +
                        "  \"bpi\": {\n" +
                        "    \"USD\": {\n" +
                        "      \"code\": \"USD\",\n" +
                        "      \"symbol\": \"&#36;\",\n" +
                        "      \"rate\": \"57,756.298\",\n" +
                        "      \"description\": \"United States Dollar\",\n" +
                        "      \"rate_float\": 57756.2984\n" +
                        "    },\n" +
                        "    \"GBP\": {\n" +
                        "      \"code\": \"GBP\",\n" +
                        "      \"symbol\": \"&pound;\",\n" +
                        "      \"rate\": \"43,984.02\",\n" +
                        "      \"description\": \"British Pound Sterling\",\n" +
                        "      \"rate_float\": 43984.0203\n" +
                        "    },\n" +
                        "    \"EUR\": {\n" +
                        "      \"code\": \"EUR\",\n" +
                        "      \"symbol\": \"&euro;\",\n" +
                        "      \"rate\": \"52,243.287\",\n" +
                        "      \"description\": \"Euro\",\n" +
                        "      \"rate_float\": 52243.2865\n" +
                        "    }\n" +
                        "  }\n" +
                        "}";

        private final String updatedTestJson = "{\n" +
                        "  \"time\": {\n" +
                        "    \"updated\": \"Oct 10, 2025 12:34:56 UTC\",\n" +
                        "    \"updatedISO\": \"2025-10-10T12:34:56+00:00\",\n" +
                        "    \"updateduk\": \"Oct 10, 2025 at 13:34 BST\"\n" +
                        "  },\n" +
                        "  \"disclaimer\": \"updated for test\",\n" +
                        "  \"chartName\": \"Bitcoin Updated\",\n" +
                        "  \"bpi\": {\n" +
                        "    \"USD\": {\n" +
                        "      \"code\": \"USD\",\n" +
                        "      \"symbol\": \"&#36;\",\n" +
                        "      \"rate\": \"60,000.000\",\n" +
                        "      \"description\": \"United States Dollar Updated\",\n" +
                        "      \"rate_float\": 60000.0\n" +
                        "    },\n" +
                        "    \"GBP\": {\n" +
                        "      \"code\": \"GBP\",\n" +
                        "      \"symbol\": \"&pound;\",\n" +
                        "      \"rate\": \"45,000.00\",\n" +
                        "      \"description\": \"British Pound Sterling Updated\",\n" +
                        "      \"rate_float\": 45000.0\n" +
                        "    },\n" +
                        "    \"EUR\": {\n" +
                        "      \"code\": \"EUR\",\n" +
                        "      \"symbol\": \"&euro;\",\n" +
                        "      \"rate\": \"55,000.000\",\n" +
                        "      \"description\": \"Euro Updated\",\n" +
                        "      \"rate_float\": 55000.0\n" +
                        "    }\n" +
                        "  }\n" +
                        "}";

        @Test
        void testPostAndGetAndPutAndDelete() throws Exception {
                // 新增
                String postResponse = mockMvc.perform(post("/coindesk")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testJson))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();
                System.out.println("新增POST 回傳: " + postResponse);

                // 取得最新一筆 id（假設 id=1）
                String getResponse = mockMvc.perform(get("/coindesk/1"))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();
                System.out.println("讀取 GET 回傳: " + getResponse);

                // 更新
                String putResponse = mockMvc.perform(put("/coindesk/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatedTestJson))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();
                System.out.println("更新 PUT 回傳: " + putResponse);

                // 取得更新內容
                String getUpdateResponse = mockMvc.perform(get("/coindesk/1"))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();
                System.out.println("讀取update  GET 回傳: " + getUpdateResponse);

                // 刪除
                String deleteResponse = mockMvc.perform(delete("/coindesk/1"))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();
                System.out.println("刪除 DELETE 回傳: " + deleteResponse);

                // 取得刪除內容
                String getDeleteResponse = mockMvc.perform(get("/coindesk/1"))
                                .andExpect(status().is4xxClientError())
                                .andReturn().getResponse().getContentAsString();
                System.out.println("讀取 delete  GET 回傳: " + getDeleteResponse);
        }
}
