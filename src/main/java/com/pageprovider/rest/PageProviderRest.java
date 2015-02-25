package com.pageprovider.rest;

import com.pageprovider.dao.PageDao;
import com.pageprovider.dao.impl.PageDaoImpl;
import com.pageprovider.model.Page;
import com.pageprovider.util.CachePageService;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by antoniop on 25/02/15.
 */

@Path("/page")
@Singleton
@Produces(MediaType.TEXT_HTML)
public class PageProviderRest {


    private PageDao pageDao;

    private final static Logger LOG = Logger.getLogger(PageProviderRest.class.getName());

    public PageProviderRest(){
        this.pageDao = new PageDaoImpl();
        try{
            List<Page> pageList = pageDao.findAll();
            for(Page page : pageList){
                CachePageService.put(page.getContentId(), page.getPaymentPageTypeId(), page);
                LOG.log(Level.INFO, "CACHED "+page.getContentId()+" , "+page.getPaymentPageTypeId());
            }


        }catch(Exception ex){
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }


    @GET
    @Path("{contentId}/{pageType}")
    public String getpage(@PathParam("contentId") int content, @PathParam("pageType") int pageType) throws Exception{

        Page p =this.pageDao.find(content, pageType);
        return p.getHtmlFile();
    }
}
