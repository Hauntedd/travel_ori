package cn.itcast.travel.dao.Impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.TrueFavorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

public class FavoriteDaoImpl implements FavoriteDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        Favorite favorite = null;
        try {//这里捕捉主要是用来处理没有收藏的报错，返回null值
            String sql = "select * from tab_favorite where rid = ? and uid = ?";
            favorite = template.queryForObject(sql,new BeanPropertyRowMapper<>(Favorite.class),rid,uid);
        } catch (DataAccessException e) {
            //e.printStackTrace();
        }
        return favorite;
    }

    @Override
    public int findCountByRid(int rid) {
        String sql = "select count(*) from tab_favorite where rid = ?";

        return template.queryForObject(sql,Integer.class,rid);
    }

    @Override
    public void add(int rid, int uid) {
        String sql = "insert into tab_favorite values(?,?,?)";
        template.update(sql,rid,new Date(),uid);

    }

    //查询个人收藏数据
    @Override
    public List<TrueFavorite> findByUid_tap1(int uid) {
        String sql = "select * from tab_favorite where uid = ?";

        List<TrueFavorite> favorites = null;
        //List<Integer> ridLIst =null;

        favorites = template.query(sql,new BeanPropertyRowMapper<>(TrueFavorite.class),uid);
        return favorites;
    }

    @Override
    public List<Route> findByUid(List<Integer> rids) {
        //String sql = "select * from tab_route where rid = ?";

        return null;
    }

//    public static void main(String[] args) {//Favorite{route=null, date='2020-05-16', user=null},我吐了，这个javabean的设置有毒
//        JdbcTemplate template1 = new JdbcTemplate(JDBCUtils.getDataSource());
//        List<TrueFavorite> favorites = null;
//        List<Integer> ridLIst =null;
//        String sql = "select * from tab_favorite where uid = ?";
//        favorites = template1.query(sql,new BeanPropertyRowMapper<>(TrueFavorite.class),3);
//        for (TrueFavorite favorite : favorites) {
//            System.out.println(favorite);
//        }
//    }
}
