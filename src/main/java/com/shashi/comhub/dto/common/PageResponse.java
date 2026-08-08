package com.shashi.comhub.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
        name = "Page Response",
        description = "Generic paginated response."
)
public class PageResponse<T> {

    @Schema(description = "List of records in current page")
    private List<T> data;

    @Schema(description = "Current page number", example = "0")
    private int page;

    @Schema(description = "Page size", example = "10")
    private int size;

    @Schema(description = "Total number of records", example = "125")
    private long totalElements;

    @Schema(description = "Total number of pages", example = "13")
    private int totalPages;

    @Schema(description = "Whether this is the first page")
    private boolean first;

    @Schema(description = "Whether this is the last page")
    private boolean last;

    @Schema(description = "Whether another page exists")
    private boolean hasNext;

    @Schema(description = "Whether previous page exists")
    private boolean hasPrevious;
}