package com.lpdata.action;

import com.alibaba.fastjson.JSONObject;
import com.lpdata.bean.Host;
import com.lpdata.util.MyUtil;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @author zenngwei
 * @date 2018/03/21 16:27
 */
public class BeeeyeHostAction extends ActionSupport {
    private String tableName = "beeeye_host";
    private static SessionFactory sf;
    static {
        //创建SessionFactory对象
        sf = new Configuration().configure().buildSessionFactory();
    }

    public void get() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        String select = " SELECT * FROM  " + tableName;
        String count = " SELECT count(*) FROM  " + tableName;
        Map<String, Object> json = MyUtil.getJsonData(request);
        Map<String, Object> page = (Map<String, Object>) json.get("page");
        int pageNumber = MyUtil.getInt(page, "pageNumber");
        int pageSize = MyUtil.getInt(page, "pageSize");
        int pageStart = (pageNumber - 1) * pageSize;
        String pageSql = " limit :pageStart, :pageSize ";
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        NativeQuery nativeQuery = session.createNativeQuery(select + pageSql);
        nativeQuery.setParameter("pageStart", pageStart);
        nativeQuery.setParameter("pageSize", pageSize);
        //把每一行记录封装为指定的对象类型
        List list = nativeQuery.addEntity(Host.class).list();

        NativeQuery countQuery = session.createNativeQuery(count);
        int totalRow = ((Number)countQuery.uniqueResult()).intValue();
//        int totalRow = ((BigInteger)countQuery.list().get(0)).intValue();
        //关闭
        tx.commit();
        session.close();
        JSONObject pageJson = MyUtil.getPageJson(list, pageNumber, pageSize, totalRow);
        JSONObject data = MyUtil.getJson("成功", 200, pageJson);
        response.getWriter().write(data.toString());
    }
}
