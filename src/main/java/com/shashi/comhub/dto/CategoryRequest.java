package com.shashi.comhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryRequest {
    @NotBlank(message = "Category name cannot be blank")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Category description cannot be blank")
    @Size(max = 500)
    private String description;
}
