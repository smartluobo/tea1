package com.ibay.tea.carousel;

import com.ibay.dao.CarouselMapper;
import com.ibay.entity.Carousel;
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
        Carousel carousel = carouselMapper.findById(id);
        return carousel;
    }
}
