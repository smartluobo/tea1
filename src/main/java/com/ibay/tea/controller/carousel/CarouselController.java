package com.ibay.tea.controller.carousel;

import com.ibay.tea.dao.CarouselMapper;
import com.ibay.tea.entity.Carousel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("carousel")
public class CarouselController {

    @Resource
    private CarouselMapper carouselMapper;

    @RequestMapping("/findById/{id}")
    public Carousel findCarouselById(@PathVariable("id") int id){
        Carousel carousel = carouselMapper.selectByPrimaryKey(id);
        return carousel;
    }
}
