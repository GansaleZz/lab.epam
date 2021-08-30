package com.epam.esm.persistence.util.mapper;

import com.epam.esm.persistence.dao.GiftCertificate;
import com.epam.esm.persistence.dao.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class GiftMapperDB implements ResultSetExtractor<List<GiftCertificate>> {

    /**
     * Implementation, which extract result set of all values of gift certificate tables columns
     * and then initializes gift certificates by them
     * @return list of initialized gift certificates
     */
    @Override
    public List<GiftCertificate> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, GiftCertificate> map = new LinkedHashMap<>();
        while (rs.next()) {
            GiftCertificate giftCertificate = map.getOrDefault(rs.getLong("gift_id"),
                    GiftCertificate.builder()
                            .id(rs.getLong("gift_id"))
                            .name(rs.getString("name"))
                            .description(rs.getString("description"))
                            .price(rs.getDouble("price"))
                            .duration(rs.getInt("duration"))
                            .createDate(convertToLocalDateTime(rs.getDate("create_date")))
                            .lastUpdateDate(convertToLocalDateTime(rs.getDate("last_update_date")))
                            .build());
            map.putIfAbsent(giftCertificate.getId(), giftCertificate);
            Tag tag = Tag.builder()
                    .id(rs.getLong("tag_id"))
                    .name(rs.getString("tag_name")).build();
            if (tag.getId() != 0) {
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
