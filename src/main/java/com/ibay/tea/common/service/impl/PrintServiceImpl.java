package com.ibay.tea.common.service.impl;

import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.service.PrintService;
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
        printContent += "<CB>--已在线支付--</CB><BR>";
        printContent += "下单时间: "+tbOrder.getCreateDateStr()+"<BR>";
        printContent += "--订单详情--<BR>";
        for (TbOrderItem orderItem : orderItemList) {
            printContent += orderItem.getTitle()+"   X"+orderItem.getNum()+"   "+orderItem.getTotalFee()+"<BR>";
        }
        printContent += "订单金额          "+tbOrder.getOrderPayment()+"<BR>";
        printContent += "订单支付金额      "+tbOrder.getPayment()+"<BR>";

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
    public String printOrderItem(TbOrderItem orderItem,TbStore store) {

        TbPrinter printer = tbPrinterMapper.findById(store.getOrderItemPrinterId());
        String printContent = buildOrderItemPrintContent(orderItem);
        printUtil.print(printer.getPrinterSn(),printContent);
        return null;
    }

    @Override
    public String printOrderItem(List<TbOrderItem> orderItemList, TbStore store) {
        for (TbOrderItem orderItem : orderItemList) {
            printOrderItem(orderItem,store);
        }
        return null;
    }

    private String buildOrderItemPrintContent(TbOrderItem orderItem) {
       String printContent = "";
       printContent += "商品名称："+orderItem.getTitle()+"<BR>";
       printContent += "商品规格："+orderItem.getSkuDetailDesc()+"<BR>";
        return printContent;
    }
}
