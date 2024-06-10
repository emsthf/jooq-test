package com.sight.jooqfirstlook.config.converter;

import com.sight.jooqfirstlook.film.FilmPriceSummary;
import org.jooq.impl.EnumConverter;

public class PriceCategoryConverter extends EnumConverter<String, FilmPriceSummary.PriceCategory> {

    public PriceCategoryConverter() {
        super(String.class, FilmPriceSummary.PriceCategory.class, FilmPriceSummary.PriceCategory::getCode);
    }
}
