package com.epam.esm.persistence.util.mapper;

import com.epam.esm.persistence.dao.TagDao;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagMapper implements RowMapper<TagDao> {

    /**
     * Implementation, which extract result set of all values of tag tables columns
     * and then initializes tags by them
     * @return list of initialized tags
     */
    @Override
    public TagDao mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TagDao.builder()
                .id(rs.getLong("tag_id"))
                .name(rs.getString("name"))
                .build();
    }
}
