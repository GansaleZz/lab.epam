package com.epam.esm.service.util.mapper;

public interface AbstractEntityMapper<K,T> {
    /**
     * Method which created for transforming entity from dto type to dao.
     * @param k - entity which will be transformed.
     * @return result of transforming.
     */
    T toDao(K k);

    /**
     * Method which created for transforming entity from dao type to dto.
     * @param t - entity which will be transformed
     * @return result of transforming
     */
    K toDto(T t);
}
