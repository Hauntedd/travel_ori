package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.Impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.Impl.RouteDaoImpl;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.TrueFavorite;
import cn.itcast.travel.service.FavoriteService;

import java.util.ArrayList;
import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {

    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    private RouteDao routeDao =new RouteDaoImpl();
    @Override
    public boolean isFavorite(String rid, int uid) {

        Favorite favorite = favoriteDao.findByRidAndUid(Integer.parseInt(rid),uid);

        return (favorite != null);//如果对象有值，则为true，反之，则为false
    }

    @Override
    public void add(String rid, int uid) {
        favoriteDao.add((Integer.parseInt(rid)),uid);
    }

    @Override
    public PageBean<Route> findByUid(int uid) {//这里使用之前写的pageBean,用来传递数据
        PageBean<Route> pb = new PageBean<>();
        List<TrueFavorite> favorites = favoriteDao.findByUid_tap1(uid);
        List<Integer> ridList =new ArrayList<>();
        List<Route> routeList = new ArrayList<>();
        //分离rid数据
        for (TrueFavorite favorite : favorites) {

            ridList.add(favorite.getRid());
        }
        for (Integer rid : ridList) {
            routeList.add(routeDao.findOne(rid));
        }
        pb.setList(routeList);
        return pb;
    }
}
