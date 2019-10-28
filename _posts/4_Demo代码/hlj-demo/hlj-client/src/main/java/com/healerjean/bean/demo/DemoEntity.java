package com.healerjean.bean.demo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@ApiModel(value = "demo实体类")
public class DemoEntity {

	private String name;

}
