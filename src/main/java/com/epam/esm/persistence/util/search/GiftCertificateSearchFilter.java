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
    @NotNull
    private QueryOrder giftCertificatesByNameOrder = QueryOrder.NO;

    @Builder.Default
    @NotNull
    private QueryOrder giftCertificatesByDateOrder = QueryOrder.NO;

    private String giftCertificateName;

    private String giftCertificateDescription;

    @Builder.Default
    private List<String> tags = new ArrayList<>();
}