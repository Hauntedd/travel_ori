package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    //声明业务对象
    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.接收参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");

        //接收rname线路名称(用于搜索阶段)
        String rname = request.getParameter("rname");//rname的使用在Dao中有检查值
        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");//汉字转码,这个好像有问题啊？问题本身不是这个函数，问题在前端
//        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");

        System.out.println("currentPage:" + currentPageStr);
        System.out.println("pageSize:" + pageSizeStr);
        System.out.println("cid:" + cidStr);
        System.out.println("rname:" + rname);

        int cid = 0;//类别ID，默认置为0，防止底层报错
        int currentPage = 0;//当前页码，如果不传递，则默认为第一页
        int pageSize = 0;//每页显示条数，如果不传递，默认每页显示5条记录
        //2.处理参数
        if(cidStr != null && cidStr.length() >0 && !"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }
        if(currentPageStr != null && currentPageStr.length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
        }else {
            currentPage = 1;
        }
        if(pageSizeStr != null && pageSizeStr.length() > 0 && !"null".equals(cidStr)){
            pageSize = Integer.parseInt(pageSizeStr);
        }else {
            pageSize = 5;
        }
        //3.调用service查询pagebean对象
        PageBean<Route> pb = routeService.pageQuery(cid,currentPage,pageSize,rname);
        //4,将pagebean对象序列化返回
        writeValue(pb,response);

    }
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.接收rid
        String rid = request.getParameter("rid");
        //2.调用service查询route对象
        Route route = routeService.findOne(rid);
        //3.写回客户端
        writeValue(route,response);
    }

    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.接收线路rid
        String rid = request.getParameter("rid");
        //2.获取当前封登录的USER
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户的id
        if(user == null){
            //用户尚未登录
            uid = 0;
        }else{
            //用户已登录
            uid = user.getUid();
        }
        //调用FavoriteService查询是否收藏
        boolean flag = favoriteService.isFavorite(rid, uid);

        //写回客户端
        writeValue(flag,response);

    }

    /**
     * 添加收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.接收线路rid
        String rid = request.getParameter("rid");
        //2. 获取当前登录的用户
        User user = (User)request.getSession().getAttribute("user");
        int uid;//用户id
        if(user == null){
            //用户尚未登录
            return ;
        }else{
            //用户已经登录
            uid = user.getUid();
        }

        //调用service添加
        favoriteService.add(rid,uid);
    }

//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//    }//不用记得注释掉啊，我吐了
}
