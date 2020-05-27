package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {

    //查询所有线路的数量
    public int findTotalCount(int cid,String rname);

    //根据页码信息查询线路，rname用于搜索使用
    public List<Route> findByPage(int cid, int start, int pageSize, String rname);

    //根据rid查询某个单独的线路，线路详情用
    public Route findOne(int rid);
}
