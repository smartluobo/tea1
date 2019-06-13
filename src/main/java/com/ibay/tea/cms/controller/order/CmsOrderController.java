package com.ibay.tea.cms.controller.order;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.sendMsg.OrderMessageSendService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.dao.TbOrderMapper;
import com.ibay.tea.entity.TbOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("cms/order")
public class CmsOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsOrderController.class);

    @Resource
    private TbOrderMapper tbOrderMapper;

    @Resource
    private OrderMessageSendService orderMessageSendService;

    @RequestMapping("/orderList/{storeId}/{orderStatus}")
    public ResultInfo orderList(@PathVariable("storeId") int storeId,@PathVariable("orderStatus") int orderStatus){
        try {
            Map<String,Object> condition = new HashMap<>();
            condition.put("storeId",storeId);
            condition.put("orderStatus",orderStatus);
            List<TbOrder> orderList = tbOrderMapper.findOrderListByCondition(condition);
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            resultInfo.setData(orderList);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }


    @RequestMapping("/orderList/{storeId}/{orderId}/{orderStatus}")
    public ResultInfo updateOrder(@PathVariable("storeId") int storeId,@PathVariable("orderId") String orderId ,@PathVariable("orderStatus") int orderStatus){
        try {
            if (orderStatus == ApiConstant.ORDER_STATUS_MAKE_COMPLETE || orderStatus == ApiConstant.ORDER_STATUS_CLOSED){
                Map<String,Object> condition = new HashMap<>();
                //执行更新操作
                condition.put("storeId",storeId);
                condition.put("orderStatus",orderStatus);
                condition.put("orderId",orderId);
                tbOrderMapper.updateOrderStatusByCondition(condition);
                //调用接口完成推送
                if (orderStatus == ApiConstant.ORDER_STATUS_MAKE_COMPLETE){
                    orderMessageSendService.orderMessageSend(orderId,ApiConstant.ORDER_MAKE_COMPLETE_MESSAGE_SEND);
                }else {
                    orderMessageSendService.orderMessageSend(orderId,ApiConstant.ORDER_CLOSE_MESSAGE_SEND);
                }
                return ResultInfo.newSuccessResultInfo();
            }else{
                return ResultInfo.newEmptyParamsResultInfo();
            }
        }catch (Exception e){
            LOGGER.error("updateOrder happen exception orderId : {} orderStatus : {} storeId : {}",orderId,orderStatus,storeId,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
