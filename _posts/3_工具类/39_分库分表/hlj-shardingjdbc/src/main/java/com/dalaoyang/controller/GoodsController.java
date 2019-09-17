package com.hlj.proj.controller;

import com.hlj.proj.entity.Goods;
import com.hlj.proj.repository.GoodsRepository;
import com.dangdang.ddframe.rdb.sharding.keygen.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangyang
 * @date 2019/1/29
 */
@RestController
public class GoodsController {


    @Autowired
    private GoodsRepository goodsRepository;

    @GetMapping("save")
    public String save(int i, int length) {
        for (; i <= length; i++) {
            Goods goods = new Goods();
            goods.setGoodsId((long) i);
            goods.setGoodsName("shangpin" + i);
            goods.setGoodsType((long) (i + 1));
            goodsRepository.save(goods);
        }
        return "success";
    }

    @GetMapping("select")
    public List select() {
        return goodsRepository.findAll();
    }

    @GetMapping("delete")
    public void delete() {
        goodsRepository.deleteAll();
    }

    @GetMapping("query1")
    public Object query1() {
        return goodsRepository.findAllByGoodsIdBetween(10L, 30L);
    }

    @GetMapping("query2")
    public Object query2() {
        List<Long> goodsIds = new ArrayList<>();
        goodsIds.add(10L);
        goodsIds.add(15L);
        goodsIds.add(20L);
        goodsIds.add(25L);
        return goodsRepository.findAllByGoodsIdIn(goodsIds);
    }
}
