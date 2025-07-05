package com.example.api_demo.Model;

import javax.persistence.*;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "price")
public class Price extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "snapshot_id")
    private PriceSnapshot snapshot;

    @Column(length = 10)
    private String code;

    @Column(length = 10)
    private String symbol;

    @Column(length = 50)
    private String rate;

    @Column(length = 50)
    private String description;

    @Column(name = "rate_float", precision = 18, scale = 6)
    private BigDecimal rateFloat;

}
