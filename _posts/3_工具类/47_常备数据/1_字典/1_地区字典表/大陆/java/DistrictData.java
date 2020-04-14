package com.healerjean.proj.dto;

import lombok.Data;

import java.util.List;

/**
 * @author HealerJean
 * @ClassName DistrictData
 * @date 2020/4/1  9:51.
 * @Description
 */
@Data
public class DistrictData {

    /** provinceCode   */
    private String v;
    /** provinceName   */
    private String n;

    private List<CityDTO> c;

    @Data
    public static class CityDTO {
        /** cityCode   */
        private String v;
        /** cityName   */
        private String n;

        private List<DistrictDTO> c;

        @Data
        public static class DistrictDTO {
            /** districCode   */
            private String v;
            /** districName   */
            private String n;
        }
    }
}
