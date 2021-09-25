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
    private Integer page;

    @NotNull(message = "Items should not be null")
    private Integer items;

    @Null
    private Integer count;
}
