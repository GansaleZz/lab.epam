package com.epam.esm.service;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.util.search.GiftCertificateSearchFilter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.web.util.exception.EntityBadInputException;
import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.service.giftCertificate.GiftCertificateServiceImpl;
import com.epam.esm.web.util.exception.EntityNotFoundException;
import com.epam.esm.web.util.pagination.PaginationFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceTest {

    @Mock
    private GiftCertificateDao giftCertificateDao;

    @Mock
    private AbstractEntityMapper<GiftCertificateDto, GiftCertificate> giftCertificateMapper;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Test
    void findAllGiftsWithoutSearchFilterParams() {
        GiftCertificateSearchFilter giftSearchFilter = new GiftCertificateSearchFilter();
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("Test1")
                .description("Test_test1")
                .price(BigDecimal.valueOf(100L))
                .duration(Duration.ofDays(10))
                .build();
        GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                .name("Test1")
                .description("Test_test1")
                .price(BigDecimal.valueOf(100L))
                .duration(Duration.ofDays(10))
                .build();
        int paginationItems = 1000;
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();

        when(giftCertificateMapper.toDto(any())).thenReturn(giftCertificateDto);
        when(giftCertificateDao.findAllGiftCertificates(giftSearchFilter, paginationFilter))
                .thenReturn(Collections.singletonList(giftCertificate));

        assertEquals(giftCertificateDto,
                giftCertificateService.findAllGiftCertificates(giftSearchFilter, paginationFilter).get(0));
        verify(giftCertificateDao, times(1))
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);
    }

    @Test
    void findAllGiftsWithTagNotExists() {
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .tags(Collections.singletonList("La la la"))
                .build();
        int paginationItems = 1000;
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();

        when(giftCertificateDao.findAllGiftCertificates(giftSearchFilter, paginationFilter))
                .thenReturn(new ArrayList<>());

        assertEquals(0, giftCertificateService.findAllGiftCertificates(giftSearchFilter, paginationFilter).size());
        verify(giftCertificateDao, times(1))
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);
    }


    @Test
    void findGiftByIdExists() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("Test1")
                .description("Test_test1")
                .price(BigDecimal.valueOf(100L))
                .duration(Duration.ofDays(10))
                .build();
        GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                .name("Test1")
                .description("Test_test1")
                .price(BigDecimal.valueOf(100L))
                .duration(Duration.ofDays(10))
                .build();

        when(giftCertificateMapper.toDto(any())).thenReturn(giftCertificateDto);
        when(giftCertificateDao.findEntityById(1L))
                .thenReturn(Optional.of(giftCertificate));

        assertEquals(giftCertificateDto, giftCertificateService.findGiftCertificateById(1L));
        verify(giftCertificateDao,
                times(1)).findEntityById(1L);
    }

    @Test
    void findGiftByIdFailNotFound() {
        when(giftCertificateDao.findEntityById(124124L))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.findGiftCertificateById(124124L));
        verify(giftCertificateDao, times(1)).findEntityById(124124L);
    }

    @Test
    void createSuccess() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("Test1")
                .description("Test_test1")
                .price(BigDecimal.valueOf(100L))
                .duration(Duration.ofDays(10))
                .build();
        GiftCertificate giftCertificateResult = GiftCertificate.builder()
                .giftId(2L)
                .name("Test1")
                .description("Test_test1")
                .price(BigDecimal.valueOf(100L))
                .duration(Duration.ofDays(10))
                .build();
        GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                .name("Test1")
                .description("Test_test1")
                .price(BigDecimal.valueOf(100L))
                .duration(Duration.ofDays(10))
                .build();
        GiftCertificateDto giftCertificateDtoResult = GiftCertificateDto.builder()
                .id(2L)
                .name("Test1")
                .description("Test_test1")
                .price(BigDecimal.valueOf(100L))
                .duration(Duration.ofDays(10))
                .build();

        when(giftCertificateMapper.toEntity(any()))
                .thenReturn(giftCertificate);
        when(giftCertificateMapper.toDto(any()))
                .thenReturn(giftCertificateDtoResult);
        when(giftCertificateDao.create(giftCertificate))
                .thenReturn(giftCertificateResult);

        assertEquals(giftCertificateDtoResult, giftCertificateService.create(giftCertificateDto));
        verify(giftCertificateDao, times(1))
                .create(giftCertificate);
    }

    @Test
    void createFailBadInput() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("Test1")
                .build();
        GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                .name("Test1")
                .build();

        when(giftCertificateMapper.toEntity(any())).thenReturn(giftCertificate);
        when(giftCertificateDao.create(giftCertificate))
                .thenThrow(EntityBadInputException.class);

        assertThrows(EntityBadInputException.class, () -> giftCertificateService.create(giftCertificateDto));
        verify(giftCertificateDao, times(1))
                .create(any());
    }

    @Test
    void updateSuccess() {
        GiftCertificate giftCertificateResult = GiftCertificate.builder()
                .giftId(2L)
                .name("Test")
                .description("Test_test1")
                .price(BigDecimal.valueOf(100L))
                .duration(Duration.ofDays(10))
                .build();
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .giftId(2L)
                .name("Test")
                .build();
        GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                .id(2L)
                .name("Test")
                .description("Test_test1")
                .price(BigDecimal.valueOf(100L))
                .duration(Duration.ofDays(10))
                .build();

        when(giftCertificateMapper.toEntity(any()))
                .thenReturn(giftCertificate);
        when(giftCertificateMapper.toDto(any()))
                .thenReturn(giftCertificateDto);
        when(giftCertificateDao.update(any()))
                .thenReturn(giftCertificateResult);
        when(giftCertificateDao.findEntityById(any()))
                .thenReturn(Optional.of(giftCertificateResult));

        assertEquals(giftCertificateDto,
                giftCertificateService.update(giftCertificateDto));
        verify(giftCertificateDao,
                times(1)).update(giftCertificate);
    }

    @Test
    void updateFailNotFound() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .giftId(123L)
                .name("Test name")
                .build();
        GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                .id(123L)
                .name("Test name")
                .build();

        when(giftCertificateDao.findEntityById(any()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> giftCertificateService.update(giftCertificateDto));
        verify(giftCertificateDao, times(0))
                    .update(giftCertificate);
    }

    @Test
    void deleteSuccess() {
        when(giftCertificateDao.delete(1L))
                .thenReturn(true);

        assertTrue(giftCertificateService.delete(1L));
        verify(giftCertificateDao, times(1)).delete(1L);
    }

    @Test
    void deleteFailNotFound() {
        when(giftCertificateDao.delete(123L))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.delete(123L));
        verify(giftCertificateDao, times(1)).delete(123L);
    }
}
