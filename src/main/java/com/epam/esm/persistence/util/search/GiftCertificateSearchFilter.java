package com.epam.esm.persistence.util.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCertificateSearchFilter {
    @Builder.Default
    @NotNull(message = "GiftsByNameOrder should not be null")
    private QueryOrder giftsByNameOrder = QueryOrder.NO;

    @Builder.Default
    @NotNull(message = "GiftsByDateOrder should not be null")
    private QueryOrder giftsByDateOrder = QueryOrder.NO;

    private String giftName;

    private String giftDescription;

    @Builder.Default
    private List<String> tags = new ArrayList<>();
}