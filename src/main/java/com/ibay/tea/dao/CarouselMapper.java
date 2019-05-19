package com.ibay.tea.dao;

import com.ibay.tea.entity.Carousel;
import org.springframework.stereotype.Repository;

@Repository
public interface CarouselMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Carousel record);

    Carousel selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(Carousel record);
}