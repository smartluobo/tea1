package com.ibay.tea.api.controller.carousel;

import com.ibay.tea.api.service.carousel.ApiCarouselService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/carousel")
public class ApiCarouselController {

    @Resource
    private ApiCarouselService apiCarouselService;
}
