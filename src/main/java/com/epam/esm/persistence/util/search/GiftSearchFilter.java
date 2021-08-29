package com.epam.esm.persistence.util.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftSearchFilter {
    @Builder.Default
    private QueryOrder giftsByNameOrder = QueryOrder.NO;
    @Builder.Default
    private QueryOrder giftsByDateOrder = QueryOrder.NO;
    private String tag;
    private String giftName;
    private String giftDescription;
}