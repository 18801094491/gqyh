package com.zcdy.dsc.common.excel.export;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.jeecgframework.poi.excel.export.styler.IExcelExportStyler;
import org.jeecgframework.poi.exception.excel.ExcelExportException;
import org.jeecgframework.poi.exception.excel.enums.ExcelExportEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author : songguang.jiao
 */
public class ExcelExportServer extends org.jeecgframework.poi.excel.export.ExcelExportServer {
    /**
     * 最大行数,超过自动多Sheet
     */
    protected int maxNum = 60000;
    private final static Logger LOGGER = LoggerFactory.getLogger(ExcelExportServer.class);

    private int createHeaderAndTitle(ExportParams entity, Sheet sheet, Workbook workbook, List<ExcelExportEntity> excelParams) {
        int rows = 0, feildWidth = getFieldWidth(excelParams);
        if (entity.getTitle() != null) {
            rows += createHeaderRow(entity, sheet, workbook, feildWidth);
        }
        rows += createTitleRow(entity, sheet, workbook, rows, excelParams);
        sheet.createFreezePane(0, rows, 0, rows);
        return rows;
    }

    /**
     * 创建表头
     *
     * @param title
     * @param index
     */
    private int createTitleRow(ExportParams title, Sheet sheet, Workbook workbook, int index, List<ExcelExportEntity> excelParams) {
        Row row = sheet.createRow(index);
        int rows = getRowNums(excelParams);
        row.setHeight((short) 450);
        Row listRow = null;
        if (rows == 2) {
            listRow = sheet.createRow(index + 1);
            listRow.setHeight((short) 450);
        }
        int cellIndex = 0;
        CellStyle titleStyle = getExcelExportStyler().getTitleStyle(title.getColor());
        for (int i = 0, exportFieldTitleSize = excelParams.size(); i < exportFieldTitleSize; i++) {
            ExcelExportEntity entity = excelParams.get(i);
            if (StringUtils.isNotBlank(entity.getName())) {
                createStringCell(row, cellIndex, entity.getName(), titleStyle, entity);
            }
            if (entity.getList() != null) {
                List<ExcelExportEntity> sTitel = entity.getList();
                if (StringUtils.isNotBlank(entity.getName())) {
                    sheet.addMergedRegion(new CellRangeAddress(index, index, cellIndex, cellIndex + sTitel.size() - 1));
                }
                for (int j = 0, size = sTitel.size(); j < size; j++) {
                    createStringCell(rows == 2 ? listRow : row, cellIndex, sTitel.get(j).getName(), titleStyle, entity);
                    cellIndex++;
                }
                cellIndex--;
            } else if (rows == 2) {
                createStringCell(listRow, cellIndex, "", titleStyle, entity);
                sheet.addMergedRegion(new CellRangeAddress(index, index + 1, cellIndex, cellIndex));
            }
            cellIndex++;
        }
        return rows;

    }

    /**
     * 判断表头是只有一行还是两行
     *
     * @param excelParams
     * @return
     */
    private int getRowNums(List<ExcelExportEntity> excelParams) {
        for (int i = 0; i < excelParams.size(); i++) {
            if (excelParams.get(i).getList() != null && StringUtils.isNotBlank(excelParams.get(i).getName())) {
                return 2;
            }
        }
        return 1;
    }


    public void createSheet(Workbook workbook, ExportParams entity, List<Map<String, String>> header, Collection<?> dataSet) {
        if(header == null) {
            return;
        }
        List<ExcelExportEntity> excelParams = new ArrayList<ExcelExportEntity>();
        for(Map<String, String> map : header){
            ExcelExportEntity excelExportEntity = new ExcelExportEntity(map.get("title"), map.get("dataIndex"), Integer.parseInt(map.get("width")));
            excelExportEntity.setFormat("");
            excelParams.add(excelExportEntity);
        }
        super.type = entity.getType();
        if (type.equals(ExcelType.XSSF)) {
            maxNum = 1000000;
        }
        Sheet sheet = null;
        try {
            sheet = workbook.createSheet(entity.getSheetName());
        } catch (Exception e) {
            // 重复遍历,出现了重名现象,创建非指定的名称Sheet
            sheet = workbook.createSheet();
        }
        try {
            dataHanlder = entity.getDataHanlder();
            if (dataHanlder != null) {
                needHanlderList = Arrays.asList(dataHanlder.getNeedHandlerFields());
            }
            setExcelExportStyler((IExcelExportStyler) entity.getStyle().getConstructor(Workbook.class).newInstance(workbook));
            Drawing patriarch = sheet.createDrawingPatriarch();
            sortAllParams(excelParams);
            int index = entity.isCreateHeadRows() ? createHeaderAndTitle(entity, sheet, workbook, excelParams) : 0;
            int titleHeight = index;
            setCellWith(excelParams, sheet);
            short rowHeight = getRowHeight(excelParams);
            setCurrentIndex(1);
            Iterator<?> its = dataSet.iterator();
            List<Object> tempList = new ArrayList<Object>();
            while (its.hasNext()) {
                Object t = its.next();
                index += createCells(patriarch, index, t, excelParams, sheet, workbook, rowHeight);
                tempList.add(t);
                if (index >= maxNum) {
                    break;
                }
            }
            mergeCells(sheet, excelParams, titleHeight);

            if (entity.getFreezeCol() != 0) {
                sheet.createFreezePane(entity.getFreezeCol(), 0, entity.getFreezeCol(), 0);
            }

            its = dataSet.iterator();
            for (int i = 0, le = tempList.size(); i < le; i++) {
                its.next();
                its.remove();
            }
            // 创建合计信息
            addStatisticsRow(getExcelExportStyler().getStyles(true, null), sheet);

            // 发现还有剩余list 继续循环创建Sheet
            if (dataSet.size() > 0) {
                createSheet(workbook, entity, header, dataSet);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ExcelExportException(ExcelExportEnum.EXPORT_ERROR, e.getCause());
        }
    }

}
