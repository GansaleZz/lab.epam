package com.epam.esm.persistence.util.mapper;

import com.epam.esm.model.dto.GiftCertificateDto;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class GiftMapper implements RowMapper<GiftCertificateDto> {

    @SneakyThrows
    @Override
    public GiftCertificateDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificateDto giftCertificate = GiftCertificateDto.builder().build();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String create_Date = dateFormat.format(rs.getDate("create_date"));
        String last_update_date = dateFormat.format(rs.getDate("last_update_date"));

        giftCertificate.setId(rs.getInt("id"));
        giftCertificate.setName(rs.getString("name"));
        giftCertificate.setDescription(rs.getString("description"));
        giftCertificate.setPrice(rs.getDouble("price"));
        giftCertificate.setDuration(rs.getInt("duration"));
        giftCertificate.setCreateDate(dateFormat.parse(create_Date));
        giftCertificate.setLastUpdateDate(dateFormat.parse(last_update_date));
        return giftCertificate;
    }
}
