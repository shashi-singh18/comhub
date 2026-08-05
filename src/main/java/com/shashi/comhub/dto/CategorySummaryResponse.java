package com.shashi.comhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
        name = "Summary information about a category.",
        description = "Represents the product's category's summary response details."
)
public class CategorySummaryResponse {
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
}
