package com.epam.esm.persistence.jdbc.tag;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.util.mapper.TagMapper;
import com.epam.esm.util.validation.BeforeTagValidation;
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
public class JdbcTemplateTagDaoImpl implements JdbcTemplateTagDao {
    private static final String SQL_FIND_ALL_TAGS = "SELECT tag_id, name FROM tag";
    private static final String SQL_FIND_TAG_BY_NAME = "SELECT tag_id, name FROM tag WHERE name = ?";
    private static final String SQL_FIND_TAG_BY_ID = "SELECT tag_id, name FROM tag WHERE tag_id = ?";
    private static final String SQL_CREATE_TAG = "INSERT INTO tag(name) VALUES (?)";
    private static final String SQL_DELETE_TAG = "DELETE FROM tag WHERE tag_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final BeforeTagValidation<TagDao, Long> tagValidation;

    @Autowired
    public JdbcTemplateTagDaoImpl(JdbcTemplate jdbcTemplate, BeforeTagValidation<TagDao, Long> tagValidation) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagValidation = tagValidation;
    }


    @Override
    public List<TagDao> findAllEntities() {
        return jdbcTemplate.query(SQL_FIND_ALL_TAGS, new TagMapper());
    }

    @Override
    public Optional<TagDao> findEntityById(Long id) {
        Optional<TagDao> tagDao = jdbcTemplate.query(SQL_FIND_TAG_BY_ID, new Long[]{id},
                new TagMapper()).stream().findAny();
        tagValidation.onBeforeFindEntity(tagDao, id);
        return tagDao;
    }

    @Override
    public Optional<TagDao> findTagByName(String name) {
        Optional<TagDao> tagDao = jdbcTemplate.query(SQL_FIND_TAG_BY_NAME, new String[]{name},
                new TagMapper()).stream().findAny();
        tagValidation.onBeforeFindEntity(tagDao, name);
        return tagDao;
    }

    @Override
    public TagDao create(TagDao tagDao) {
        tagValidation.onBeforeInsert(tagDao);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_TAG,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, tagDao.getName());
            return preparedStatement;
        }, keyHolder);
        tagDao.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return tagDao;
    }

    @Override
    public boolean delete(Long id) {
        tagValidation.onBeforeDelete(jdbcTemplate.update(SQL_DELETE_TAG,id) == 1, id);
        return true;
    }
}
