package com.example.api_demo.Service;

import com.example.api_demo.model.bean.CoindeskApiBean;
import com.example.api_demo.service.CoindeskService;
import com.example.api_demo.model.PriceSnapshot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CoindeskServiceTest {
    @Autowired
    private CoindeskService service;

    @Test
    void testTransJsonToDtoAndBack() throws Exception {
        String json = service.fetchCoindeskJson();
        System.out.println("Testing with JSON: " + json);
        CoindeskApiBean bean = service.transJsonToDto(json);
        System.out.println("Converted DTO: " + bean);
        String jsonBack = service.transDtoToJson(bean);
        System.out.println("Converted back to JSON: " + jsonBack);
    }

    @Test
    void testTransDtoToEntityAndBack() throws Exception {
        String json = service.fetchCoindeskJson();
        System.out.println("Testing with JSON: " + json);
        CoindeskApiBean bean = service.transJsonToDto(json);
        System.out.println("Converted DTO: " + bean);
        PriceSnapshot entity = service.transDtoToEntity(bean);
        System.out.println("Converted Entity: " + entity);
        CoindeskApiBean backBean = service.tranEntityToDto(entity);
        System.out.println("Converted back to DTO: " + backBean);
    }
}
