package com.carpooling.ums.utils;

import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.stream.Collectors;

public class DtoConverter {

    private static final ModelMapper modelMapper = new ModelMapper();

    // Convert an entity to a DTO
    public static <D, T> D convertToDto(T entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    // Convert a list of entities to a list of DTOs
    public static <D, T> List<D> convertToDtoList(List<T> entityList, Class<D> dtoClass) {
        return entityList.stream()
                .map(entity -> convertToDto(entity, dtoClass))
                .collect(Collectors.toList());
    }

    // Convert a DTO to an entity
    public static <T, D> T convertToEntity(D dto, Class<T> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    // Convert a list of DTOs to a list of entities
    public static <T, D> List<T> convertToEntityList(List<D> dtoList, Class<T> entityClass) {
        return dtoList.stream()
                .map(dto -> convertToEntity(dto, entityClass))
                .collect(Collectors.toList());
    }
}
