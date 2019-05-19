package com.ibay.tea.api.service.carousel.impl;

import com.ibay.tea.api.service.carousel.ApiCarouselService;
import com.ibay.tea.dao.CarouselMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ApiCarouselServiceImpl implements ApiCarouselService {

    @Resource
    private CarouselMapper carouselMapper;
}
