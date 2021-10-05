package com.epam.esm.persistence.jdbc.tag;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.jdbc.util.mapper.TagMapperDb;
import com.epam.esm.persistence.jdbc.util.validation.BaseTagValidator;
import com.epam.esm.web.util.pagination.PageFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcTemplateTagDao implements TagDao {
    private static final String SQL_FIND_ALL_TAGS = "SELECT tag_id, name as tag_name FROM tag";
    private static final String SQL_FIND_TAG_BY_NAME = "SELECT tag_id, name as tag_name FROM tag WHERE name = ?";
    private static final String SQL_FIND_TAG_BY_ID = "SELECT tag_id, name as tag_name FROM tag WHERE tag_id = ?";
    private static final String SQL_CREATE_TAG = "INSERT INTO tag(name) VALUES (?)";
    private static final String SQL_DELETE_TAG = "DELETE FROM tag WHERE tag_id = ?";

    private final TagMapperDb tagMapper;
    private final JdbcTemplate jdbcTemplate;
    private final BaseTagValidator<Tag, Long> tagValidation;

    @Autowired
    public JdbcTemplateTagDao(JdbcTemplate jdbcTemplate,
                              BaseTagValidator<Tag, Long> tagValidation,
                              TagMapperDb tagMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagValidation = tagValidation;
        this.tagMapper = tagMapper;
    }

    @Override
    public List<Tag> findAllTags(PageFilter pageFilter) {
        return jdbcTemplate.query(SQL_FIND_ALL_TAGS, tagMapper);
    }

    @Override
    public Optional<Tag> findTagByName(Tag tag) {
        return jdbcTemplate.query(SQL_FIND_TAG_BY_NAME,
                tagMapper, tag.getName()).stream().findAny();
    }

    @Override
    public Tag findMostWidelyUsedTag(Long id) {
        return null;
    }

    @Override
    public Optional<Tag> findEntityById(Long id) {
        return jdbcTemplate.query(SQL_FIND_TAG_BY_ID, tagMapper,
                id).stream().findAny();
    }

    @Override
    public Tag createEntity(Tag tag) {
        tagValidation.onBeforeInsert(tag);
        if (findTagByName(tag).isPresent()) {
            return findTagByName(tag).get();
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_TAG,
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, tag.getName());
                return preparedStatement;
            }, keyHolder);

            tag.setTagId(Objects.requireNonNull(keyHolder.getKey()).longValue());
            return tag;
        }
    }

    @Override
    public boolean deleteEntity(Long id) {
        return jdbcTemplate.update(SQL_DELETE_TAG,id) == 1;
    }
}
