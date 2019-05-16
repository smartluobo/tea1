package com.ibay.dao;

import com.ibay.entity.Carousel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CarouselMapper {
    int insert(Carousel record);

    Carousel findById(int id);
}