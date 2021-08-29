package com.epam.esm.service;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.jdbc.gift.JdbcTemplateGiftDaoImpl;
import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.gift.GiftServiceImpl;
import com.epam.esm.service.util.mapper.GiftCertificateMapperImpl;
import com.epam.esm.util.validation.dto.GiftValidationDto;
import com.epam.esm.web.exception.EntityBadInputException;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiftServiceTest {
    @Mock
    private JdbcTemplateGiftDaoImpl jdbcTemplateGiftDao;

    @Mock
    private GiftCertificateMapperImpl giftMapper;

    @Mock
    private GiftValidationDto giftValidationDto;

    @InjectMocks
    private GiftServiceImpl giftService;

    @Test
    void findAllGiftsWithoutSearchFilterParams() {
        try {
            GiftSearchFilter giftSearchFilter = new GiftSearchFilter();
            GiftCertificateDao giftCertificateDao = GiftCertificateDao.builder()
                    .name("Test1")
                    .description("Test_test1")
                    .price(100.0)
                    .duration(10)
                    .build();
            GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                    .name("Test1")
                    .description("Test_test1")
                    .price(100.0)
                    .duration(10)
                    .build();

            when(giftMapper.toDto(any())).thenReturn(giftCertificateDto);
            when(jdbcTemplateGiftDao.findAllEntities(giftSearchFilter))
                    .thenReturn(Arrays.asList(giftCertificateDao));

            assertEquals(giftCertificateDto, giftService.findAllEntities(giftSearchFilter).get(0));
            verify(jdbcTemplateGiftDao, times(1)).findAllEntities(giftSearchFilter);
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findAllGiftsWithTagNotExists() {
        try {
            GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder()
                    .tag("La la la")
                    .build();

            when(jdbcTemplateGiftDao.findAllEntities(giftSearchFilter))
                    .thenReturn(new ArrayList<>());

            assertEquals(0, giftService.findAllEntities(giftSearchFilter).size());
            verify(jdbcTemplateGiftDao, times(1)).findAllEntities(giftSearchFilter);
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }


    @Test
    void findGiftByIdExists() {
        try {
            GiftCertificateDao giftCertificateDao = GiftCertificateDao.builder()
                    .name("Test1")
                    .description("Test_test1")
                    .price(100.0)
                    .duration(10)
                    .build();
            GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                    .name("Test1")
                    .description("Test_test1")
                    .price(100.0)
                    .duration(10)
                    .build();

            when(giftMapper.toDto(any())).thenReturn(giftCertificateDto);
            when(jdbcTemplateGiftDao.findEntityById(1L))
                    .thenReturn(Optional.of(giftCertificateDao));

            assertEquals(giftCertificateDto, giftService.findEntityById(1L).get());
            verify(jdbcTemplateGiftDao, times(1)).findEntityById(1L);
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findGiftByIdFailNotFound() {
        try {
            when(jdbcTemplateGiftDao.findEntityById(124124L))
                    .thenThrow(EntityNotFoundException.class);

            assertThrows(EntityNotFoundException.class, () -> giftService.findEntityById(124124L));
            verify(jdbcTemplateGiftDao, times(1)).findEntityById(124124L);
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void createSuccess() {
        try {
            GiftCertificateDao giftCertificateDao = GiftCertificateDao.builder()
                    .name("Test1")
                    .description("Test_test1")
                    .price(100.0)
                    .duration(10)
                    .build();
            GiftCertificateDao giftCertificateDaoRes = GiftCertificateDao.builder()
                    .id(2L)
                    .name("Test1")
                    .description("Test_test1")
                    .price(100.0)
                    .duration(10)
                    .build();
            GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                    .name("Test1")
                    .description("Test_test1")
                    .price(100.0)
                    .duration(10)
                    .build();
            GiftCertificateDto giftCertificateDtoRes = GiftCertificateDto.builder()
                    .id(2L)
                    .name("Test1")
                    .description("Test_test1")
                    .price(100.0)
                    .duration(10)
                    .build();

            when(giftMapper.toDao(any()))
                    .thenReturn(giftCertificateDao);
            when(giftMapper.toDto(any()))
                    .thenReturn(giftCertificateDtoRes);
            when(jdbcTemplateGiftDao.create(giftCertificateDao))
                    .thenReturn(giftCertificateDaoRes);

            assertEquals(giftCertificateDtoRes, giftService.create(giftCertificateDto));
            verify(jdbcTemplateGiftDao, times(1)).create(giftCertificateDao);
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void createFailBadInput() {
        try {
            GiftCertificateDao giftCertificateDao = GiftCertificateDao.builder()
                    .name("Test1")
                    .build();
            GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                    .name("Test1")
                    .build();

            when(giftMapper.toDao(any())).thenReturn(giftCertificateDao);
            when(jdbcTemplateGiftDao.create(giftCertificateDao))
                    .thenThrow(EntityBadInputException.class);

            assertThrows(EntityBadInputException.class, () -> giftService.create(giftCertificateDto));
            verify(jdbcTemplateGiftDao, times(1))
                    .create(any());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void updateSuccess() {
        try {
            GiftCertificateDao giftCertificateDaoRes = GiftCertificateDao.builder()
                    .id(2L)
                    .name("Test")
                    .description("Test_test1")
                    .price(100.0)
                    .duration(10)
                    .build();
            GiftCertificateDao giftCertificateDao = GiftCertificateDao.builder()
                    .id(2L)
                    .name("Test")
                    .build();
            GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                    .id(2L)
                    .name("Test")
                    .description("Test_test1")
                    .price(100.0)
                    .duration(10)
                    .build();

            when(giftMapper.toDao(any()))
                    .thenReturn(giftCertificateDao);
            when(giftMapper.toDto(any()))
                    .thenReturn(giftCertificateDto);
            when(jdbcTemplateGiftDao.update(giftCertificateDao))
                    .thenReturn(giftCertificateDaoRes);

            assertEquals(giftCertificateDto, giftService.update(giftCertificateDto));
            verify(jdbcTemplateGiftDao, times(1)).update(giftCertificateDao);
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void updateFailNotFound() {
        try {
            GiftCertificateDao giftCertificateDao = GiftCertificateDao.builder()
                    .id(123L)
                    .name("Test name")
                    .build();

            GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                    .id(123L)
                    .name("Test name")
                    .build();

            when(giftMapper.toDao(giftCertificateDto))
                    .thenReturn(giftCertificateDao);
            when(jdbcTemplateGiftDao.update(giftCertificateDao))
                    .thenThrow(EntityNotFoundException.class);

            assertThrows(EntityNotFoundException.class, () -> giftService.update(giftCertificateDto));
            verify(jdbcTemplateGiftDao, times(1))
                    .update(giftCertificateDao);
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void deleteSuccess() {
        try {
            when(jdbcTemplateGiftDao.delete(1L))
                    .thenReturn(true);

            assertTrue(giftService.delete(1L));
            verify(jdbcTemplateGiftDao, times(1)).delete(1L);
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void deleteFailNotFound() {
        try {
            when(jdbcTemplateGiftDao.delete(123L))
                    .thenThrow(EntityNotFoundException.class);

            assertThrows(EntityNotFoundException.class, () -> giftService.delete(123L));
            verify(jdbcTemplateGiftDao, times(1)).delete(123L);
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }
}
