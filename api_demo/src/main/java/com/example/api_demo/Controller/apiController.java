package com.example.api_demo.Controller;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.api_demo.Model.PriceSnapshot;
import com.example.api_demo.Model.Bean.CoindeskApiBean;
import com.example.api_demo.Service.CoindeskService;
import com.example.api_demo.Model.Currency;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/coindesk")
public class apiController {

    private final CoindeskService coindeskService;

    public apiController(CoindeskService coindeskService) {
        this.coindeskService = coindeskService;
    }

    @GetMapping("/callApi")
    public ResponseEntity<String> coinDeskApi(HttpServletRequest request) {

        String json = null;

        try {
            json = coindeskService.fetchCoindeskJson();

            System.out.println("Response from coindesk API: " + json);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    // 新增
    @PostMapping
    public ResponseEntity<String> saveCoinDeskApi(@RequestBody String json) {
        System.out.println("Received JSON: " + json);
        CoindeskApiBean coindeskApiBean = new CoindeskApiBean();
        PriceSnapshot snapshot = new PriceSnapshot();

        try {
            coindeskApiBean = coindeskService.transJsonToDto(json);
            System.out.println("Converted to DTO: " + coindeskApiBean);

            snapshot = coindeskService.transDtoToEntity(coindeskApiBean);
            System.out.println("Converted to Entity: " + snapshot);

            coindeskService.savePriceSnapshot(snapshot);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Saved!", HttpStatus.OK);
    }

    // 更新
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCoinDeskApi(@PathVariable Long id,
            @RequestBody String json) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("Invalid ID");
        }
        if (json == null || json.isEmpty()) {
            return ResponseEntity.badRequest().body("JSON cannot be null or empty");
        }

        try {
            // 檢查是否存在該 ID 的 PriceSnapshot
            if (coindeskService.getPriceSnapshotById(id) == null) {
                return ResponseEntity.badRequest().body("not found priceSnapshot with ID: " + id);
            }

            CoindeskApiBean coindeskApiBean = coindeskService.transJsonToDto(json);

            // 重點錯誤
            if (coindeskApiBean.getId() != null && !coindeskApiBean.getId().equals(id)) {
                return ResponseEntity.badRequest().body("Path ID and JSON ID do not match");
            }

            coindeskApiBean.setId(id);

            PriceSnapshot priceSnapshot = coindeskService.transDtoToEntity(coindeskApiBean);

            coindeskService.savePriceSnapshot(priceSnapshot);

            return ResponseEntity.ok("priceSnapshot" + " " + id + " is Updated");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred: " + e.getMessage());
        }
    }

    // 搜尋
    @GetMapping("/{id}")
    public ResponseEntity<String> readCoinDeskApi(@PathVariable Long id) {
        String json = null;

        if (id == null || id <= 0) {
            return new ResponseEntity<>("ID cannot be null", HttpStatus.BAD_REQUEST);
        }

        try {
            PriceSnapshot snapshot = coindeskService.getPriceSnapshotById(id);

            if (snapshot == null) {
                return new ResponseEntity<>("PriceSnapshot with ID " + id + " not found",
                        HttpStatus.NOT_FOUND);
            }

            CoindeskApiBean coindeskApiBean = coindeskService.tranEntityToDto(snapshot);
            json = coindeskService.transDtoToJson(coindeskApiBean);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String> getAll() {
        // 查詢多筆邏輯
        return ResponseEntity.ok("Get all!");
    }

    // 刪除
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCoinDeskApi(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>("ID cannot be null", HttpStatus.BAD_REQUEST);
        }

        try {
            PriceSnapshot snapshot = coindeskService.getPriceSnapshotById(id);
            if (snapshot == null) {
                return new ResponseEntity<>("PriceSnapshot with ID " + id + " not found",
                        HttpStatus.NOT_FOUND);
            }
            coindeskService.deletePriceSnapshot(snapshot);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("snapshot " + id + " is Deleted", HttpStatus.OK);
    }

    // 轉換api
    @GetMapping("/transApi")
    public ResponseEntity<?> getNewApi() {
        try {
            String json = coindeskService.fetchCoindeskJson();

            CoindeskApiBean bean = coindeskService.transJsonToDto(json);

            Map<String, Object> result = new HashMap<>();

            if (bean.getTime() == null || bean.getTime().getUpdatedISO() == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error: Time or updatedISO is null");
            }

            String formattedTime = bean.getTime().getUpdatedISO().toString();

            result.put("updatedTime", formattedTime);

            List<Map<String, Object>> currencyList = bean.getBpi().values().stream().map(bpi -> {
                Map<String, Object> map = new HashMap<>();
                map.put("code", bpi.getCode());
                map.put("name_zh", Currency.getZhNameByCode(bpi.getCode()));
                map.put("rate", bpi.getRate());
                return map;
            }).collect(Collectors.toList());
            result.put("currencies", currencyList);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

}
