package cn.itcast.travel.service;

import cn.itcast.travel.domain.Category;

import java.util.List;

public interface CategoryService {

    //找到素有的分类
    public List<Category> findAll();
}
