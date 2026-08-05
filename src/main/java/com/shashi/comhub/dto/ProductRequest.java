package com.shashi.comhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
        name = "Product Request",
        description = "Payload required to create or update a product."
)
public class ProductRequest {
    @Schema(
            description = "Product name",
            example = "Laptop"
    )
    @NotBlank(message = "Product name cannot be blank")
    @Size(max = 100)
    private String name;

    @Schema(
            description = "Product description",
            example = "For all type of laptops"
    )
    @NotBlank(message = "Product description cannot be blank")
    @Size(max = 500)
    private String description;

    @Schema(
            description = "Product price",
            example = "75000.00"
    )
    @NotNull(message = "Product price cannot be null")
    @Positive(message = "Product price cannot be 0")
    @Digits(integer = 8, fraction = 2)
    private BigDecimal price;

    @Schema(
            description = "Product brand",
            example = "Hp"
    )
    @NotBlank(message = "Product brand cannot be blank")
    @Size(max = 100)
    private String brand;

    @Schema(
            description = "Product image url",
            example = "https://image1.com/"
    )
    @NotBlank(message = "Product image url cannot be blank")
    @Size(max = 500)
    private String imageUrl;

    @Schema(
            description = "Product stock",
            example = "25"
    )
    @NotNull(message = "Product stock cannot be null")
    @Positive(message = "Product stock cannot be 0")
    private Long stock;

    @Schema(
            description = "Product's category id",
            example = "12"
    )
    @NotNull(message = "Product's category id cannot be null")
    @Positive(message = "Product's category id cannot be 0")
    private Long categoryId;
}
