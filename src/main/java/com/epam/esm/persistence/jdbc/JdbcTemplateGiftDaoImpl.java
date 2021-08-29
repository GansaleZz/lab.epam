package com.epam.esm.persistence.jdbc;

import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.persistence.dao.GiftDao;
import com.epam.esm.persistence.util.mapper.GiftMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JdbcTemplateGiftDaoImpl implements GiftDao {
    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    @Override
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public List<GiftCertificateDto> findAllEntities() {
        final String SQL = "SELECT * FROM gift_certificate";
        List<GiftCertificateDto> list = new ArrayList<>();
        JdbcTemplateTagDaoImpl jdbcTemplateTagDao = new JdbcTemplateTagDaoImpl();
        jdbcTemplateTagDao.setDataSource(this.dataSource);
        List<Map<String, Object>> listOfGiftsWithTag = findAllGiftsWithTag();

        jdbcTemplate.query(SQL, new GiftMapper())
                .forEach(i -> {
                    Optional<Integer> tagId = Optional.empty();
                    for(Map<String, Object> j : listOfGiftsWithTag){
                        if(j.get("giftId").equals(i.getId())){
                            tagId = Optional.of((Integer) j.get("tagId"));
                        }
                    }
                    tagId.ifPresent(j -> i.setTagName(jdbcTemplateTagDao.findEntityById(j).get().getName()));
                    list.add(i);
                });

        return list;
    }

    @Override
    public Optional<GiftCertificateDto> findEntityById(Integer id) {
        return findAllEntities().stream().filter(i -> id.equals(i.getId())).findAny();
    }

    @Override
    public boolean create(GiftCertificateDto giftCertificate) {
        final String SQL = "INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date) VALUES (?,?,?,?,?,?)";
        return jdbcTemplate.update(SQL, giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(), giftCertificate.getDuration(), giftCertificate.getCreateDate(), giftCertificate.getLastUpdateDate()) == 1;
    }

    @Override
    public boolean create(GiftCertificateDto giftCertificate, TagDto tag) {
        final String SQL_INSERT_GIFT = "INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date) VALUES (?,?,?,?,?,?)";
        final String SQL_INSERT_GIFT_TAGS = "INSERT INTO gift_tags (tagId, giftId) VALUES (?,?)";
        JdbcTemplateTagDaoImpl jdbcTemplateTagDao = new JdbcTemplateTagDaoImpl();
        jdbcTemplateTagDao.setDataSource(dataSource);

        if(!jdbcTemplateTagDao.findTagByName(tag.getName()).isPresent()){
            jdbcTemplateTagDao.create(tag);
        }

        jdbcTemplate.update(SQL_INSERT_GIFT, giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(), giftCertificate.getDuration(), giftCertificate.getCreateDate(), giftCertificate.getLastUpdateDate());
        int tagId = jdbcTemplateTagDao.findTagByName(tag.getName()).get().getId();
        int giftId = findAllEntities().get(findAllEntities().size()-1).getId();
        return jdbcTemplate.update(SQL_INSERT_GIFT_TAGS, tagId, giftId) == 1;
    }

    @Override
    public boolean delete(Integer id) {
        final String SQL = "DELETE FROM gift_certificate WHERE id = ?";
        return jdbcTemplate.update(SQL,id) == 1;
    }

    @Override
    public boolean update(GiftCertificateDto giftCertificate) {
        final String SQL = "UPDATE gift_certificate SET name = ?, description = ?, price = ?, duration = ?, create_date = ?, last_update_date = ? WHERE id = ?";
        return jdbcTemplate.update(SQL, giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(), giftCertificate.getDuration(), giftCertificate.getCreateDate(), giftCertificate.getLastUpdateDate(), giftCertificate.getId()) == 1;
    }

    @Override
    public boolean update(GiftCertificateDto giftCertificate, TagDto tag){
        if(update(giftCertificate)){
            final String SQL = "UPDATE gift_tags SET tagId = ? WHERE giftId = ?";
            JdbcTemplateTagDaoImpl jdbcTemplateTagDao = new JdbcTemplateTagDaoImpl();
            jdbcTemplateTagDao.setDataSource(dataSource);

            if(!jdbcTemplateTagDao.findTagByName(tag.getName()).isPresent()){
                jdbcTemplateTagDao.create(tag);
            }

            int tagId = jdbcTemplateTagDao.findTagByName(tag.getName()).get().getId();

            return jdbcTemplate.update(SQL, tagId, giftCertificate.getId()) == 1;
        }else{
            return false;
        }
    }

    @Override
    public List<GiftCertificateDto> findGiftsByName(String name) {
        return findAllEntities().stream()
                .filter(i -> i.getName().contains(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificateDto> findGiftsByDescription(String description) {
        return findAllEntities().stream()
                .filter(i -> i.getDescription().contains(description))
                .collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificateDto> findGiftsByTag(TagDto tag) {
        JdbcTemplateTagDaoImpl jdbcTemplateTagDao = new JdbcTemplateTagDaoImpl();
        List<GiftCertificateDto> list = new ArrayList<>();
        jdbcTemplateTagDao.setDataSource(dataSource);

        if(jdbcTemplateTagDao.findTagByName(tag.getName()).isPresent() && !findAllGiftsWithTag().isEmpty()) {
            List<Map<String, Object>> listOfGiftsWithTag = findAllGiftsWithTag();

            tag.setId(jdbcTemplateTagDao.findTagByName(tag.getName()).get().getId());
            listOfGiftsWithTag.stream()
                    .filter(i -> i.get("tagId").equals(tag.getId()))
                    .forEach(i -> list.add(findEntityById((Integer) i.get("giftId")).get()));
        }

        return list;
    }

    @Override
    public List<GiftCertificateDto> sortGiftsByNameASC() {
        final String SQL = "SELECT * FROM gift_certificate ORDER BY name ASC";
        return jdbcTemplate.query(SQL, new GiftMapper());
    }

    @Override
    public List<GiftCertificateDto> sortGiftsByNameDESC() {
        final String SQL = "SELECT * FROM gift_certificate ORDER BY name DESC";
        return jdbcTemplate.query(SQL, new GiftMapper());
    }

    @Override
    public List<GiftCertificateDto> sortGiftsByDescriptionASC() {
        final String SQL = "SELECT * FROM gift_certificate ORDER BY description ASC";
        return jdbcTemplate.query(SQL, new GiftMapper());
    }

    @Override
    public List<GiftCertificateDto> sortGiftsByDescriptionDESC() {
        final String SQL = "SELECT * FROM gift_certificate ORDER BY description DESC";
        return jdbcTemplate.query(SQL, new GiftMapper());
    }

    private List<Map<String, Object>> findAllGiftsWithTag(){
        final String SQL = "SELECT * FROM gift_tags";
        return jdbcTemplate.queryForList(SQL);
    }
}
