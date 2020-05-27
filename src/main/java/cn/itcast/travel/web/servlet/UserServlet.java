package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {

    //声明UserServlet业务对象
    private UserService service = new UserServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //验证校验
        String check = request.getParameter("check");//输入的验证码
        //从session中获取验证码，这里获取的是生成验证码图片的时候存进去的验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//为了保证验证码只能使用一次
        //比较验证码
        if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){//忽略大小写

            //验证码不正确
            ResultInfo info = new ResultInfo();
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info对象序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
//            mapper.writeValue(response.getOutputStream(),info);//另一种写回json的方法
            return;
        }
        //1.获取数据（all）
        Map<String, String[]> map = request.getParameterMap();
        System.out.println(map);
        //2.封装对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);//将数据填装进对象
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用service完成注册
        boolean flag = service.regist(user);
        ResultInfo info = new ResultInfo();
        //4.响应结果
        if(flag){
            //注册成功
            info.setFlag(true);
        }else{
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败!");
        }
        //将info对象序列化为json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        //将json数据写回客户端
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
//        mapper.writeValue(response.getOutputStream(),info);
    }

    /**
     * 登录功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //先整一手验证码的校验
        String check = request.getParameter("check");
        //从session中获取验证码
        String checkcode_server = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
        request.getSession().removeAttribute("CHECKCODE_SERVER");//为了保证验证码只能使用一次
        //进行验证
        if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
            //验证码不正确
            ResultInfo info = new ResultInfo();
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            writeValue(info,response);
            return;
        }


        //1.获取用户名和密码数据
        Map<String, String[]> map = request.getParameterMap();
        //2.封装User对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用Service查询
        User u = service.login(user);
        ResultInfo info = new ResultInfo();
        //4.判断用户对象是否为null
        if(u == null){
            //用户名或密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
        //5.判断用户是否激活
        if(u != null && !"Y".equals(u.getStatus())){
            //用户尚未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请先激活");
        }
        //6.判断登录成功
        if (u != null && "Y".equals(u.getStatus())){
            request.getSession().setAttribute("user",u);//登录成功标记
            info.setFlag(true);
        }
        //响应数据
        writeValue(info,response);
    }

    /**
     * 查询单个对象（登录的对象）
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //从session中获取登录用户
        Object user = request.getSession().getAttribute("user");
        writeValue(user,response);
    }

    /**
     * 退出登录功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.销毁session
        request.getSession().invalidate();
        //2.跳转登录页面
        response.sendRedirect(request.getContextPath()+ "/login.html");
    }

    /**
     * 激活功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.获取激活码
        String code = request.getParameter("code");
        if(code != null){
            //2.调用service完成激活
            boolean flag = service.active(code);
            //3.判断标记
            String msg = null;
            if(flag){
                //激活成功

                msg = "激活成功，请<a href='http://localhost/travel/login.html'>登录</a>";
            }else{
                //激活失败
                msg = "激活失败，请联系管理员!";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }

    public void getFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.获取登录状态
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
        //3调用favoriteService查询当前用户下的收藏数据List<Route>
        PageBean<Route> pb = favoriteService.findByUid(uid);//这里我似乎忘记了只能javabean格式的数据，真的傻比
        //写回
        writeValue(pb,response);
    }
}
