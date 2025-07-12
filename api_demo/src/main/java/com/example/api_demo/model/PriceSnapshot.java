package com.example.api_demo.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "price_snapshot")
public class PriceSnapshot extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "updated")
    private String updated;

    @Column(name = "updated_iso")
    private LocalDateTime updatedISO;

    @Column(length = 50)
    private String updateduk;

    @Column(length = 255)
    private String disclaimer;

    @Column(name = "chart_name", length = 50)
    private String chartName;

    @OneToMany(mappedBy = "snapshot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Price> prices;

}
