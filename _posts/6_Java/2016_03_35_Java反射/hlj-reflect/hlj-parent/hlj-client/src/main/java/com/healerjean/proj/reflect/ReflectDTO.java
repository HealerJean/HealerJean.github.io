package com.healerjean.proj.reflect;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author HealerJean
 * @ClassName ReflectDTO
 * @date 2019/11/12  11:15.
 * @Description
 */
@Data
public class ReflectDTO extends FatherDTO {

    private Long privateId;

    private String privateName;

    private Integer privateAge;

    private BigDecimal privateMoney;

    private Date privateDate;


    public Long publicid;

    public String publicName;

    public Integer publicAge;

    public BigDecimal publicMoney;

    public Date publicDate;

    public List<String> list ;

}
