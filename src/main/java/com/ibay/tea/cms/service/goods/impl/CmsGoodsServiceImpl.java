package com.ibay.tea.cms.service.goods.impl;

import com.ibay.tea.cms.service.goods.CmsGoodsService;
import com.ibay.tea.dao.GoodsMapper;
import com.ibay.tea.dao.TbItemMapper;
import com.ibay.tea.entity.Goods;
import com.ibay.tea.entity.TbItem;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CmsGoodsServiceImpl implements CmsGoodsService {

    @Resource
    private TbItemMapper tbItemMapper;
    @Override
    public List<TbItem> findGoodsListByPage(Integer pageNum, Integer pageSize) {
        int startIndex = (pageNum-1) * pageSize;
        return tbItemMapper.findGoodsListByPage(startIndex,pageSize);
    }

    @Override
    public long countGoodsByCondition(Map<String,Object> condition){
        return tbItemMapper.countGoodsByCondition(condition);
    }

    @Override
    public void updateGoods(TbItem tbItem) {

        TbItem dbGoods = tbItemMapper.selectByPrimaryKey(tbItem.getId());
        if (dbGoods == null){
            return;
        }
        tbItemMapper.deleteGoods(tbItem.getId());
        tbItemMapper.saveUpdateGoods(tbItem);

    }

    @Override
    public void deleteGoods(long id) {
        tbItemMapper.deleteGoods(id);
    }

    @Override
    public void addGoods(TbItem tbItem) {
        tbItemMapper.addGoods(tbItem);
    }
}
