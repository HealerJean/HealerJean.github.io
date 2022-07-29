package com.healerjean.proj.a_test.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.healerjean.proj.util.json.JsonUtils;
import org.junit.Test;

import java.util.List;

/**
 * @author zhangyujin
 * @date 2022/6/8  16:20.
 */
public class TestMain {


    // @Test
    // public void test(){
    //     //测试
    //     // String json = "[{\"id\":\"1016151\",\"name\":\"京东安联财产保险有限公司北京分公司\",\"phone\":\"4008002020\"},{\"id\":\"1030033\",\"name\":\"中国太平洋财险保险股份有限公司深圳分公司\",\"phone\":\"0755-22695215\"},{\"id\":\"3115659\",\"name\":\"华农财产保险股份有限公司\",\"phone\":\"4000100000\"},{\"id\":\"11252859\",\"name\":\"太平财产保险有限公司北京分公司\",\"phone\":\"010-83253227\"},{\"id\":\"11284905\",\"name\":\"中国平安财产保险股份有限公司深圳分公司\",\"phone\":\"95511\"},{\"id\":\"134\",\"name\":\"中国人寿财产保险股份有限公司北京市分公司\",\"phone\":\"010-65602366-8016\"},{\"id\":\"11221724\",\"name\":\"紫金财产保险股份有限公司浙江分公司\",\"phone\":\"95312\"},{\"id\":\"139\",\"name\":\"中国大地财产保险股份有限公司北京分公司\",\"phone\":\"010-82277957\"},{\"id\":\"146\",\"name\":\"中国人民财产保险股份有限公司\",\"phone\":\"95518\"},{\"id\":\"147\",\"name\":\"泰康在线财产保险股份有限公司\",\"phone\":\"95522-3\"}]";
    //
    //     //生产
    //     String json = "[{\"id\":\"1016151\",\"name\":\"京东安联财产保险有限公司北京分公司\",\"phone\":\"4008002020\"},{\"id\":\"1030033\",\"name\":\"中国太平洋财险保险股份有限公司深圳分公司\",\"phone\":\"0755-22695215\"},{\"id\":\"3115659\",\"name\":\"华农财产保险股份有限公司\",\"phone\":\"4000100000\"},{\"id\":\"3191757\",\"name\":\"太平财产保险有限公司北京分公司\",\"phone\":\"010-83253227\"},{\"id\":\"11211352\",\"name\":\"中国平安财产保险股份有限公司深圳分公司\",\"phone\":\"95511\"},{\"id\":\"134\",\"name\":\"中国人寿财产保险股份有限公司北京市分公司\",\"phone\":\"010-65602366-8016\"},{\"id\":\"11216195\",\"name\":\"紫金财产保险股份有限公司浙江分公司\",\"phone\":\"95312\"},{\"id\":\"139\",\"name\":\"中国大地财产保险股份有限公司北京分公司\",\"phone\":\"010-82277957\"},{\"id\":\"42826\",\"name\":\"中国人民财产保险股份有限公司\",\"phone\":\"95518\"},{\"id\":\"1002841\",\"name\":\"泰康在线财产保险股份有限公司\",\"phone\":\"95522-3\"}]";
    //     List<SupplierDto> supplierDtos = JsonUtils.toArrayList(json, SupplierDto.class);
    //     Map<String, SupplierDto> map = new HashMap();
    //     supplierDtos.stream().forEach(item->{
    //         // System.out.println("      - "+item.getId());
    //         System.out.println("    "+item.getId()+":");
    //         System.out.println("      supplierId: " +item.getId());
    //         System.out.println("      supplierName: " +item.getName());
    //         System.out.println("      supplierPhone: "+item.getPhone());
    //     });
    //     System.out.println(JsonUtils.toJsonString(map));
    // }


    @Test
    public void test(){
        String json = "[\n" +
                "  \"flashbasicfee\",\n" +
                "  \"flashclaim\",\n" +
                "  \"flashclaimdayinfo\",\n" +
                "  \"flashexceptsku\",\n" +
                "  \"flashfeefactorplan\",\n" +
                "  \"flashfeefactorrate\",\n" +
                "  \"flashpolicy\",\n" +
                "  \"flashpolicydayinfo\",\n" +
                "  \"flashstatusplan\",\n" +
                "  \"flashsupplierplan\",\n" +
                "  \"flashvender\",\n" +
                "  \"globalcategory\",\n" +
                "  \"globalexchangerate\",\n" +
                "  \"globalorder\",\n" +
                "  \"globalsupplier\",\n" +
                "  \"globalsuppliercategoryrate\",\n" +
                "  \"globalvender\",\n" +
                "  \"globalvendercategory\",\n" +
                "  \"qrxactivity\",\n" +
                "  \"qrxactivityvenders\",\n" +
                "  \"qrxagent\",\n" +
                "  \"qrxbatch\",\n" +
                "  \"qrxbatchitem\",\n" +
                "  \"qrxbatchwaybill\",\n" +
                "  \"qrxblacklistplan\",\n" +
                "  \"qrxcategory\",\n" +
                "  \"qrxclaim\",\n" +
                "  \"qrxclaim2013\",\n" +
                "  \"qrxclaim2014\",\n" +
                "  \"qrxclaim2015\",\n" +
                "  \"qrxclaim2016\",\n" +
                "  \"qrxclaim2017\",\n" +
                "  \"qrxclaim2018\",\n" +
                "  \"qrxclaimcheck\",\n" +
                "  \"qrxclaimfee\",\n" +
                "  \"qrxclaimrateplan\",\n" +
                "  \"qrxdayinfo\",\n" +
                "  \"qrxdayinfo2013\",\n" +
                "  \"qrxdayinfo2014\",\n" +
                "  \"qrxdayinfo2015\",\n" +
                "  \"qrxdayinfo2016\",\n" +
                "  \"qrxdayinfo2017\",\n" +
                "  \"qrxdayinfo2018\",\n" +
                "  \"qrxinsurance\",\n" +
                "  \"qrxinsurancecategory\",\n" +
                "  \"qrxinsurancepackage\",\n" +
                "  \"qrxinsurancerate\",\n" +
                "  \"qrxjsxclaim\",\n" +
                "  \"qrxmonthinfo\",\n" +
                "  \"qrxnotice\",\n" +
                "  \"qrxnoticevenders\",\n" +
                "  \"qrxparams\",\n" +
                "  \"qrxpercentplan\",\n" +
                "  \"qrxphoneblacklist\",\n" +
                "  \"qrxpinblacklist\",\n" +
                "  \"qrxpolicyadjustrate\",\n" +
                "  \"qrxpolicydata\",\n" +
                "  \"qrxpolicyfee\",\n" +
                "  \"qrxpolicyrisk\",\n" +
                "  \"qrxquitinfo\",\n" +
                "  \"qrxsettlement\",\n" +
                "  \"qrxstatusplan\",\n" +
                "  \"qrxsuminfo\",\n" +
                "  \"qrxsupplier\",\n" +
                "  \"qrxsupplierinsurance\",\n" +
                "  \"qrxsupplierplan\",\n" +
                "  \"qrxtask\",\n" +
                "  \"qrxtask2016\",\n" +
                "  \"qrxtaskserviceconfig\",\n" +
                "  \"qrxtasktmp\",\n" +
                "  \"qrxthread\",\n" +
                "  \"qrxtimertaskconfig\",\n" +
                "  \"qrxvender\",\n" +
                "  \"qrxvendercategory\",\n" +
                "  \"qrxvendermerchant\",\n" +
                "  \"qrxvenderpackage\",\n" +
                "  \"qrxvenderprovince\",\n" +
                "  \"qrxinsuranceinfo\",\n" +
                "  \"qrx_insurance_black_list\",\n" +
                "  \"qrxpolicydata2018\",\n" +
                "  \"qrxpolicydata2019\",\n" +
                "  \"qrxinsurancecatefee\",\n" +
                "  \"qrx_insurance_claim_config\",\n" +
                "  \"qrx_file_supplier_config\",\n" +
                "  \"qrx_sign_config\",\n" +
                "  \"qrx_category_black_list\",\n" +
                "  \"temp_yfx_calculate\",\n" +
                "  \"qrx_insurance_sku_fee\",\n" +
                "  \"qrx_insurance_desc\",\n" +
                "  \"qrxpolicyrisk1\",\n" +
                "  \"qrx_not_accept_insurance\",\n" +
                "  \"sync_config\",\n" +
                "  \"sku_cate\",\n" +
                "  \"sjx_insurance_policy_config\",\n" +
                "  \"sjx_insurance_claim_config\",\n" +
                "  \"sjx_channel\",\n" +
                "  \"sync_configs\",\n" +
                "  \"qrxpolicydata2020\",\n" +
                "  \"vender_page_info_config\",\n" +
                "  \"vender_page_info_config_group\",\n" +
                "  \"batch_execute_task\",\n" +
                "  \"vender_history_day\",\n" +
                "  \"vender_supply_route\",\n" +
                "  \"supply_quota\",\n" +
                "  \"apply_insure\",\n" +
                "  \"loan_contract\",\n" +
                "  \"merchant_black_list_plan\",\n" +
                "  \"merchant_global_vender\",\n" +
                "  \"merchant_percent_plan\",\n" +
                "  \"merchant_policy_config\",\n" +
                "  \"merchant_status_plan\",\n" +
                "  \"merchant_supplier_plan\",\n" +
                "  \"merchant_vender\",\n" +
                "  \"merchant_vender_category\",\n" +
                "  \"qrxvenderbak1\",\n" +
                "  \"qrxstatusplanbak1\",\n" +
                "  \"merchant_order_risk_tag\",\n" +
                "  \"merchant_activity_medical\",\n" +
                "  \"merchant_global_dept_sku\",\n" +
                "  \"vender_supply_route_check\",\n" +
                "  \"supply_quota_check\",\n" +
                "  \"merchant_discount\",\n" +
                "  \"vat_invoice_record\",\n" +
                "  \"route_policy_avg\",\n" +
                "  \"route_vender_supplier\",\n" +
                "  \"route_vender_supplier_log\",\n" +
                "  \"route_supplier_log\",\n" +
                "  \"route_insurance_default_supplier\",\n" +
                "  \"route_rule\",\n" +
                "  \"route_rule_supplier\",\n" +
                "  \"merchant_vender_status\",\n" +
                "  \"merchant_pricing_strategy\",\n" +
                "  \"merchant_pricing_result\",\n" +
                "  \"sign_verify_rule\",\n" +
                "  \"sign_verify_rule_detail\",\n" +
                "  \"sign_operate_log\",\n" +
                "  \"merchant_pricing_task\",\n" +
                "  \"merchant_wallet_alteration\",\n" +
                "  \"merchant_refund_policy_info\",\n" +
                "  \"merchant_refund_policy_detail\",\n" +
                "  \"merchant_oa_approval_config\",\n" +
                "  \"merchant_oa_approval_node_config\",\n" +
                "  \"merchant_oa_approval_flow\",\n" +
                "  \"vat_download_record\",\n" +
                "  \"merchant_route_policy\",\n" +
                "  \"merchant_route_relation\",\n" +
                "  \"merchant_route\",\n" +
                "  \"merchant_route_gpo\",\n" +
                "  \"merchant_route_sub\",\n" +
                "  \"merchant_fee_policy\",\n" +
                "  \"merchant_vender_risk\",\n" +
                "  \"merchant_vendor_change_record\",\n" +
                "  \"merchant_cate_fee\",\n" +
                "  \"merchant_promise_rate_fee\",\n" +
                "  \"merchant_promise_rate\",\n" +
                "  \"sku_category_fee_extend\",\n" +
                "  \"merchant_vender_activity\",\n" +
                "  \"merchant_refund_policy_task\",\n" +
                "  \"merchant_vender_claim_rate\",\n" +
                "  \"merchant_vender_risk_claim_rate\",\n" +
                "  \"merchant_vender_level\",\n" +
                "  \"merchant_risk_strategy\",\n" +
                "  \"merchant_risk_claim_rate\",\n" +
                "  \"merchant_policy_price_strategy\",\n" +
                "  \"merchant_level_claim_rate\",\n" +
                "  \"merchant_switch_strategy\",\n" +
                "  \"merchant_switch_vender\"\n" +
                "]";
        JsonNode rootNode = JsonUtils.toJsonNode(json);
        List<String> list = Lists.newArrayList("qrxpolicy",
                "qrxpolicycash",
                "qrxpolicydaysales",
                "merchant_order_message",
                "merchant_policy",
                "qrxpolicyattach");
        for (JsonNode jsonNode : rootNode) {
            String table = jsonNode.asText();
            if (list.contains(table)){
                continue;
            }
            System.out.printf(table + ":" + table +";");
        }

    }
}
