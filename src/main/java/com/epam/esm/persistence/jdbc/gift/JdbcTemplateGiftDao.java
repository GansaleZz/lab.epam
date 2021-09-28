package com.epam.esm.persistence.jdbc.gift;

import com.epam.esm.persistence.dao.GiftCertificate;
import com.epam.esm.persistence.dao.Tag;
import com.epam.esm.persistence.jdbc.tag.JdbcTemplateTagDao;
import com.epam.esm.persistence.util.search.QueryOrder;
import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.persistence.util.mapper.GiftMapperDb;
import com.epam.esm.util.validation.BaseGiftValidator;
import com.epam.esm.web.exception.EntityNotFoundException;
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
public class JdbcTemplateGiftDao implements GiftDao {
    private static final String SQL_FIND_ALL_GIFTS = "SELECT gift_certificate.gift_id, " +
            "gift_certificate.name, " +
            "gift_certificate.description,gift_certificate.price, " +
            "gift_certificate.duration, gift_certificate.create_date, " +
            "gift_certificate.last_update_date, gift_tags.gift_id, " +
            "gift_tags.tag_id, tag.name tag_name FROM gift_certificate " +
            "LEFT JOIN gift_tags ON gift_tags.gift_id = gift_certificate.gift_id " +
            "LEFT JOIN tag ON gift_tags.tag_id = tag.tag_id";

    private static final String SQL_WITH_TAG = " tag.name = '%s' ";
    private static final String SQL_WITH_NAME = " gift_certificate.name LIKE '%%%s%%'";
    private static final String SQL_WITH_DESCRIPTION = " description LIKE '%%%s%%'";

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

    private static final String WHERE = " WHERE ";
    private static final String AND = " AND ";
    private static final String ORDER_BY = " ORDER BY";
    private static final String GIFT_CERTIFICATE_NAME = " gift_certificate.name ";
    private static final String COMMA = ", ";
    private static final String CREATE_DATE = " create_date ";
    private static final String NOT_FOUND = "Requested gift not found (id = %s)";

    private final GiftMapperDb giftMapper;
    private final JdbcTemplate jdbcTemplate;
    private final BaseGiftValidator<GiftCertificate, Long> giftValidation;
    private final JdbcTemplateTagDao jdbcTemplateTagDao;

    @Autowired
    public JdbcTemplateGiftDao(JdbcTemplate jdbcTemplate,
                               BaseGiftValidator<GiftCertificate, Long> giftValidation, GiftMapperDb giftMapper, JdbcTemplateTagDao jdbcTemplateTagDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.giftValidation = giftValidation;
        this.giftMapper = giftMapper;
        this.jdbcTemplateTagDao = jdbcTemplateTagDao;
    }

    @Override
    public List<GiftCertificate> findAllEntities(GiftSearchFilter giftSearchFilter) {
        giftValidation.onBeforeFindAllEntities(giftSearchFilter);
        StringBuilder query = new StringBuilder(SQL_FIND_ALL_GIFTS);
        StringBuilder queryParams = new StringBuilder();
        StringBuilder orderParams = new StringBuilder();

        addTag(giftSearchFilter, queryParams);
        addPartOfName(giftSearchFilter, queryParams);
        addPartOfDescription(giftSearchFilter, queryParams);
        addOrderOfSearch(giftSearchFilter, orderParams);

        query.append(queryParams);
        query.append(orderParams);
        return jdbcTemplate.query(query.toString(), giftMapper);
    }

    @Override
    public Optional<GiftCertificate> findEntityById(Long id) {
        return jdbcTemplate.query(SQL_FIND_GIFT_BY_ID,giftMapper, id).stream().findAny();
    }

    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) {
        giftValidation.onBeforeInsert(giftCertificate);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(SQL_CREATE_GIFT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, giftCertificate.getName());
            preparedStatement.setString(2, giftCertificate.getDescription());
            preparedStatement.setDouble(3, giftCertificate.getPrice());
            preparedStatement.setInt(4, giftCertificate.getDuration());
            preparedStatement.setDate(5,
                    Date.valueOf(giftCertificate.getCreateDate().toLocalDate()));
            preparedStatement.setDate(6,
                    Date.valueOf(giftCertificate.getLastUpdateDate().toLocalDate()));
            return preparedStatement;
        }, keyHolder);

        giftCertificate.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        giftCertificate.getTags()
                .forEach(i -> {
                    checkExistence(i);
                    jdbcTemplate
                            .update(SQL_INSERT_GIFT_TAGS,giftCertificate.getId(),i.getId());
                });

        return giftCertificate;
    }


    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        GiftCertificate giftCertificateUpdated = jdbcTemplate.query(SQL_FIND_GIFT_BY_ID,
                giftMapper, giftCertificate.getId() ).stream()
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND, giftCertificate.getId())));

        if (giftCertificate.getName() != null) {
            giftCertificateUpdated.setName(giftCertificate.getName());
        }
        if (giftCertificate.getDescription() != null) {
            giftCertificateUpdated.setDescription(giftCertificate.getDescription());
        }
        if (giftCertificate.getDuration() != null) {
            giftCertificateUpdated.setDuration(giftCertificate.getDuration());
        }
        if (giftCertificate.getPrice() != null) {
            giftCertificateUpdated.setPrice(giftCertificate.getPrice());
        }

        jdbcTemplate.update(SQL_UPDATE_GIFT, giftCertificateUpdated.getName(),
                giftCertificateUpdated.getDescription(), giftCertificateUpdated.getPrice(),
                giftCertificateUpdated.getDuration(),
                Date.valueOf(LocalDateTime.now().toLocalDate()),
                giftCertificateUpdated.getId());
        if (giftCertificate.getTags() != null) {
            if (!giftCertificate.getTags().isEmpty()) {
                jdbcTemplate.update(SQL_DELETE_GIFT_TAGS, giftCertificate.getId());
                giftCertificate.getTags()
                        .forEach(i -> {
                            checkExistence(i);
                            jdbcTemplate.update(SQL_INSERT_GIFT_TAGS,
                                    giftCertificate.getId(), i.getId());
                        });
                giftCertificateUpdated.setTags(giftCertificate.getTags());
            }
        }

        return giftCertificateUpdated;
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update(SQL_DELETE_GIFT,id) == 1;
    }

    private void checkExistence(Tag tag) {
        if (tag.getId() == null && tag.getName() != null) {
            if (jdbcTemplateTagDao.findTagByName(tag.getName()).isPresent()){
                tag.setId(jdbcTemplateTagDao.findTagByName(tag.getName()).get().getId());
            } else {
                tag.setId(jdbcTemplateTagDao.create(tag).getId());
            }
        }
    }

    private void addTag(GiftSearchFilter giftSearchFilter,
                        StringBuilder queryParams) {
        if (giftSearchFilter.getTag() != null) {
            queryParams.append(WHERE);
            queryParams.append(String.format(SQL_WITH_TAG, giftSearchFilter.getTag()));
        }
    }

    private void addPartOfName(GiftSearchFilter giftSearchFilter,
                               StringBuilder queryParams) {
        if (giftSearchFilter.getGiftName() != null) {
            if (!queryParams.toString().isEmpty()) {
                queryParams.append(AND);
            } else {
                queryParams.append(WHERE);
            }
            queryParams.append(String.format(SQL_WITH_NAME, giftSearchFilter.getGiftName()));
        }
    }

    private void addPartOfDescription(GiftSearchFilter giftSearchFilter,
                                      StringBuilder queryParams) {
        if (giftSearchFilter.getGiftDescription() != null) {
            if (!queryParams.toString().isEmpty()) {
                queryParams.append(AND);
            } else {
                queryParams.append(WHERE);
            }
            queryParams.append(String.format(SQL_WITH_DESCRIPTION, giftSearchFilter.getGiftDescription()));
        }
    }

    private void addOrderOfSearch(GiftSearchFilter giftSearchFilter,
                                  StringBuilder orderParams) {
        if (giftSearchFilter.getGiftsByNameOrder() != QueryOrder.NO ||
                giftSearchFilter.getGiftsByDateOrder() != QueryOrder.NO) {
            orderParams.append(ORDER_BY);
            if (giftSearchFilter.getGiftsByNameOrder() != QueryOrder.NO) {
                orderParams.append(GIFT_CERTIFICATE_NAME)
                        .append(giftSearchFilter.getGiftsByNameOrder());
            }
            if (giftSearchFilter.getGiftsByNameOrder() != QueryOrder.NO &&
                    giftSearchFilter.getGiftsByDateOrder() != QueryOrder.NO) {
                orderParams.append(COMMA);
            }
            if (giftSearchFilter.getGiftsByDateOrder() != QueryOrder.NO) {
                orderParams.append(CREATE_DATE)
                        .append(giftSearchFilter.getGiftsByDateOrder());
            }
        }
    }
}
