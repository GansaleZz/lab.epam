package com.epam.esm.persistence.jdbc.util.mapper;

import com.epam.esm.persistence.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagMapperDb implements RowMapper<Tag> {

    private static final String TAG_ID = "tag_id";
    private static final String TAG_NAME = "tag_name";

    /**
     * Implementation, which extract result set of all values of tag tables columns
     * and then initializes tags by them
     * @return list of initialized tags
     */
    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Tag.builder()
                .tagId(rs.getLong(TAG_ID))
                .name(rs.getString(TAG_NAME))
                .build();
    }
}
