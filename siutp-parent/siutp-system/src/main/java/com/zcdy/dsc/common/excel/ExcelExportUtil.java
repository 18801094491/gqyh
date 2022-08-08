package com.zcdy.dsc.common.excel;

import com.zcdy.dsc.common.excel.export.ExcelExportServer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author : songguang.jiao
 */
public class ExcelExportUtil {

    public static Workbook exportExcel(ExportParams entity, List<Map<String, String>> header, Collection<?> dataSet) {
        Workbook workbook;
        if (ExcelType.HSSF.equals(entity.getType())) {
            workbook = new HSSFWorkbook();
        } else if (dataSet.size() < 1000) {
            workbook = new XSSFWorkbook();
        } else {
            workbook = new SXSSFWorkbook();
        }
        new ExcelExportServer().createSheet(workbook, entity, header, dataSet);
        return workbook;

    }



}
