package kz.toko.app.mapper;

import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

public interface EntityDtoMapper<E, D> {

    D toDto(E entity);
    E toEntity(D dto);

    default List<D> toDto(final List<E> entities) {
        return entities.stream().map(this::toDto).collect(toUnmodifiableList());
    }

    default List<E> toEntity(final List<D> dtos) {
        return dtos.stream().map(this::toEntity).collect(toUnmodifiableList());
    }
}
