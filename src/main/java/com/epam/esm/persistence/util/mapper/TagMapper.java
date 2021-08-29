package com.epam.esm.persistence.util.mapper;

import com.epam.esm.model.dto.TagDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagMapper implements RowMapper<TagDto> {

    @Override
    public TagDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        TagDto tag = TagDto.builder().build();
        tag.setId(rs.getInt("id"));
        tag.setName(rs.getString("name"));
        return tag;
    }
}
