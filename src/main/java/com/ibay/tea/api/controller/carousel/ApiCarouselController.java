package com.ibay.tea.api.controller.carousel;

import com.ibay.tea.api.service.carousel.ApiCarouselService;
import com.ibay.tea.entity.Carousel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("api/carousel")
public class ApiCarouselController {

    @Resource
    private ApiCarouselService apiCarouselService;

    @RequestMapping("/findAll")
    public List<Carousel> findAll(){
        return apiCarouselService.findAll();
    }
}
