package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.Impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.Impl.RouteDaoImpl;
import cn.itcast.travel.dao.Impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.Impl.SellerDaoImpl;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.domain.*;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {

    RouteDao routedao = new RouteDaoImpl();

    RouteImgDao routeImgdao = new RouteImgDaoImpl();

    SellerDao sellerdao = new SellerDaoImpl();

    FavoriteDao favoritedao = new FavoriteDaoImpl();

    /**
     * 根据类别进行分页查询
     * @param cid
     * @param currentPage
     * @param pageSize
     * @param rname
     * @return
     */
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        //封装PageBean
        PageBean<Route> pb = new PageBean<>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示的条数
        pb.setPageSize(pageSize);

        //设置记录总数
        int totalCount = routedao.findTotalCount(cid,rname);
        pb.setTotalCount(totalCount);
        //设置当前页面显示的数据集合
        int start = (currentPage - 1) * pageSize;//开始的记录编号
        List<Route> list = routedao.findByPage(cid,start,pageSize,rname);
        pb.setList(list);

        //设置总页数 = 总记录数/每页显示条数
        int totalPage = totalCount % pageSize == 0 ? totalCount/pageSize : (totalCount/pageSize) +1;
        pb.setTotalPage(totalPage);

        return pb;
    }

    /**
     * 根据rid查询单个旅游线路的详情
     * @param rid
     * @return
     */

    @Override
    public Route findOne(String rid) {
        //1.根据rid去route表中查询route对象
        Route route = routedao.findOne(Integer.parseInt(rid));
        //2.根据route的id 查询图片集合信息
        List<RouteImg> routeImgList =routeImgdao.findByRid(Integer.parseInt(rid));
        //2.2将集合设置到route对象
        route.setRouteImgList(routeImgList);
        //3.根据route对象的sid查询商家对象
        Seller seller = sellerdao.findById(route.getSid());
        route.setSeller(seller);

        //4.查询收藏次数
        int count = favoritedao.findCountByRid(route.getRid());
        route.setCount(count);

        return route;
    }
}
