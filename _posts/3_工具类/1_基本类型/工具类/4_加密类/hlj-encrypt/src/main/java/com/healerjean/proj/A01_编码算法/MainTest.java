package com.healerjean.proj.A01_编码算法;

import com.healerjean.proj.A01_编码算法.Base64.Base64Utils;
import com.healerjean.proj.A01_编码算法.Base64.UnicodeUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;

/**
 * @author HealerJean
 * @date 2020/12/11  11:21.
 * @description
 */
public class MainTest {

    @Test
    public void base64Test() throws IOException {
        File file = new File("C:\\Users\\HealerJean\\Pictures\\aa.png");
        String base64Image = Base64Utils.encode(IOUtils.toByteArray(new FileInputStream(file)));
        System.out.println(base64Image);
        byte[] bytes = Base64Utils.decode(base64Image);
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\HealerJean\\Pictures\\bb.png");
        fileOutputStream.write(bytes);
        fileOutputStream.flush();
        fileOutputStream.close();
    }


    @Test
    public void unicodeTest() {
        String cn = "全部中文";
        System.out.println(UnicodeUtil.cnToUnicode(cn));
        String unicode = "\\u5168\\u90e8\\u4e2d\\u6587";
        System.out.println(UnicodeUtil.unicodeToCn(unicode));

        String someUnicode = "{\"msg\":\"\\u64cd\\u4f5c\\u6210\\u529f\",\"error\":0,\"data\":[{\"adzone_id\":\"1782066627\",\"adzone_name\":\"\\u85cf\\u5b9d\\u9601\",\"alipay_total_price\":\"18.0000\",\"auction_category\":\"\\u7f8e\\u5986-\\u7f8e\\u5bb9\\u62a4\\u80a4\",\"commission\":\"0.00\",\"commission_rate\":\"1.0000\",\"create_time\":\"2018-09-21 19:31:47\",\"income_rate\":\"0.3100\",\"item_num\":1,\"item_title\":\"\\u7f8e\\u533b\\u751f\\u9664\\u87a8\\u8ff7\\u8fed\\u9999\\u6d17\\u9762\\u5976\\u5973\\u7537\\u5b66\\u751f\\u63a7\\u6cb9\\u75d8\\u75d8\\u6e05\\u6d01\\u53bb\\u87a8\\u9ed1\\u5934\\u7c89\\u523a\\u6d01\\u9762\\u4e73\",\"num_iid\":532235400390,\"order_type\":\"\\u6dd8\\u5b9d\",\"pay_price\":\"0.00\",\"price\":\"69.00\",\"pub_share_pre_fee\":\"5.58\",\"seller_nick\":\"\\u5e7f\\u5dde\\u7f8e\\u533b\\u751f\\u751f\\u7269\\u79d1\\u6280\",\"seller_shop_title\":\"\\u7f8e\\u533b\\u751f\\u5b98\\u65b9\\u4f01\\u4e1a\\u5e97\",\"site_id\":\"46766627\",\"site_name\":\"\\u5c0f\\u5f53\\u4f18\\u60e0\\u5238\",\"subsidy_fee\":\"0\",\"subsidy_rate\":\"0.0000\",\"subsidy_type\":\"-1\",\"terminal_type\":\"2\",\"tk3rd_type\":\"--\",\"tk_status\":12,\"total_commission_fee\":\"0\",\"total_commission_rate\":\"0.3100\",\"trade_id\":224003948316112915,\"trade_parent_id\":224003948316112915}]}";
        System.out.println("一部分英文，一部分 unicode编码" + UnicodeUtil.decode(someUnicode));

        String normal = "{\"msg\":\"操作成功\",\"error\":0,\"data\":[{\"adzone_id\":\"1782066627\",\"adzone_name\":\"藏宝阁\",\"alipay_total_price\":\"18.0000\",\"auction_category\":\"美妆-美容护肤\",\"commission\":\"0.00\",\"commission_rate\":\"1.0000\",\"create_time\":\"2018-09-21 19:31:47\",\"income_rate\":\"0.3100\",\"item_num\":1,\"item_title\":\"美医生除螨迷迭香洗面奶女男学生控油痘痘清洁去螨黑头粉刺洁面乳\",\"num_iid\":532235400390,\"order_type\":\"淘宝\",\"pay_price\":\"0.00\",\"price\":\"69.00\",\"pub_share_pre_fee\":\"5.58\",\"seller_nick\":\"广州美医生生物科技\",\"seller_shop_title\":\"美医生官方企业店\",\"site_id\":\"46766627\",\"site_name\":\"小当优惠券\",\"subsidy_fee\":\"0\",\"subsidy_rate\":\"0.0000\",\"subsidy_type\":\"-1\",\"terminal_type\":\"2\",\"tk3rd_type\":\"--\",\"tk_status\":12,\"total_commission_fee\":\"0\",\"total_commission_rate\":\"0.3100\",\"trade_id\":224003948316112915,\"trade_parent_id\":224003948316112915}]}";
        System.out.println("一部分英文，一部分 中文编码" + UnicodeUtil.decode(normal));
    }

}
