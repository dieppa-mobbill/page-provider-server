package com.pageprovider.dao.impl;

import com.pageprovider.dao.PageDao;
import com.pageprovider.dao.mapper.PageResultSetExtractor;
import com.pageprovider.model.Page;
import com.pageprovider.util.CachePageService;
import com.pageprovider.util.MysqlPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: andyw
 * Date: 24/04/13
 * Time: 17:08
 */
public class PageDaoImpl implements PageDao {

    private static final Logger LOG = Logger.getLogger(PageDaoImpl.class.getName());



    public Page find(int contentId, int pageType) throws Exception{

        Page page = CachePageService.get(contentId, pageType);
        if(page == null){
            StringBuilder sql = new StringBuilder("SELECT * FROM payment_pages p WHERE content_id = ? and payment_page_type_id = ?");

            Connection conn         = null;
            PreparedStatement st    = null;
            try {
                conn = MysqlPool.getConnection();
                st = conn.prepareStatement(sql.toString());

                st.setInt(1, contentId);
                st.setInt(2, pageType);
                ResultSet rs = st.executeQuery();

                List<Page> pages = new PageResultSetExtractor().extractData(rs);

                if (pages.size() > 0) {
                    page = pages.get(0);
                    CachePageService.put(contentId, pageType, page);
                }
            }finally{
                if(st !=null){
                    try{st.close();}
                    catch(Exception ex){
                        LOG.log(Level.WARNING, ex.getMessage());
                    }
                }
                MysqlPool.closeConnection(conn);
            }
        }
        return page;

    }

    public List<Page> findAll()throws Exception{

        StringBuilder sql = new StringBuilder("SELECT * FROM payment_pages");

        Connection conn = MysqlPool.getConnection();
        Statement st    = conn.createStatement();
        ResultSet rs    = st.executeQuery(sql.toString());

        return new PageResultSetExtractor().extractData(rs);

    }

}
