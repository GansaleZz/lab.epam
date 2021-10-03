package com.epam.esm.service.util.mapper;

public interface AbstractEntityMapper<K,T> {
    /**
     * Transforming entity from dto type to dao.
     * @param k entity which will be transformed.
     * @return result of transforming.
     */
    T toEntity(K k);

    /**
     * Transforming entity from dao type to dto.
     * @param t entity which will be transformed
     * @return result of transforming
     */
    K toDto(T t);
}
