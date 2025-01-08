package com.healerjean.proj.controller;

import com.google.common.collect.Lists;
import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.common.data.bo.BaseRes;
import com.healerjean.proj.utils.filter.compressed.CompressedBitInfo;
import com.healerjean.proj.utils.filter.compressed.CompressedBitUtils;
import com.healerjean.proj.utils.filter.compressed.CompressedEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("hlj/compressed")
@Api(tags = "CompressedController")
@Slf4j
public class CompressedController {


    @ApiOperation("getBitInfo")
    @LogIndex
    @PostMapping("getBitInfo")
    @ResponseBody
    public BaseRes<CompressedBitInfo> getBitInfo(long sourceOffset) {
        CompressedBitInfo bitInfo = CompressedBitUtils.getBitInfo(CompressedEnum.CompressedInfoEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(bitInfo);
    }


    @ApiOperation("setCompressedBit")
    @LogIndex
    @PostMapping("setCompressedBit")
    @ResponseBody
    public BaseRes<CompressedBitInfo> setCompressedBit(long sourceOffset) {
        CompressedBitInfo result = CompressedBitUtils.setCompressedBit(CompressedEnum.CompressedInfoEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(result);
    }


    @ApiOperation("setBatchCompressedBit")
    @LogIndex
    @PostMapping("setBatchCompressedBit")
    @ResponseBody
    public BaseRes<List<CompressedBitInfo>> setBatchCompressedBit(long sourceOffset) {
        List<CompressedBitInfo> result = Lists.newArrayList();
        for (long i = 0; i < sourceOffset; i++) {
            CompressedBitInfo compressedBitInfo = CompressedBitUtils.setCompressedBit(CompressedEnum.CompressedInfoEnum.DEFAULT, i);
            result.add(compressedBitInfo);
        }
        return BaseRes.buildSuccess(result);
    }


    @ApiOperation("remCompressedBit")
    @LogIndex
    @PostMapping("remCompressedBit")
    @ResponseBody
    public BaseRes<CompressedBitInfo> remCompressedBit(long sourceOffset) {
        CompressedBitInfo result = CompressedBitUtils.remCompressedBit(CompressedEnum.CompressedInfoEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(result);
    }

    @ApiOperation("getCompressedBit")
    @LogIndex
    @PostMapping("getCompressedBit")
    @ResponseBody
    public BaseRes<CompressedBitInfo> getCompressedBit(long sourceOffset) {
        CompressedBitInfo result = CompressedBitUtils.getCompressedBit(CompressedEnum.CompressedInfoEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(result);
    }


    @ApiOperation("getAllBucketKeys")
    @LogIndex
    @PostMapping("getAllBucketKeys")
    @ResponseBody
    public BaseRes<List<String>> getAllBucketKeys(long sourceOffset) {
        List<String> result = CompressedBitUtils.getAllBucketKeys(CompressedEnum.CompressedInfoEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(result);
    }


    @ApiOperation("deleteAllCompressedBit")
    @LogIndex
    @PostMapping("deleteAllCompressedBit")
    @ResponseBody
    public BaseRes<Boolean> deleteAllCompressedBit(long sourceOffset) {
        CompressedBitUtils.deleteAllCompressedBit(CompressedEnum.CompressedInfoEnum.DEFAULT, sourceOffset);
        return BaseRes.buildSuccess(true);
    }


}
