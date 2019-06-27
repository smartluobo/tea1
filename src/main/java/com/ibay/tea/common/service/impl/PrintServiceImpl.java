package com.ibay.tea.common.service.impl;

import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.service.PrintService;
import com.ibay.tea.common.utils.PrintUtil;
import com.ibay.tea.config.PrintSysProperties;
import com.ibay.tea.dao.TbPrinterMapper;
import com.ibay.tea.entity.TbOrder;
import com.ibay.tea.entity.TbStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PrintServiceImpl implements PrintService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintServiceImpl.class);

    @Resource
    private PrintUtil printUtil;

    @Resource
    private TbPrinterMapper tbPrinterMapper;


    @Override
    public void addPrinter(String sn) {
        printUtil.addprinter(sn);
    }



    @Override
    public String printOrder(TbOrder tbOrder, TbStore store,int sendType) {
        if (sendType == ApiConstant.PRINT_TYPE_ORDER_ALL){
            //打印订单和订单明细
            //printUtil.print

        }else if (sendType == ApiConstant.PRINT_TYPE_ORDER){
            //只打印订单信息
        } else if (sendType == ApiConstant.PRINT_TYPE_ORDER_ITEM){
            //全量打印订单明细
        }

        return null;
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
    public String printOrderItem(TbOrder tbOrder,TbStore store) {
        return null;
    }
}
