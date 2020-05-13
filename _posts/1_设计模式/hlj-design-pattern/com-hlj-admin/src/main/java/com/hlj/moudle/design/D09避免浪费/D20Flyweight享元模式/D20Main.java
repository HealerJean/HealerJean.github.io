package com.hlj.moudle.design.D09避免浪费.D20Flyweight享元模式;

/**
 * @author HealerJean
 * @ClassName D20Main
 * @date 2019/8/21  10:27.
 * @Description
 */
public class D20Main {

    public static void main(String[] args) {
        //享元工厂
        FlyweightFactory factory = new FlyweightFactory();

        AbstractWebSite teachWebsite = factory.getWebSiteCategory("技术");
        teachWebsite.operate("SpringBoot");


        AbstractWebSite qgWebsite = factory.getWebSiteCategory("情感");
        qgWebsite.operate("爱情");


        System.out.println("网站分类总数为：" + factory.getWebSiteCount());
    }
}
