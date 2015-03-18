package com.pageprovider.rest;

import com.pageprovider.dao.PageDao;
import com.pageprovider.dao.impl.PageDaoImpl;
import com.pageprovider.model.Page;
import com.pageprovider.services.PageProviderService;
import com.pageprovider.util.CachePageService;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    private PageProviderService pageProviderService;

    private final static Logger LOG = Logger.getLogger(PageProviderRest.class.getName());

    public PageProviderRest(){
        this.pageDao                = new PageDaoImpl();
        this.pageProviderService    = new PageProviderService(this.pageDao);
        try{
            List<Page> pageList = pageDao.findAll();
            for(Page page : pageList){
                CachePageService.put(page.getContentId(), page.getPaymentPageTypeId(), page);
            }


        }catch(Exception ex){
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }


    @GET
    @Path("{contentId}/{pageType}")
    public Response getpage(@PathParam("contentId") int content, @PathParam("pageType") int pageType) throws Exception{

        try{
            Page p =this.pageDao.find(content, pageType);
            if(p != null){
                return okResponse(p.getHtmlFile());
            }else{
                return notFound();
            }


        }catch(Exception ex){
            return internalServerError(ex);
        }
    }

    @PUT
    @Path("{contentId}/{pageType}")
    public Response updatepage(@PathParam("contentId") int content, @PathParam("pageType") int pageType) throws Exception{

        try{
            this.pageProviderService.refreshPage(content, pageType);
            LOG.log(Level.INFO, "REFRESHED {contentId: "+content+" , PageType: "+pageType+"}");
            return okResponse("OK");

        }catch(Exception ex){
            LOG.log(Level.SEVERE, ex.getMessage());
            return internalServerError(ex);
        }
    }



    private Response okResponse(Object entity){
        return Response.status(Response.Status.OK).entity(entity).build();
    }

    private Response internalServerError(Exception ex){
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error\n"+ex.getMessage()).build();
    }

    private Response notFound(){
        return Response.status(Response.Status.NOT_FOUND).entity("PAGE NOT FOUND").build();
    }
}
