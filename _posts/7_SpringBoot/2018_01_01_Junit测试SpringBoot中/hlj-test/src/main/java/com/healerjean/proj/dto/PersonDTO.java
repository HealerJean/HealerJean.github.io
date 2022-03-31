package com.healerjean.proj.dto;

/**
 * @author zhangyujin
 * @date 2022/3/30  18:42.
 * @description
 */
public class PersonDTO {

    /**
     * 获取该类型性别并进行打印
     *
     * @param type 0代表女生、1代表男生、其他数字提示参数有误
     */
    public void getSex(int type) {
        if (type == 0) {
            printing("女");
        } else if (type == 1) {
            printing("男");
        } else {
            printing("参数有误");
        }
    }

    /**
     * 打印方法
     */
    public void printing(String content) {
        System.out.print(content);
    }

    /**
     * 判断类型是否是男人
     */
    public boolean isMan(int type) {
        System.out.println("type:"+ type);
        return type == 1;
    }
}
