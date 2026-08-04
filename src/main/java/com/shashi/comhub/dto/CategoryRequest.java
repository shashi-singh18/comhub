package com.shashi.comhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
        name = "Category Request",
        description = "Payload required to create or update a category."
)
public class CategoryRequest {
    @Schema(
            description = "Category name",
            example = "Electronics"
    )
    @NotBlank(message = "Category name cannot be blank")
    @Size(max = 100)
    private String name;

    @Schema(
            description = "Category description",
            example = "For electronics products"
    )
    @NotBlank(message = "Category description cannot be blank")
    @Size(max = 500)
    private String description;
}
