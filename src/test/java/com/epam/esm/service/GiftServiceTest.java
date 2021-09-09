package com.epam.esm.service;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.jdbc.gift.JdbcTemplateGiftDao;
import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.gift.GiftServiceImpl;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.web.exception.EntityBadInputException;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiftServiceTest {
    @Mock
    private JdbcTemplateGiftDao jdbcTemplateGiftDao;

    @Mock
    private AbstractEntityMapper<GiftCertificateDto, GiftCertificate> giftMapper;


    @InjectMocks
    private GiftServiceImpl giftService;

    @Test
    void findAllGiftsWithoutSearchFilterParams() {
        GiftSearchFilter giftSearchFilter = new GiftSearchFilter();
        GiftCertificate giftCertificateDao = GiftCertificate.builder()
                .name("Test1")
                .description("Test_test1")
                .price(100.0)
                .duration(Duration.ofDays(10))
                .build();
        GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                .name("Test1")
                .description("Test_test1")
                .price(100.0)
                .duration(Duration.ofDays(10))
                .build();

        when(giftMapper.toDto(any())).thenReturn(giftCertificateDto);
        when(jdbcTemplateGiftDao.findAllEntities(giftSearchFilter))
                .thenReturn(Arrays.asList(giftCertificateDao));

        assertEquals(giftCertificateDto, giftService.findAllGifts(giftSearchFilter).get(0));
        verify(jdbcTemplateGiftDao, times(1)).findAllEntities(giftSearchFilter);
    }

    @Test
    void findAllGiftsWithTagNotExists() {
        GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder()
                .tag("La la la")
                .build();

        when(jdbcTemplateGiftDao.findAllEntities(giftSearchFilter))
                .thenReturn(new ArrayList<>());

        assertEquals(0, giftService.findAllGifts(giftSearchFilter).size());
        verify(jdbcTemplateGiftDao, times(1)).findAllEntities(giftSearchFilter);
    }


    @Test
    void findGiftByIdExists() {
        GiftCertificate giftCertificateDao = GiftCertificate.builder()
                .name("Test1")
                .description("Test_test1")
                .price(100.0)
                .duration(Duration.ofDays(10))
                .build();
        GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                .name("Test1")
                .description("Test_test1")
                .price(100.0)
                .duration(Duration.ofDays(10))
                .build();

        when(giftMapper.toDto(any())).thenReturn(giftCertificateDto);
        when(jdbcTemplateGiftDao.findEntityById(1L))
                .thenReturn(Optional.of(giftCertificateDao));

        assertEquals(giftCertificateDto, giftService.findGiftById(1L));
        verify(jdbcTemplateGiftDao, times(1)).findEntityById(1L);
    }

    @Test
    void findGiftByIdFailNotFound() {
        when(jdbcTemplateGiftDao.findEntityById(124124L))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> giftService.findGiftById(124124L));
        verify(jdbcTemplateGiftDao, times(1)).findEntityById(124124L);
    }

    @Test
    void createSuccess() {
        GiftCertificate giftCertificateDao = GiftCertificate.builder()
                .name("Test1")
                .description("Test_test1")
                .price(100.0)
                .duration(Duration.ofDays(10))
                .build();
        GiftCertificate giftCertificateDaoRes = GiftCertificate.builder()
                .id(2L)
                .name("Test1")
                .description("Test_test1")
                .price(100.0)
                .duration(Duration.ofDays(10))
                .build();
        GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                .name("Test1")
                .description("Test_test1")
                .price(100.0)
                .duration(Duration.ofDays(10))
                .build();
        GiftCertificateDto giftCertificateDtoRes = GiftCertificateDto.builder()
                .id(2L)
                .name("Test1")
                .description("Test_test1")
                .price(100.0)
                .duration(Duration.ofDays(10))
                .build();

        when(giftMapper.toEntity(any()))
                .thenReturn(giftCertificateDao);
        when(giftMapper.toDto(any()))
                .thenReturn(giftCertificateDtoRes);
        when(jdbcTemplateGiftDao.create(giftCertificateDao))
                .thenReturn(giftCertificateDaoRes);

        assertEquals(giftCertificateDtoRes, giftService.create(giftCertificateDto));
        verify(jdbcTemplateGiftDao, times(1)).create(giftCertificateDao);
    }

    @Test
    void createFailBadInput() {
        GiftCertificate giftCertificateDao = GiftCertificate.builder()
                .name("Test1")
                .build();
        GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                .name("Test1")
                .build();

        when(giftMapper.toEntity(any())).thenReturn(giftCertificateDao);
        when(jdbcTemplateGiftDao.create(giftCertificateDao))
                .thenThrow(EntityBadInputException.class);

        assertThrows(EntityBadInputException.class, () -> giftService.create(giftCertificateDto));
        verify(jdbcTemplateGiftDao, times(1))
                .create(any());
    }

    @Test
    void updateSuccess() {
        GiftCertificate giftCertificateDaoRes = GiftCertificate.builder()
                .id(2L)
                .name("Test")
                .description("Test_test1")
                .price(100.0)
                .duration(Duration.ofDays(10))
                .build();
        GiftCertificate giftCertificateDao = GiftCertificate.builder()
                .id(2L)
                .name("Test")
                .build();
        GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                .id(2L)
                .name("Test")
                .description("Test_test1")
                .price(100.0)
                .duration(Duration.ofDays(10))
                .build();

        when(giftMapper.toEntity(any()))
                .thenReturn(giftCertificateDao);
        when(giftMapper.toDto(any()))
                .thenReturn(giftCertificateDto);
        when(jdbcTemplateGiftDao.update(any()))
                .thenReturn(giftCertificateDaoRes);
        when(jdbcTemplateGiftDao.findEntityById(any()))
                .thenReturn(Optional.of(giftCertificateDaoRes));

        assertEquals(giftCertificateDto, giftService.update(giftCertificateDto));
        verify(jdbcTemplateGiftDao, times(1)).update(giftCertificateDao);
    }

    @Test
    void updateFailNotFound() {
        GiftCertificate giftCertificateDao = GiftCertificate.builder()
                .id(123L)
                .name("Test name")
                .build();
        GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                .id(123L)
                .name("Test name")
                .build();

        when(jdbcTemplateGiftDao.findEntityById(any()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> giftService.update(giftCertificateDto));
        verify(jdbcTemplateGiftDao, times(0))
                    .update(giftCertificateDao);
    }

    @Test
    void deleteSuccess() {
        when(jdbcTemplateGiftDao.delete(1L))
                .thenReturn(true);

        assertTrue(giftService.delete(1L));
        verify(jdbcTemplateGiftDao, times(1)).delete(1L);
    }

    @Test
    void deleteFailNotFound() {
        when(jdbcTemplateGiftDao.delete(123L))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> giftService.delete(123L));
        verify(jdbcTemplateGiftDao, times(1)).delete(123L);
    }
}
