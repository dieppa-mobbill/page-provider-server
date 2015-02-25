package com.pageprovider.dao;

import com.pageprovider.model.Page;

import java.util.List;


public interface PageDao {

    Page find(int contentId, int pageType) throws Exception;

    List<Page> findAll() throws Exception;
}
