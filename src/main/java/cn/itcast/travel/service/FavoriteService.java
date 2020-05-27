package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

import java.util.List;

public interface FavoriteService {

    public boolean isFavorite(String rid, int uid);

    public void add(String rid ,int uid);

    public PageBean<Route> findByUid(int uid);
}
