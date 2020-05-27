package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.TrueFavorite;

import java.util.List;

public interface FavoriteDao {

    //根据rid和uid查询收藏信息
    public Favorite findByRidAndUid(int rid, int uid);

    //根据rid查询收藏次数
    public int findCountByRid(int rid);

    //添加收藏
    public void add(int rid, int uid);


    //连体操做
    public List<TrueFavorite> findByUid_tap1(int uid);

//    public List<Favorite> findByUid_tap2(int uid);
    public List<Route> findByUid(List<Integer> rids);
}
