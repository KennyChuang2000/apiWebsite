package com.example.api_demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.api_demo.model.bean.CoindeskApiBean;
import com.example.api_demo.repository.PriceSnapshotRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.api_demo.model.Price;
import com.example.api_demo.model.PriceSnapshot;

@Service
public class CoindeskService {
    private final PriceSnapshotRepository priceSnapshotRepository;

    @Value("${coindesk.api}")
    private String coindeskApiUrl;

    public CoindeskService(PriceSnapshotRepository priceSnapshotRepository) {
        this.priceSnapshotRepository = priceSnapshotRepository;
    }

    public CoindeskApiBean transJsonToDto(String json) throws Exception {
        CoindeskApiBean coindeskApiBean = new CoindeskApiBean();
        ObjectMapper objectMapper = new ObjectMapper();

        coindeskApiBean = objectMapper.readValue(json, CoindeskApiBean.class);

        return coindeskApiBean;
    }

    public String transDtoToJson(CoindeskApiBean coindeskApiBean) throws Exception {
        String json = null;
        ObjectMapper objectMapper = new ObjectMapper();

        json = objectMapper.writeValueAsString(coindeskApiBean);

        return json;
    }

    public PriceSnapshot transDtoToEntity(CoindeskApiBean bean) throws Exception {
        PriceSnapshot snapshot = new PriceSnapshot();

        if (bean.getId() != null) {
            snapshot.setId(bean.getId());
        }

        snapshot.setUpdated(bean.getTime().getUpdated());
        snapshot.setUpdatedISO(bean.getTime().getUpdatedISO() != null
                ? parseUpdatedISO(bean.getTime().getUpdatedISO())
                : null);
        snapshot.setUpdateduk(bean.getTime().getUpdateduk());
        snapshot.setDisclaimer(bean.getDisclaimer());
        snapshot.setChartName(bean.getChartName());

        List<Price> prices = bean.getBpi().entrySet().stream().map(entry -> {
            CoindeskApiBean.BpiDetail detail = entry.getValue();
            Price price = new Price();
            price.setSnapshot(snapshot); // 設定雙向關聯
            price.setCode(detail.getCode());
            price.setSymbol(detail.getSymbol());
            price.setRate(detail.getRate());
            price.setDescription(detail.getDescription());
            price.setRateFloat(BigDecimal.valueOf(detail.getRate_float()));
            return price;
        }).collect(Collectors.toList());

        snapshot.setPrices(prices);

        return snapshot;
    }

    public CoindeskApiBean tranEntityToDto(PriceSnapshot priceSnapshot) throws Exception {
        CoindeskApiBean bean = new CoindeskApiBean();
        // 設定 ID
        bean.setId(priceSnapshot.getId());
        // 轉換時間欄位
        CoindeskApiBean.Time time = new CoindeskApiBean.Time();
        time.setUpdated(priceSnapshot.getUpdated());
        time.setUpdateduk(priceSnapshot.getUpdateduk());
        time.setUpdatedISO(
                priceSnapshot.getUpdatedISO() != null ? priceSnapshot.getUpdatedISO().toString()
                        : null);
        bean.setTime(time);

        // 轉換其他欄位
        bean.setDisclaimer(priceSnapshot.getDisclaimer());
        bean.setChartName(priceSnapshot.getChartName());

        // 轉換 BPI 詳細資料
        Map<String, CoindeskApiBean.BpiDetail> bpi = priceSnapshot.getPrices().stream()
                .collect(Collectors.toMap(Price::getCode, price -> {
                    CoindeskApiBean.BpiDetail detail = new CoindeskApiBean.BpiDetail();
                    detail.setCode(price.getCode());
                    detail.setSymbol(price.getSymbol());
                    detail.setRate(price.getRate());
                    detail.setDescription(price.getDescription());
                    detail.setRate_float(price.getRateFloat().doubleValue());
                    return detail;
                }));
        bean.setBpi(bpi);

        return bean;
    }

    public void savePriceSnapshot(PriceSnapshot priceSnapshot) throws Exception {
        if (priceSnapshot == null) {
            throw new IllegalArgumentException("PriceSnapshot cannot be null");
        }

        priceSnapshotRepository.save(priceSnapshot);
    }

    public PriceSnapshot getPriceSnapshotById(Long id) {
        PriceSnapshot priceSnapshot = priceSnapshotRepository.findById(id).orElse(null);
        return priceSnapshot;
    }

    public void deletePriceSnapshot(PriceSnapshot priceSnapshot) throws Exception {
        if (priceSnapshot == null) {
            throw new IllegalArgumentException("PriceSnapshot cannot be null");
        }
        priceSnapshotRepository.delete(priceSnapshot);
    }

    private LocalDateTime parseUpdatedISO(String isoString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        OffsetDateTime odt = OffsetDateTime.parse(isoString, formatter);
        return odt.toLocalDateTime();
    }

    public String fetchCoindeskJson() throws Exception {
        WebClient webClient = WebClient.create();
        System.out.println("coindesk API URL: " + coindeskApiUrl);

        return webClient.get().uri(coindeskApiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
