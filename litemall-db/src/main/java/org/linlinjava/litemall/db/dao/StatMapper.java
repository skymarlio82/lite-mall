package org.linlinjava.litemall.db.dao;

import java.util.List;
import java.util.Map;

public interface StatMapper {

    @SuppressWarnings("rawtypes")
    List<Map> statUser();

    @SuppressWarnings("rawtypes")
    List<Map> statOrder();

    @SuppressWarnings("rawtypes")
    List<Map> statGoods();
}