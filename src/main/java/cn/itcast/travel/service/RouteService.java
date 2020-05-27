package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface RouteService {
    //分页查询
    public PageBean<Route> pageQuery(int cid, int currenPage, int pageSize,String rname);

    //rid精确查找，用于详情
    public Route findOne(String rid);
}
