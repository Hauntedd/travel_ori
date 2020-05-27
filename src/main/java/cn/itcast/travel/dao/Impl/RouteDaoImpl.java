package cn.itcast.travel.dao.Impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int findTotalCount(int cid, String rname) {
        //1.定义sql模板
        String sql = " select count(*) from tab_route where 1=1 ";//1=1后面必须要留一个空格
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//后续的条件
        //2.判断参数是否有值
        if(cid !=0){//无值会在更上层赋值为0
            sb.append(" and cid = ? ");//补充SQL语句
            params.add(cid);//添加参数
        }
        if(rname !=null && rname.length()>0 && !"null".equals(rname)){
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%");//这是模糊查询的格式 like '%value%';
        }
        sql = sb.toString();//转换回string类型
        return template.queryForObject(sql,Integer.class, params.toArray());
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {

        String sql = " select * from tab_route where 1 = 1 ";
        //1.定义sql模板
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//后续的条件
        //2.判断参数是否有值
        if(cid != 0){
            sb.append( " and cid = ? ");
            params.add(cid);//添加？对应的值
        }
        if(rname != null && rname.length() > 0 && !"null".equals(rname)){
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%");  //%是SQL语句中的占位符，就像正则表达式一样用来进行匹配
        }
        sb.append(" limit ? , ? ");//分页条件
        params.add(start);
        params.add(pageSize);

        sql = sb.toString();

        return template.query(sql, new BeanPropertyRowMapper<>(Route.class),params.toArray());
    }

    @Override
    public Route findOne(int rid) {
        String sql = " select * from tab_route where rid = ? ";
        return template.queryForObject(sql,new BeanPropertyRowMapper<>(Route.class),rid);

    }
}
