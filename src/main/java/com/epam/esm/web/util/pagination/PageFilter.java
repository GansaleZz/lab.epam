package com.epam.esm.web.util.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageFilter {

    @NotNull
    @Builder.Default
    private int page = 0;

    @NotNull
    @Builder.Default
    private int items = 1;

    private long count;
}
