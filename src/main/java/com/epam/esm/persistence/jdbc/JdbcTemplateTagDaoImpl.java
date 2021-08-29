package com.epam.esm.persistence.jdbc;

import com.epam.esm.model.dto.TagDto;
import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.util.mapper.TagMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcTemplateTagDaoImpl implements TagDao {
    private JdbcTemplate jdbcTemplate;

    @Override
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<TagDto> findAllEntities() {
        final String SQL = "SELECT * FROM tag";
        return jdbcTemplate.query(SQL, new TagMapper());
    }

    @Override
    public Optional<TagDto> findEntityById(Integer id) {
        return findAllEntities().stream()
                .filter(i -> id.equals(i.getId()))
                .findAny();
    }

    @Override
    public boolean create(TagDto tag) {
        final String SQL = "INSERT INTO tag (name) VALUES (?)";
        return jdbcTemplate.update(SQL,tag.getName()) == 1;
    }

    @Override
    public boolean delete(Integer id) {
        final String SQL = "DELETE FROM tag WHERE id = ?";
        return jdbcTemplate.update(SQL,id) == 1;
    }

    @Override
    public Optional<TagDto> findTagByName(String name) {
        return findAllEntities().stream()
                .filter(i -> i.getName().equals(name))
                .findAny();
    }
}
