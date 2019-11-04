package hlj.wordtopdf;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author HealerJean
 * @ClassName ContractTeamplateBillDTO
 * @date 2019/11/4  13:43.
 * @Description 合同模板票据DTO
 */
@Data
@Accessors(chain = true)
public class ContractTeamplateBillDTO {

    /**
     * 序号
     */
    private Integer index;

    /**
     * 买方企业名称
     */
    private String buyerCompanyName;

    /**
     * 基础交易合同号（基础交易合同及编号）
     */
    private String basicTransactionContractNo;

    /**
     * 票据种类（应收账款种类）
     */
    private String billType ;

    /**
     * 有效金额（应收账款金额）
     */
    private String validAmount;

    /**
     * 订单到期日 （应收账款到期日）
     */
    private String billEndTime;


    /** 凭证编号 (发票号)*/
    private String creditNo;


    /** 账目金额(发票金额) */
    private String billAmount;

    /**
     * 订单起始日( 发票开具日)
     */
    private String billStartTime;
}
