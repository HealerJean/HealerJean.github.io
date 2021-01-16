package com.hlj.arith.demo0001_杀人算法;

import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
作者：HealerJean
题目：杀人算法
  把犯人围城一圈，每次杀掉第七个，又从第八个开始杀掉第七个，直到剩下最后一个
解题思路：
  1、构造囚犯Prisoner对象，设置他们的编号
  2、使用 list.subList()进行截取杀人
 */
public class 杀人算法 {

    @Test
    public void start() {
        //1、初始化所有的囚犯,并打印
        List<Prisoner> prisoners = initPrisonerList(20);
        printPrisonerList(prisoners);

        killPrisoner(prisoners);
    }

    /**
     * 杀人
     */
    private void killPrisoner(List<Prisoner> prisoners) {
        List<Prisoner> beforeKillPrisoner = prisoners.subList(0, 6);
        List<Prisoner> afterKillPrisoner = prisoners.subList(7, prisoners.size());
        afterKillPrisoner.addAll(beforeKillPrisoner);

        printPrisonerList(afterKillPrisoner);
        if (afterKillPrisoner.size() > 6 ){
            killPrisoner(afterKillPrisoner);
        }

    }

    /**
     * 打印囚犯集合
     */
    public void printPrisonerList(List<Prisoner> prisoners) {
        prisoners.stream().forEach(p -> {
            System.out.print(p.getNumber() + ",");
        });
        System.out.println();
    }

    /**
     * 初始化person集合数据
     */
    public List<Prisoner> initPrisonerList(Integer n) {
        List persons = new ArrayList<>();
        for (Integer i = 1; i <= n; i++) {
            Prisoner prisoner = new Prisoner();
            prisoner.setNumber(i);
            persons.add(prisoner);
        }
        return persons;
    }

    @Data
    @Accessors(chain = true)
    public class Prisoner {
        private Integer number;
    }
}

