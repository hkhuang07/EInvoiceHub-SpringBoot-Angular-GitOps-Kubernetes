package vn.softz.app.einvoicehub.mapper;

import org.mapstruct.*;

import java.util.List;

/**Interface cơ sở cho tất cả các Mapper
 * Định nghĩa các phương thức chung để chuyển đổi giữa Entity và DTO
 * @param <D> DTO type
 * @param <E> Entity type*/
public interface EntityMapper<D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntity(List<D> dtoList);

    List<D> toDto(List<E> entityList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget E entity, D dto);
}
