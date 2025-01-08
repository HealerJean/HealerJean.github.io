package com.healerjean.proj.controller;

import com.google.common.collect.Lists;
import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.common.data.bo.BaseRes;
import com.healerjean.proj.utils.filter.bloom.BloomFilterUtils;
import com.healerjean.proj.utils.filter.compressed.CompressedBitInfo;
import com.healerjean.proj.utils.filter.compressed.CompressedBitUtils;
import com.healerjean.proj.utils.filter.compressed.CompressedEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        CompressedBitInfo bitInfo = CompressedBitUtils.getBitInfo(CompressedEnum.CompressedInfoEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(bitInfo);
    }


    @ApiOperation("compressed/setCompressedBit")
    @LogIndex
    @PostMapping("compressed/setCompressedBit")
    @ResponseBody
    public BaseRes<CompressedBitInfo> setCompressedBit(long sourceOffset) {
        CompressedBitInfo result = CompressedBitUtils.setCompressedBit(CompressedEnum.CompressedInfoEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(result);
    }


    @ApiOperation("compressed/setBatchCompressedBit")
    @LogIndex
    @PostMapping("compressed/setBatchCompressedBit")
    @ResponseBody
    public BaseRes<List<CompressedBitInfo>> setBatchCompressedBit(long sourceOffset) {
        List<CompressedBitInfo> result = Lists.newArrayList();
        for (long i = 0; i < sourceOffset; i++) {
            CompressedBitInfo compressedBitInfo = CompressedBitUtils.setCompressedBit(CompressedEnum.CompressedInfoEnum.DEFAULT, i);
            result.add(compressedBitInfo);
        }
        return BaseRes.buildSuccess(result);
    }


    @ApiOperation("compressed/remCompressedBit")
    @LogIndex
    @PostMapping("compressed/remCompressedBit")
    @ResponseBody
    public BaseRes<CompressedBitInfo> remCompressedBit(long sourceOffset) {
        CompressedBitInfo result = CompressedBitUtils.remCompressedBit(CompressedEnum.CompressedInfoEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(result);
    }

    @ApiOperation("compressed/getCompressedBit")
    @LogIndex
    @PostMapping("compressed/getCompressedBit")
    @ResponseBody
    public BaseRes<CompressedBitInfo> getCompressedBit(long sourceOffset) {
        CompressedBitInfo result = CompressedBitUtils.getCompressedBit(CompressedEnum.CompressedInfoEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(result);
    }


    @ApiOperation("compressed/getAllBucketKeys")
    @LogIndex
    @PostMapping("compressed/getAllBucketKeys")
    @ResponseBody
    public BaseRes<List<String>> getAllBucketKeys(long sourceOffset) {
        List<String> result = CompressedBitUtils.getAllBucketKeys(CompressedEnum.CompressedInfoEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(result);
    }


    @ApiOperation("compressed/deleteAllCompressedBit")
    @LogIndex
    @PostMapping("compressed/deleteAllCompressedBit")
    @ResponseBody
    public BaseRes<Boolean> deleteAllCompressedBit(long sourceOffset) {
        CompressedBitUtils.deleteAllCompressedBit(CompressedEnum.CompressedInfoEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(true);
    }


    @ApiOperation("bloom/bloomFilterCreate")
    @LogIndex
    @PostMapping("bloom/bloomFilterCreate")
    @ResponseBody
    public BaseRes<RBloomFilter<?>> bloomFilterCreate(long sourceOffset) {
        return BaseRes.buildSuccess( BloomFilterUtils.create("bloomInitKey", 2000, 0.01));
    }


}
