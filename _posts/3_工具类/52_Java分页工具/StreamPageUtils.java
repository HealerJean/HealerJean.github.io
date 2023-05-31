package com.hlj.util.z035_分页;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stream 流分页
 * @author zhangyujin
 * @date 2023/5/30  11:51.
 */
@Slf4j
public class StreamPageUtils {

    public static void main(String[] args) {
        DemoDTO demo1 = new DemoDTO()
                .setPrice(new BigDecimal("1"))
                .setSkuName("大疆飞机V1")
                .setOrderId("111")
                .setCreateTime(LocalDateTime.now().minusDays(1));
        DemoDTO demo2 = new DemoDTO()
                .setPrice(new BigDecimal("1"))
                .setSkuName("大疆飞机V2")
                .setOrderId("222")
                .setCreateTime(LocalDateTime.now().minusDays(1));

        DemoDTO demo3 = new DemoDTO()
                .setPrice(new BigDecimal("1"))
                .setSkuName("大疆飞机V3")
                .setOrderId("333")
                .setCreateTime(LocalDateTime.now().minusDays(1));

        DemoDTO demo4 = new DemoDTO()
                .setPrice(new BigDecimal("1"))
                .setSkuName("大疆飞机V4")
                .setOrderId("444")
                .setCreateTime(LocalDateTime.now().minusDays(1));

        DemoDTO demo5 = new DemoDTO()
                .setPrice(new BigDecimal("1"))
                .setSkuName("大疆飞机V5")
                .setOrderId("555")
                .setCreateTime(LocalDateTime.now().minusDays(1));

        List<DemoDTO> list = Lists.newArrayList(demo1,demo2,demo3,demo4,demo5);
        DemoQuery query = new DemoQuery().setOrderId("111");
        PageDto<DemoDTO> page = toPageDto(query, list, 1, 1);
        log.info("page:{}", page);

        query = new DemoQuery().setSkuName("大疆飞机");
        page = toPageDto(query, list, 2, 3);
        log.info("page:{}", page);
    }

    public static  PageDto<DemoDTO> toPageDto(DemoQuery query, List<DemoDTO> list, Integer pageNow, Integer pageSize) {
        if  (list == null || list.isEmpty()){
            return new PageDto<>(null, pageNow, pageSize, 0, 0);
        }

        Stream<DemoDTO> stream = list.stream();
        if (Objects.nonNull(query.getStartTime()) ) {
            stream = stream.filter(item->query.getStartTime().compareTo(item.getCreateTime()) <= 0);
        }
        if (Objects.nonNull(query.getEndTime()) ) {
            stream = stream.filter(item->query.getEndTime().compareTo(item.getCreateTime()) >= 0);
        }
        if (StringUtils.isNotBlank(query.getOrderId()) ) {
            stream = stream.filter(item->StringUtils.equals(query.getOrderId(), item.getOrderId()));
        }
        if (StringUtils.isNotBlank(query.getSkuName()) ) {
            stream = stream.filter(item-> item.getSkuName().contains(query.getSkuName()));
        }

        List<DemoDTO> matchList = stream
                .sorted(Comparator.comparing(DemoDTO::getCreateTime).reversed())
                .collect(Collectors.toList());

        Integer totalCount = matchList.size();
        Integer pageCount;
        if (totalCount % pageSize == 0) {
            pageCount = totalCount / pageSize;
        } else {
            pageCount = totalCount / pageSize + 1;
        }
        List<DemoDTO> pageList = matchList.stream()
                .skip((long) (pageNow - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
        return new PageDto<>(pageList, pageNow, pageSize, totalCount, pageCount);
    }
    

    /**
     * @author zhangyujin
     * @date 2023/5/30  13:06.
     */
    @ToString
    @Accessors(chain = true)
    @Data
    public static class DemoDTO {

        private String orderId;

        private String skuName;

        private BigDecimal price;

        private LocalDateTime createTime;

    }


    /**
     * @author zhangyujin
     * @date 2023/5/30  13:06.
     */
    @ToString
    @Accessors(chain = true)
    @Data
    public static class DemoQuery {

        private String orderId;

        private String skuName;

        private BigDecimal price;

        private LocalDateTime startTime;

        private LocalDateTime endTime;

    }

    @ToString
    @Accessors(chain = true)
    public static class PageDto<T> {
        private List<T> datas;
        /**
         * 当前页码数
         */
        private Integer pageNow;
        /**
         * 每页显示的记录数
         */
        private Integer pageSize;
        /**
         * 总记录数
         */
        private Integer totalCount;
        /**
         * 一共多少页
         */
        private Integer pageCount;

        public PageDto(List<T> datas, Integer pageNow, Integer pageSize, Integer totalCount, Integer pageCount) {
            this.datas = datas;
            this.pageNow = pageNow;
            this.pageSize = pageSize;
            this.totalCount = totalCount;
            this.pageCount = pageCount;
        }

        private PageDto(List<T> datas) {
            this.datas = datas;
        }

        private PageDto() {
        }
    }

}

