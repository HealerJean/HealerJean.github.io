package com.healerjean.proj.pojo;

import java.util.List;

/**
 * @author HealerJean
 * @ClassName DistrictDO
 * @date 2020/4/1  9:51.
 * @Description
 */
public class DistrictDO {

    /** provinceCode   */
    private String v;
    /** provinceName   */
    private String n;

    private List<CityData> c;

    public static class CityData {
        /** cityCode   */
        private String v;
        /** cityName   */
        private String n;

        private List<DistrictData> c;

        public static class DistrictData {
            /** districCode   */
            private String v;
            /** districName   */
            private String n;
        }
    }
}
