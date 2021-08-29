package com.epam.esm.persistence.jdbc.gift;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.util.search.QueryOrder;
import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.persistence.util.mapper.GiftMapper;
import com.epam.esm.util.validation.BeforeGiftValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Repository
public class JdbcTemplateGiftDaoImpl implements JdbcTemplateGiftDao {
    private static final String SQL_FIND_ALL_GIFTS = "SELECT gift_certificate.gift_id, " +
            "gift_certificate.name, " +
            "gift_certificate.description,gift_certificate.price, " +
            "gift_certificate.duration, gift_certificate.create_date, " +
            "gift_certificate.last_update_date, gift_tags.gift_id, " +
            "gift_tags.tag_id, tag.name tag_name FROM gift_certificate " +
            "LEFT JOIN gift_tags ON gift_tags.gift_id = gift_certificate.gift_id " +
            "LEFT JOIN tag ON gift_tags.tag_id = tag.tag_id";

    private static final String SQL_WITH_TAG = " tag.name = '%s' ";
    private static final String SQL_WITH_NAME = " gift_certificate.name LIKE '%";
    private static final String SQL_WITH_DESCRIPTION = " description LIKE '%";

    private static final String SQL_FIND_GIFT_BY_ID = "SELECT gift_certificate.gift_id, " +
            "gift_certificate.name, tag.name tag_name, " +
            "gift_certificate.description,gift_certificate.price, " +
            "gift_certificate.duration, gift_certificate.create_date, " +
            "gift_certificate.last_update_date, gift_tags.gift_id, " +
            "gift_tags.tag_id FROM gift_certificate " +
            "LEFT JOIN gift_tags ON gift_tags.gift_id = gift_certificate.gift_id " +
            "LEFT JOIN tag ON gift_tags.tag_id = tag.tag_id " +
            "WHERE gift_certificate.gift_id = ?";

    private static final String SQL_CREATE_GIFT = "INSERT INTO gift_certificate" +
            "(name, description, price, duration, create_date, last_update_date) " +
            "VALUES (?,?,?,?,?,?)";

    private static final String SQL_UPDATE_GIFT = "UPDATE gift_certificate " +
            "SET name = ?, description = ?, " +
            "price = ?, duration = ?, last_update_date = ? " +
            "WHERE gift_id = ?";

    private static final String SQL_DELETE_GIFT = "DELETE FROM gift_certificate WHERE gift_id = ?";
    private static final String SQL_INSERT_GIFT_TAGS = "INSERT INTO gift_tags(gift_id, tag_id) VALUES (?,?)";
    private static final String SQL_DELETE_GIFT_TAGS = "DELETE FROM gift_tags WHERE gift_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final BeforeGiftValidation<GiftCertificateDao, Long> giftValidation;

    @Autowired
    public JdbcTemplateGiftDaoImpl(JdbcTemplate jdbcTemplate,
                                   BeforeGiftValidation<GiftCertificateDao, Long> giftValidation) {
        this.jdbcTemplate = jdbcTemplate;
        this.giftValidation = giftValidation;
    }

    @Override
    public List<GiftCertificateDao> findAllEntities(GiftSearchFilter giftSearchFilter) {
        giftValidation.onBeforeFindAllEntities(giftSearchFilter);
        StringBuilder query = new StringBuilder(SQL_FIND_ALL_GIFTS);
        StringBuilder queryParams = new StringBuilder();
        StringBuilder orderParams = new StringBuilder();
        if (giftSearchFilter.getTag() != null) {
            queryParams.append(" WHERE ");
            queryParams.append(String.format(SQL_WITH_TAG, giftSearchFilter.getTag()));
        }
        if (giftSearchFilter.getGiftName() != null) {
            if (!queryParams.toString().isEmpty()) {
                queryParams.append(" AND ");
            } else {
                queryParams.append(" WHERE ");
            }
            queryParams.append(SQL_WITH_NAME)
                    .append(giftSearchFilter.getGiftName()).append("%'");
        }
        if (giftSearchFilter.getGiftDescription() != null) {
            if (!queryParams.toString().isEmpty()) {
                queryParams.append(" AND ");
            } else {
                queryParams.append(" WHERE ");
            }
            queryParams.append(SQL_WITH_DESCRIPTION)
                    .append(giftSearchFilter.getGiftDescription()).append("%'");
        }
        if (giftSearchFilter.getGiftsByNameOrder() != QueryOrder.NO ||
                giftSearchFilter.getGiftsByDateOrder() != QueryOrder.NO) {
            orderParams.append(" ORDER BY");
            if (giftSearchFilter.getGiftsByNameOrder() != QueryOrder.NO) {
                orderParams.append(" gift_certificate.name ")
                        .append(giftSearchFilter.getGiftsByNameOrder());
            }
            if (giftSearchFilter.getGiftsByNameOrder() != QueryOrder.NO &&
                    giftSearchFilter.getGiftsByDateOrder() != QueryOrder.NO) {
                orderParams.append(", ");
            }
            if (giftSearchFilter.getGiftsByDateOrder() != QueryOrder.NO) {
                orderParams.append(" create_date ")
                        .append(giftSearchFilter.getGiftsByDateOrder());
            }
        }
        query.append(queryParams);
        query.append(orderParams);
        return jdbcTemplate.query(query.toString(), new GiftMapper());
    }

    @Override
    public Optional<GiftCertificateDao> findEntityById(Long id) {
         Optional<GiftCertificateDao> giftCertificateDao =  jdbcTemplate.query(SQL_FIND_GIFT_BY_ID,
                new Long[]{id}, new GiftMapper()).stream().findAny();
         giftValidation.onBeforeFindEntity(giftCertificateDao, id);
         return giftCertificateDao;
    }

    @Override
    public GiftCertificateDao create(GiftCertificateDao giftCertificate) {
        giftValidation.onBeforeInsert(giftCertificate);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(SQL_CREATE_GIFT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, giftCertificate.getName());
            preparedStatement.setString(2, giftCertificate.getDescription());
            preparedStatement.setDouble(3, giftCertificate.getPrice());
            preparedStatement.setInt(4, giftCertificate.getDuration());
            preparedStatement.setDate(5,
                    Date.valueOf(LocalDateTime.now().toLocalDate()));
            preparedStatement.setDate(6,
                    Date.valueOf(LocalDateTime.now().toLocalDate()));
            return preparedStatement;
        }, keyHolder);

        giftCertificate.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        giftCertificate.getTags()
                .forEach(i -> jdbcTemplate
                        .update(SQL_INSERT_GIFT_TAGS,giftCertificate.getId(),i.getId()));
        return giftCertificate;
    }


    @Override
    public GiftCertificateDao update(GiftCertificateDao giftCertificate) {
        Optional<GiftCertificateDao> giftCertificateDao = jdbcTemplate.query(SQL_FIND_GIFT_BY_ID,
                new Long[]{giftCertificate.getId()}, new GiftMapper()).stream().findAny();

        giftValidation.onBeforeUpdate(giftCertificateDao,
                giftCertificate.getId());

        if (giftCertificate.getName() != null) {
            giftCertificateDao.get().setName(giftCertificate.getName());
        }
        if (giftCertificate.getDescription() != null) {
            giftCertificateDao.get().setDescription(giftCertificate.getDescription());
        }
        if (giftCertificate.getDuration() != null) {
            giftCertificateDao.get().setDuration(giftCertificate.getDuration());
        }
        if (giftCertificate.getPrice() != null) {
            giftCertificateDao.get().setPrice(giftCertificate.getPrice());
        }

        jdbcTemplate.update(SQL_UPDATE_GIFT, giftCertificateDao.get().getName(),
                giftCertificateDao.get().getDescription(), giftCertificateDao.get().getPrice(),
                giftCertificateDao.get().getDuration(),
                Date.valueOf(LocalDateTime.now().toLocalDate()),
                giftCertificateDao.get().getId());

        if (!giftCertificate.getTags().isEmpty()) {
            jdbcTemplate.update(SQL_DELETE_GIFT_TAGS, giftCertificate.getId());
            giftCertificate.getTags()
                    .forEach(i -> jdbcTemplate.update(SQL_INSERT_GIFT_TAGS,
                            giftCertificate.getId(), i.getId()));
        }
        return giftCertificateDao.get();
    }

    @Override
    public boolean delete(Long id) {
        giftValidation.onBeforeDelete(jdbcTemplate.update(SQL_DELETE_GIFT,id) == 1, id);
        return true;
    }
}
