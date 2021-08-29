package com.epam.esm.persistence.util.mapper;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.dao.TagDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GiftMapper implements ResultSetExtractor<List<GiftCertificateDao>> {

    /**
     * Implementation, which extract result set of all values of gift certificate tables columns
     * and then initializes gift certificates by them
     * @return list of initialized gift certificates
     */
    @Override
    public List<GiftCertificateDao> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, GiftCertificateDao> map = new LinkedHashMap<>();
        while (rs.next()) {
            GiftCertificateDao giftCertificate = map.getOrDefault(rs.getLong("gift_id"),
                    GiftCertificateDao.builder()
                            .id(rs.getLong("gift_id"))
                            .name(rs.getString("name"))
                            .description(rs.getString("description"))
                            .price(rs.getDouble("price"))
                            .duration(rs.getInt("duration"))
                            .createDate(convertToLocalDateTime(rs.getDate("create_date")))
                            .lastUpdateDate(convertToLocalDateTime(rs.getDate("last_update_date")))
                            .build());
            map.putIfAbsent(giftCertificate.getId(), giftCertificate);
            TagDao tagDao = TagDao.builder()
                    .id(rs.getLong("tag_id"))
                    .name(rs.getString("tag_name")).build();
            if (tagDao.getId() != 0) {
                giftCertificate.addTag(tagDao);
            }
        }
        return new ArrayList<>(map.values());
    }

    private LocalDateTime convertToLocalDateTime (Date date) {
        return new java.sql.Timestamp(
                date.getTime()).toLocalDateTime();
    }
}
