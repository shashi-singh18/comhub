package com.shashi.comhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
        name = "Category Response",
        description = "Represents the category details returned by the API."
)
public class CategoryResponse {
    @Schema(
            description = "Unique identifier of the category",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
            description = "Category name",
            example = "Electronics"
    )
    private String name;

    @Schema(
            description = "Category description",
            example = "For electronics products"
    )
    private String description;
}
