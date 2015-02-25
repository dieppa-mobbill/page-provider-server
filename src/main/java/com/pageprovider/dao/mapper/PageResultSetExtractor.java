package com.pageprovider.dao.mapper;

import com.pageprovider.model.Page;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: andyw
 * Date: 24/04/13
 * Time: 17:13
 */
public class PageResultSetExtractor {

    public List<Page> extractData(ResultSet rs) throws SQLException {
        List<Page> modelList = new ArrayList<Page>();
        while (rs.next()) {
            Page page = new Page();
            page.setId(rs.getInt("id"));
            page.setAggregatorsId(rs.getInt("aggregators_id"));
            page.setPaymentPageTypeId(rs.getInt("payment_page_type_id"));
            page.setContentId(rs.getInt("content_id"));
            page.setHtmlFile(rs.getString("html_file"));
            modelList.add(page);
        }
        return modelList;
    }
}

