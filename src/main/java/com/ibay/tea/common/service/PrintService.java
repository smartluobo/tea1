package com.ibay.tea.common.service;

import com.ibay.tea.entity.TbOrder;
import com.ibay.tea.entity.TbStore;

public interface PrintService {

    void addPrinter(String sn);

    String printOrder(TbOrder tbOrder, TbStore store,int sendType);

    String queryPrinterStatus(String sn);

    boolean queryOrderPrintStatus(String orderPrintId);

    String printOrderItem(TbOrder tbOrder,TbStore store);

}
