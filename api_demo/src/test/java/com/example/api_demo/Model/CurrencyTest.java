package com.example.api_demo.Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CurrencyTest {
    @Test
    void testGetZhNameByCode() {
        Assertions.assertEquals("美元", Currency.getZhNameByCode("USD"));
        Assertions.assertEquals("英鎊", Currency.getZhNameByCode("GBP"));
        Assertions.assertEquals("歐元", Currency.getZhNameByCode("EUR"));
        Assertions.assertEquals("TWD", Currency.getZhNameByCode("TWD")); // 未定義時回傳原代碼
    }
}
