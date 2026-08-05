package com.shashi.comhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
        name = "Product Response",
        description = "Represents the product details returned by the API."
)
public class ProductResponse {
    @Schema(
            description = "Unique identifier of the product",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
            description = "Product name",
            example = "Laptop"
    )
    private String name;

    @Schema(
            description = "Product description",
            example = "For all type of laptops"
    )
    private String description;

    @Schema(
            description = "Product price",
            example = "75000.00"
    )
    private BigDecimal price;

    @Schema(
            description = "Product brand",
            example = "Hp"
    )
    private String brand;

    @Schema(
            description = "Product image url",
            example = "https://image1.com/"
    )
    private String imageUrl;

    @Schema(
            description = "Product stock",
            example = "25"
    )
    private Long stock;

    @Schema(
            description = "Product's active status",
            example = "true"
    )
    private Boolean active;

    @Schema(
            description = "Summary of the category to which the product belongs."
    )
    private CategorySummaryResponse category;
}
