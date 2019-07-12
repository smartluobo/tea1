package com.ibay.tea.common.service.impl;

import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.service.PrintService;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.common.utils.OrderItemPrintUtil;
import com.ibay.tea.common.utils.PrintUtil;
import com.ibay.tea.dao.TbOrderItemMapper;
import com.ibay.tea.dao.TbPrinterMapper;
import com.ibay.tea.entity.TbOrder;
import com.ibay.tea.entity.TbOrderItem;
import com.ibay.tea.entity.TbPrinter;
import com.ibay.tea.entity.TbStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class PrintServiceImpl implements PrintService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintServiceImpl.class);

    @Resource
    private PrintUtil printUtil;

    @Resource
    private TbPrinterMapper tbPrinterMapper;

    @Resource
    private TbOrderItemMapper tbOrderItemMapper;


    @Override
    public void addPrinter(int printerId) {
        TbPrinter tbPrinter = tbPrinterMapper.findById(printerId);
        String addPrinterInfo = tbPrinter.getPrinterSn()+"#"+tbPrinter.getPrinterKey()+"#"+tbPrinter.getPrinterRemark();
        printUtil.addprinter(addPrinterInfo);
    }



    @Override
    public String printOrder(TbOrder tbOrder, TbStore store,int sendType) {
        TbPrinter printer = tbPrinterMapper.findById(store.getOrderPrinterId());
        if (sendType == ApiConstant.PRINT_TYPE_ORDER_ALL){
            //打印订单和订单明细
            //printUtil.print
            String printContent = buildOrderPrintContent(tbOrder);
            printUtil.print(printer.getPrinterSn(),printContent);

        }else if (sendType == ApiConstant.PRINT_TYPE_ORDER){
            //只打印订单信息
            String printContent = buildOrderPrintContent(tbOrder);
            printUtil.print(printer.getPrinterSn(),printContent);
        } else if (sendType == ApiConstant.PRINT_TYPE_ORDER_ITEM){
            List<TbOrderItem> orderItemList = tbOrderItemMapper.findOrderItemByOrderId(tbOrder.getOrderId());
            for (TbOrderItem orderItem : orderItemList) {

            }
            //全量打印订单明细
        }

        return null;
    }

    private String buildOrderPrintContent(TbOrder tbOrder) {
        List<TbOrderItem> orderItemList = tbOrderItemMapper.findOrderItemByOrderId(tbOrder.getOrderId());
        String printContent = "";
        printContent += "<CB>eecup南山分店</CB><BR>";
        printContent += "<CB>结账单</CB><BR>";
        printContent += "时间: "+tbOrder.getCreateDateStr()+"<BR>";
        printContent += "单号: "+tbOrder.getTakeCode()+"<BR>";
        printContent += "名称          数量         单价<BR>";
        printContent += "-----------------------------<BR>";
        for (TbOrderItem orderItem : orderItemList) {
            printContent += orderItem.getTitle()+"   X"+orderItem.getNum()+"       "+orderItem.getTotalFee()+"<BR>";
        }
        printContent += "-----------------------------<BR>";
        printContent += "合计：                 "+tbOrder.getPayment()+"<BR>";

        return printContent;
    }

    @Override
    public String queryPrinterStatus(String sn) {
        return null;
    }

    @Override
    public boolean queryOrderPrintStatus(String orderPrintId) {
        return false;
    }

    @Override
    public String printOrderItem(TbOrder tbOrder,TbOrderItem orderItem,TbStore store) {

        for (int i = 0;i < orderItem.getNum();i++){
            String printContent = buildOrderItemPrintContent(tbOrder,orderItem);
            OrderItemPrintUtil.sendContent(printContent);
        }

        return null;
    }

    @Override
    public String printOrderItem(TbOrder tbOrder,List<TbOrderItem> orderItemList, TbStore store) {
        tbOrder.setCurrentIndex(1);
        for (TbOrderItem orderItem : orderItemList) {
            printOrderItem(tbOrder,orderItem,store);
        }
        return null;
    }

    private String buildOrderItemPrintContent(TbOrder tbOrder,TbOrderItem orderItem) {
        //订单商品数量
        int goodsCount = tbOrder.getGoodsTotalCount();
        int currentIndex = tbOrder.getCurrentIndex();
        String printContent = "";
        printContent += "\r   订单号:"+tbOrder.getTakeCode()+"   数量:"+currentIndex+"/"+goodsCount+"\r\r";
        printContent += "   <FB><FS>"+orderItem.getTitle()+"</FS></FB>\r";
        printContent += "   "+orderItem.getSkuDetailDesc()+"\r\r";
        printContent += "   时间:"+ DateUtil.viewDateFormat(tbOrder.getCreateTime())+"\r";

        tbOrder.setCurrentIndex(++currentIndex);
        return printContent;
    }
}
