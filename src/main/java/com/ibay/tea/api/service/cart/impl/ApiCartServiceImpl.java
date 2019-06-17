package com.ibay.tea.api.service.cart.impl;

import com.ibay.tea.api.service.cart.ApiCartService;
import com.ibay.tea.cache.GoodsCache;
import com.ibay.tea.cache.StoreCache;
import com.ibay.tea.dao.TbCartMapper;
import com.ibay.tea.entity.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ApiCartServiceImpl implements ApiCartService {

    @Resource
    private TbCartMapper tbCartMapper;

    @Resource
    private GoodsCache goodsCache;

    @Resource
    private StoreCache storeCache;

    @Override
    public List<TbItem> findCartGoodsListByOppenId(String oppenId,int storeId) {
        List<TbCart> cartGoodsList = tbCartMapper.findCartGoodsListByOppenId(oppenId);
        TbStore store = storeCache.findStoreById(storeId);
        List<TbItem> result = new ArrayList<>();
        if (cartGoodsList != null && cartGoodsList.size() > 0){
            for (TbCart tbCart : cartGoodsList) {
                TbItem goods = buildCartGoodsInfo(tbCart,store);
                if (goods != null){
                    goods.setCartItemId(tbCart.getId());
                    goods.setSkuDetailDesc(tbCart.getSkuDetailDesc());
                    result.add(goods);
                }
            }
            return result;
        }else {
            return null;
        }
    }

    @Override
    public TbItem findCartGoodsById(int id,int storeId) {
        TbCart tbCart = tbCartMapper.selectByPrimaryKey(id);
        TbStore store = storeCache.findStoreById(storeId);
        if (tbCart != null){
            return buildCartGoodsInfo(tbCart,store);
        }
        return null;
    }

    @Override
    public void addCartItem(TbCart tbCart) {
        TbItem goods = goodsCache.findGoodsById(tbCart.getGoodsId());
        if (goods == null){
            return ;
        }
        tbCart.setCreateTime(new Date());
        tbCartMapper.insert(tbCart);
    }

    @Override
    public TbItem buildCartGoodsInfo(TbCart tbCart, TbStore store) {
        TbItem goodsById = goodsCache.findGoodsById(tbCart.getGoodsId());
        if (goodsById != null){
            goodsById = goodsById.copy();
            //实时计算价格
            try {
                if (goodsById.getShowActivityPrice() == 1){
                    goodsById.setCartPrice(goodsById.getActivityPrice());
                }else {
                    goodsById.setCartPrice(goodsById.getPrice());
                }
            }catch (Exception e){

            }

            //设置从购物车点击进去后高亮显示的sku
            String skuDetailIds = tbCart.getSkuDetailIds();
            if (skuDetailIds != null && skuDetailIds.trim().length() > 0){
                setSelectedSkuDetail(goodsById,tbCart.getSkuDetailIds());
            }

            //如果购物车中改商品的数量大于1，cartPrice显示了多个商品价格的总额
            if (tbCart.getItemCount() >= 1){
                goodsById.setCartTotalPrice(goodsById.getCartPrice()*tbCart.getItemCount());
                goodsById.setCartItemCount(tbCart.getItemCount());
            }
           return goodsById;
        }
        return null;
    }

    @Override
    public void cartGoodsDelete(String oppenId, int cartItemId) {
        tbCartMapper.cartGoodsDelete(oppenId,cartItemId);
    }

    @Override
    public void checkGoodsInventory(List<TbItem> cartGoodsList, Integer integer) {

    }


    private void setSelectedSkuDetail(TbItem goods,String skuDetailIds) {
        List<TbSkuType> skuShowInfos = goods.getSkuShowInfos();
        if (skuShowInfos == null || skuShowInfos.size() == 0){
            return;
        }
        if (skuDetailIds != null || skuDetailIds.trim().length() > 0){
            String[] skuDetailArr = skuDetailIds.split(",");
            for (String s : skuDetailArr) {
                for (TbSkuType skuShowInfo : skuShowInfos) {
                    for (TbSkuDetail tbSkuDetail : skuShowInfo.getSkuDetails()) {
                        if (s.equals(String.valueOf(tbSkuDetail.getId()))){
                            tbSkuDetail.setIsSelected(1);
                            //实时计算价格
                            if (tbSkuDetail.getSkuDetailPrice() != 0){
                                goods.setCartPrice(goods.getCartPrice() + tbSkuDetail.getSkuDetailPrice());
                            }
                        }
                    }
                }
            }
        }
    }
}
