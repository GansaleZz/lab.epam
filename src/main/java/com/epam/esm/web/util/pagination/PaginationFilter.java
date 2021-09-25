package com.epam.esm.web.util.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationFilter {

    @NotNull(message = "Page should not be null")
    @Builder.Default
    private Integer page = 0;

    @NotNull(message = "Items should not be null")
    @Builder.Default
    private Integer items = 1;

    @Null
    private Integer count;
}
