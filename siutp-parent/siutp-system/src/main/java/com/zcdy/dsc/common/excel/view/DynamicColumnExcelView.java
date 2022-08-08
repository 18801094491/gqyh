package com.zcdy.dsc.common.excel.view;

import com.zcdy.dsc.common.excel.ExcelExportUtil;
import com.zcdy.dsc.common.excel.def.NormalExcelConstants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.MiniAbstractExcelView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author : songguang.jiao
 */
public class DynamicColumnExcelView extends MiniAbstractExcelView {

    @Override
    /**
     * 动态列导出
     * @author tangchao
     * @since 2020-5-6 18:45:42
     * NormalExcelConstants.HEADER_LIST
     * head
     * {
     *     title: "", //列头
     *     dataIndex: "",//字段名称
     *     width: "",//宽度
     * }
     */
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codedFileName = "临时文件";
        List<Map<String, String>> head = (List<Map<String, String>>)model.get(NormalExcelConstants.HEADER_LIST);
        Workbook workbook = ExcelExportUtil.exportExcel((ExportParams) model.get(NormalExcelConstants.PARAMS)
        ,head, (Collection<?>) model.get(org.jeecgframework.poi.excel.def.NormalExcelConstants.DATA_LIST));

        if (model.containsKey(NormalExcelConstants.FILE_NAME)) {
            codedFileName = (String) model.get(NormalExcelConstants.FILE_NAME);
        }
        if (workbook instanceof HSSFWorkbook) {
            codedFileName += HSSF;
        } else {
            codedFileName += XSSF;
        }
        if (isIE(request)) {
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF8");
        } else {
            codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }
}
