package com.healerjean.proj.controller;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.common.data.bo.BaseRes;
import com.healerjean.proj.utils.ThreadPoolUtils;
import com.healerjean.proj.utils.filter.FilterEnum;
import com.healerjean.proj.utils.filter.bloom.BloomFilterUtils;
import com.healerjean.proj.utils.filter.compressed.CompressedBitInfo;
import com.healerjean.proj.utils.filter.compressed.CompressedBitUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * CompressedController
 *
 * @author zhangyujin
 * @date 2025/1/7
 */
@RestController
@RequestMapping("hlj/filter")
@Api(tags = "FilterController")
@Slf4j
public class CompressedController {


    @ApiOperation("compressed/getBitInfo")
    @LogIndex
    @PostMapping("compressed/getBitInfo")
    @ResponseBody
    public BaseRes<CompressedBitInfo> getBitInfo(long sourceOffset) {
        CompressedBitInfo bitInfo = CompressedBitUtils.getBitInfo(FilterEnum.CompressedEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(bitInfo);
    }


    @ApiOperation("compressed/setCompressedBit")
    @LogIndex
    @PostMapping("compressed/setCompressedBit")
    @ResponseBody
    public BaseRes<CompressedBitInfo> setCompressedBit(long sourceOffset) {
        CompressedBitInfo result = CompressedBitUtils.setCompressedBit(FilterEnum.CompressedEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(result);
    }


    @ApiOperation("compressed/setBatchCompressedBit")
    @LogIndex
    @PostMapping("compressed/setBatchCompressedBit")
    @ResponseBody
    public BaseRes<List<CompressedBitInfo>> setBatchCompressedBit(long sourceOffset) {
        List<CompressedBitInfo> result = Lists.newArrayList();
        for (long i = 0; i < sourceOffset; i++) {
            CompressedBitInfo compressedBitInfo = CompressedBitUtils.setCompressedBit(FilterEnum.CompressedEnum.DEFAULT, i);
            result.add(compressedBitInfo);
        }
        return BaseRes.buildSuccess(result);
    }


    @ApiOperation("compressed/remCompressedBit")
    @LogIndex
    @PostMapping("compressed/remCompressedBit")
    @ResponseBody
    public BaseRes<CompressedBitInfo> remCompressedBit(long sourceOffset) {
        CompressedBitInfo result = CompressedBitUtils.remCompressedBit(FilterEnum.CompressedEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(result);
    }

    @ApiOperation("compressed/getCompressedBit")
    @LogIndex
    @PostMapping("compressed/getCompressedBit")
    @ResponseBody
    public BaseRes<CompressedBitInfo> getCompressedBit(long sourceOffset) {
        CompressedBitInfo result = CompressedBitUtils.getCompressedBit(FilterEnum.CompressedEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(result);
    }


    @ApiOperation("compressed/getAllBucketKeys")
    @LogIndex
    @PostMapping("compressed/getAllBucketKeys")
    @ResponseBody
    public BaseRes<List<String>> getAllBucketKeys(long sourceOffset) {
        List<String> result = CompressedBitUtils.getAllBucketKeys(FilterEnum.CompressedEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(result);
    }


    @ApiOperation("compressed/deleteAllCompressedBit")
    @LogIndex
    @PostMapping("compressed/deleteAllCompressedBit")
    @ResponseBody
    public BaseRes<Boolean> deleteAllCompressedBit(long sourceOffset) {
        CompressedBitUtils.deleteAllCompressedBit(FilterEnum.CompressedEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(true);
    }


    @ApiOperation("bloom/setBloomBit")
    @LogIndex
    @PostMapping("bloom/setBloomBit")
    @ResponseBody
    public BaseRes<Boolean> setBloomBit(String bitValue) {
        return BaseRes.buildSuccess( BloomFilterUtils.setBloomBit(FilterEnum.BloomEnum.DEFAULT,  bitValue));
    }

    @ApiOperation("bloom/getBloomBit")
    @LogIndex
    @PostMapping("bloom/getBloomBit")
    @ResponseBody
    public BaseRes<Boolean> getBloomBit(String bitValue) {
        return BaseRes.buildSuccess( BloomFilterUtils.getBloomBit(FilterEnum.BloomEnum.DEFAULT,  bitValue));
    }


    @ApiOperation("bloom/pressureSetBloomBit")
    @LogIndex
    @PostMapping("bloom/pressureSetBloomBit")
    @ResponseBody
    public BaseRes<BigDecimal> pressureSetBloomBit() {
        StopWatch stopWatch = new StopWatch();
        // 1、数据拆入
        List<Future> futureList = Lists.newArrayList();
        stopWatch.start("setBloomBit");
        ThreadPoolExecutor defaultThreadPoolExecutor = ThreadPoolUtils.DEFAULT_THREAD_POOL_EXECUTOR;
        for (long i = 0; i < 4000000; i++) {
            Future<?> submit = defaultThreadPoolExecutor.submit(() -> {
                String bitValue = String.valueOf(RandomUtil.randomLong(304311552319L, 1000000000000L)) ;
                BloomFilterUtils.setBloomBit(FilterEnum.BloomEnum.DEFAULT, bitValue);
                // log.info("[pressureSetBloomBit] bitValue:{}, bloomBit:{}", bitValue, flag);
                return bitValue;
            });
            futureList.add(submit);
        }

        List<String> sumBitValues = Lists.newArrayList();
        futureList.forEach(item->{
            try {
                sumBitValues.add(item.get().toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        futureList.clear();


        // 2、成功率统计
        stopWatch.stop();
        stopWatch.start("getBloomBit");
        sumBitValues.forEach(bitValue->{
            Future<Boolean> submit = defaultThreadPoolExecutor.submit(() -> BloomFilterUtils.getBloomBit(FilterEnum.BloomEnum.DEFAULT, String.valueOf(bitValue)));
            futureList.add(submit);
        });
        AtomicLong atomicLong = new AtomicLong();
        futureList.forEach(item->{
            try {
                Boolean flag = (Boolean) item.get();
                if (flag){
                    atomicLong.incrementAndGet();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        stopWatch.stop();
        log.info("stopWatch.prettyPrint():{}", stopWatch.prettyPrint());

        BigDecimal successRate = new BigDecimal(atomicLong.longValue()).divide(new BigDecimal(sumBitValues.size()), 2, RoundingMode.HALF_UP);
        log.info("成功率:{}", successRate);
        return BaseRes.buildSuccess(successRate);
    }


    @Test
    public void test() {
        System.out.println(RandomUtil.randomLong(304311552319L, 1000000000000L));
        System.out.println(RandomUtil.randomLong(304311552319L, 1000000000000L));
        System.out.println(RandomUtil.randomLong(304311552319L, 1000000000000L));
        System.out.println(RandomUtil.randomLong(304311552319L, 1000000000000L));
    }


}
