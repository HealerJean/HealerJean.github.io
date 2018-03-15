package com.hlj.bean;

import javax.persistence.*;

@Entity
@Table(name="log4jBean")
public class Log4jBean {

    //主键id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //主题
    @Column(nullable = false,length = 100)
    private  String name;

    public Log4jBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
