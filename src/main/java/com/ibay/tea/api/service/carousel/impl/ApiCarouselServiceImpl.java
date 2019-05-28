package com.ibay.tea.api.service.carousel.impl;

import com.ibay.tea.api.service.carousel.ApiCarouselService;
import com.ibay.tea.dao.CarouselMapper;
import com.ibay.tea.entity.Carousel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApiCarouselServiceImpl implements ApiCarouselService {

    @Resource
    private CarouselMapper carouselMapper;

    @Override
    public List<Carousel> findAll() {
        return carouselMapper.findAll();
    }
}
