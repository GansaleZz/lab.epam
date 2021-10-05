package com.epam.esm.service;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.util.search.GiftCertificateSearchFilter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.web.util.exception.EntityBadInputException;
import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.service.giftCertificate.GiftCertificateServiceImpl;
import com.epam.esm.web.util.exception.EntityNotFoundException;
import com.epam.esm.web.util.pagination.PageFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        PageFilter pageFilter = PageFilter.builder()
                .items(paginationItems)
                .build();
        when(giftCertificateMapper.toDto(any())).thenReturn(giftCertificateDto);
        when(giftCertificateDao.findAllGiftCertificates(giftSearchFilter, pageFilter))
                .thenReturn(Collections.singletonList(giftCertificate));

        List<GiftCertificateDto> actualList = giftCertificateService
                .findAllGiftCertificates(giftSearchFilter, pageFilter);

        assertEquals(giftCertificateDto, actualList.get(0));
        verify(giftCertificateDao, times(1))
                .findAllGiftCertificates(giftSearchFilter, pageFilter);
    }

    @Test
    void findAllGiftsWithTagNotExists() {
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .tags(Collections.singletonList("La la la"))
                .build();
        int paginationItems = 1000;
        PageFilter pageFilter = PageFilter.builder()
                .items(paginationItems)
                .build();
        when(giftCertificateDao.findAllGiftCertificates(giftSearchFilter, pageFilter))
                .thenReturn(new ArrayList<>());

        List<GiftCertificateDto> actualList = giftCertificateService
                .findAllGiftCertificates(giftSearchFilter, pageFilter);

        assertEquals(0, actualList.size());
        verify(giftCertificateDao, times(1))
                .findAllGiftCertificates(giftSearchFilter, pageFilter);
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

        GiftCertificateDto actualGiftCertificateDto = giftCertificateService.findGiftCertificateById(1L);

        assertEquals(giftCertificateDto, actualGiftCertificateDto);
        verify(giftCertificateDao,
                times(1)).findEntityById(1L);
    }

    @Test
    void findGiftByIdFailNotFound() {
        when(giftCertificateDao.findEntityById(124124L))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> giftCertificateService.findGiftCertificateById(124124L));
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
        when(giftCertificateDao.createEntity(giftCertificate))
                .thenReturn(giftCertificateResult);

        GiftCertificateDto actualGiftCertificateDto = giftCertificateService.createGiftCertificate(giftCertificateDto);

        assertEquals(giftCertificateDtoResult, actualGiftCertificateDto);
        verify(giftCertificateDao, times(1))
                .createEntity(giftCertificate);
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
        when(giftCertificateDao.createEntity(giftCertificate))
                .thenThrow(EntityBadInputException.class);

        assertThrows(EntityBadInputException.class,
                () -> giftCertificateService.createGiftCertificate(giftCertificateDto));
        verify(giftCertificateDao, times(1))
                .createEntity(any());
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
        when(giftCertificateDao.updateGiftCertificate(any()))
                .thenReturn(giftCertificateResult);

        GiftCertificateDto actualGiftCertificateDto = giftCertificateService.updateGiftCertificate(giftCertificateDto);

        assertEquals(giftCertificateDto, actualGiftCertificateDto);
        verify(giftCertificateDao,
                times(1)).updateGiftCertificate(any());
    }

    @Test
    void deleteSuccess() {
        when(giftCertificateDao.deleteEntity(1L))
                .thenReturn(true);

        boolean result = giftCertificateService.deleteGiftCertificate(1L);

        assertTrue(result);
        verify(giftCertificateDao, times(1)).deleteEntity(1L);
    }
}
