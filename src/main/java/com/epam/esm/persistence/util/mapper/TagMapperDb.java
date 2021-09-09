package com.epam.esm.persistence.util.mapper;

import com.epam.esm.persistence.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagMapperDb implements RowMapper<Tag> {

    /**
     * Implementation, which extract result set of all values of tag tables columns
     * and then initializes tags by them
     * @return list of initialized tags
     */
    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Tag.builder()
                .tagId(rs.getLong("tag_id"))
                .name(rs.getString("tag_name"))
                .build();
    }
}
