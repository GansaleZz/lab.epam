package com.epam.esm.persistence.jdbc.util.mapper;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class GiftMapperDb implements ResultSetExtractor<List<GiftCertificate>> {

    private static final String GIFT_ID = "gift_id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_UPDATE_DATE = "last_update_date";
    private final TagMapperDb tagMapperDb;

    @Autowired
    public GiftMapperDb(TagMapperDb tagMapperDb) {
        this.tagMapperDb = tagMapperDb;
    }

    /**
     * Implementation, which extract result set of all values of gift certificate tables columns
     * and then initializes gift certificates by them
     * @return list of initialized gift certificates
     */
    @Override
    public List<GiftCertificate> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, GiftCertificate> map = new LinkedHashMap<>();
        while (rs.next()) {
            GiftCertificate giftCertificate = map.getOrDefault(rs.getLong(GIFT_ID),
                    GiftCertificate.builder()
                            .giftId(rs.getLong(GIFT_ID))
                            .name(rs.getString(NAME))
                            .description(rs.getString(DESCRIPTION))
                            .price(rs.getBigDecimal(PRICE))
                            .duration(Duration.ofDays(rs.getLong(DURATION)))
                            .createDate(convertToLocalDateTime(rs.getDate(CREATE_DATE)))
                            .lastUpdateDate(convertToLocalDateTime(rs.getDate(LAST_UPDATE_DATE)))
                            .build());
            map.putIfAbsent(giftCertificate.getGiftId(), giftCertificate);

            Tag tag = tagMapperDb.mapRow(rs, rs.getRow());
            if (tag.getTagId() != 0) {
                giftCertificate.getTags().add(tag);
            }
        }
        return new ArrayList<>(map.values());
    }

    private LocalDateTime convertToLocalDateTime (Date date) {
        return new java.sql.Timestamp(
                date.getTime()).toLocalDateTime();
    }
}
