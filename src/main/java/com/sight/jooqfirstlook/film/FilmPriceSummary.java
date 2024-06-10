package com.sight.jooqfirstlook.film;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class FilmPriceSummary {

    private Long filmId;
    private String title;
    private BigDecimal rentalRate;
    private String priceCategory;
    private Long totalInventory;
}
