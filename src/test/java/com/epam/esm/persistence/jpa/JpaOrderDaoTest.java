package com.epam.esm.persistence.jpa;

import com.epam.esm.TestConfigJpa;
import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.dao.OrderDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.User;
import com.epam.esm.web.util.pagination.PaginationFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfigJpa.class }, loader = AnnotationConfigContextLoader.class)
@Sql(scripts = "classpath:sql/db-init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:sql/db-clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DataJpaTest
public class JpaOrderDaoTest {

    @Autowired
    private OrderDao jpaOrderDao;

    @Autowired
    private GiftCertificateDao jpaGiftCertificate;

    @Test
    void findOrdersByUserIdExists() {
        long userId = 1L;
        int expectedSize = 3;
        int paginationItems = 1000;
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();

        int actualSize = jpaOrderDao.findOrdersByUserId(paginationFilter, userId).size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void findOrdersByUserIdNotFound() {
        long userId = 1123123L;
        int expectedSize = 0;
        int paginationItems = 1000;
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();

        int actualSize = jpaOrderDao.findOrdersByUserId(paginationFilter, userId).size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void findOrdersByUserIdEmpty() {
        long userId = 4L;
        int expectedSize = 0;
        int paginationItems = 1000;
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();

        int actualSize = jpaOrderDao.findOrdersByUserId(paginationFilter, userId).size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void findOrderByIdExists() {
        long orderId = 1;
        long userId = 1;
        BigDecimal cost = BigDecimal.valueOf(2900.0);

        Optional<Order> order = jpaOrderDao.findOrderById(orderId, userId);

        assertTrue(order.isPresent());
        assertEquals(cost, order.get().getCost());
    }

    @Test
    void findOrderByIdNotFoundUserId() {
        long orderId = 1;
        long userId = 1123;

        Optional<Order> order = jpaOrderDao.findOrderById(orderId, userId);

        assertFalse(order.isPresent());
    }

    @Test
    void findOrderByIdNotFound() {
        long orderId = 1124124;
        long userId = 1;

        Optional<Order> order = jpaOrderDao.findOrderById(orderId, userId);

        assertFalse(order.isPresent());
    }

    @Test
    void createOrderSuccess() {
        User user = User.builder()
                .userId(1L)
                .build();
        GiftCertificate giftCertificate =
                jpaGiftCertificate.findEntityById(1L).get();
        BigDecimal expectedCost = giftCertificate.getPrice();

        Order order = jpaOrderDao.create(giftCertificate, user);

        assertEquals(expectedCost, order.getCost());
    }

    @Test
    void afterUpdateGiftCertificateOrderTheSame() {
        long userId = 1;
        long orderId = 1;
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .giftId(4L)
                .price(BigDecimal.valueOf(4182041824L))
                .build();
        BigDecimal expectedCost = jpaOrderDao.findOrderById(orderId, userId)
                .get().getCost();

        jpaGiftCertificate.update(giftCertificate);
        BigDecimal costAfterChangeGiftCertificate = jpaOrderDao
                .findOrderById(orderId, userId).get().getCost();

        assertEquals(expectedCost, costAfterChangeGiftCertificate);
    }
}
