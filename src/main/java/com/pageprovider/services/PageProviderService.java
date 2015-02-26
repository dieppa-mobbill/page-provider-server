package com.pageprovider.services;

import com.pageprovider.dao.PageDao;
import com.pageprovider.model.Page;
import com.pageprovider.util.CachePageService;

/**
 * Created by antoniop on 26/02/15.
 */
public class PageProviderService {

    private PageDao pageDao;


    public PageProviderService(PageDao pageDao){
        this.pageDao = pageDao;
    }


    public void refreshPage(int contentId, int pageType) throws Exception{
        CachePageService.delete(contentId, pageType);
        this.pageDao.find(contentId, pageType);
    }
}
