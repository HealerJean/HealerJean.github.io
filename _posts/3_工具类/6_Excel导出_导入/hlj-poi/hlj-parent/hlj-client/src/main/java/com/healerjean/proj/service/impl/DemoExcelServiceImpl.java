package com.healerjean.proj.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.healerjean.proj.dao.mapper.DemoEntityMapper;
import com.healerjean.proj.dto.excel.demo.ExcelConstant;
import com.healerjean.proj.dto.excel.write.DemoData;
import com.healerjean.proj.pojo.DemoEntity;
import com.healerjean.proj.service.DemoExcelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhangyujin
 * @date 2021/12/20  8:03 下午.
 * @description
 */
@Slf4j
@Service
public class DemoExcelServiceImpl implements DemoExcelService {
    @Resource
    private DemoEntityMapper demoEntityMapper;


    @Override
    public void exportBigDataExcel(HttpServletResponse response) {
        ServletOutputStream out = null;
        ExcelWriter excelWriter = null;
        try {
            out = response.getOutputStream();
            excelWriter = EasyExcel.write(out, DemoEntity.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();

            QueryWrapper<DemoEntity> queryWrapper = new QueryWrapper<>();
            Integer totalRowCount = demoEntityMapper.selectCount(queryWrapper);
            Integer pageSize = ExcelConstant.PER_WRITE_ROW_COUNT;
            Integer writeCount = totalRowCount % pageSize == 0 ? (totalRowCount / pageSize) : (totalRowCount / pageSize + 1);

            int count = 0 ;
            for (int i = 0; i < writeCount; i++) {
                List<List<String>> dataList = new ArrayList<>();
                Page<DemoEntity> page = new Page<>(i, pageSize);
                IPage<DemoEntity> demoEntityPage = demoEntityMapper.selectPage(page, queryWrapper);
                List<DemoEntity> demoEntities = demoEntityPage.getRecords();
                if (!CollectionUtils.isEmpty(demoEntities)) {
                    demoEntities.forEach(demoEntity -> {
                        dataList.add(Arrays.asList(
                                demoEntity.getName(),
                                demoEntity.getEmail(),
                                demoEntity.getCreateName(),
                                demoEntity.getPhone()
                        ));
                    });
                }
                //我自己加的，跳出循环
                excelWriter.write(dataList, writeSheet);
                count = count + dataList.size();
                if (count > 1000){
                    break;
                }
            }

            // 下载EXCEL
            String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        } catch (Exception e) {
            log.error("error", e);
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

    }

}
