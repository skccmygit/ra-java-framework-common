package kr.co.skcc.base.com.common.util;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelStream {

    private final int MAX_ROWNUM = 1000000;

    private final int MEMORY_ROW_COUNT = 100;

    SXSSFWorkbook workbook;

    String[] headerData;

    String[] bodyColNm;

    CellStyle headerStyle;

    int sheetOffset;

    int rowOffset;

    boolean isFirst;

    public ExcelStream(String[] headerData, String[] bodyColNm){
        this.sheetOffset = 0;
        this.rowOffset = 0;
        this.isFirst = true;

        this.headerData = headerData;
        this.bodyColNm = bodyColNm;

        this.workbook = new SXSSFWorkbook();
        this.workbook.setCompressTempFiles (true);

        this.headerStyle = setHeaderStyle();
    }

    public CellStyle setHeaderStyle(){

        CellStyle cellStyle = this.workbook.createCellStyle();

        Font font = this.workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeight((short)(10*20));

        cellStyle.setBorderBottom(BorderStyle.NONE);
        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setBorderLeft(BorderStyle.NONE);
        cellStyle.setBorderRight(BorderStyle.NONE);

        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFont(font);

        return cellStyle;
    }

    void setHeader(SXSSFRow row){
        // if new sheet create then make header
        int cellNum = 0;
        for (String hData : this.headerData) {
            Cell cell = row.createCell(cellNum++);
            cell.setCellValue(hData);
            cell.setCellStyle(this.headerStyle);
        }
    }

    public <T> void start(List<T> bodyData) {
        SXSSFSheet sheet;
        SXSSFRow row;

        try {
            if (this.isFirst) {
                sheet = this.workbook.createSheet();
                row = sheet.createRow(this.rowOffset++);
                sheet.setRandomAccessWindowSize(MEMORY_ROW_COUNT);
                this.setHeader(row);
                this.isFirst = false;
            } else {
                sheet = this.workbook.getSheetAt(this.sheetOffset);
            }

            Map<String, String> dataMap;
            for (T item : bodyData) {
                if (item instanceof Map) dataMap = (Map<String, String>) item;
                else dataMap = BeanUtils.describe(item);

                if (dataMap != null) {
                    // MAX값보다 크면 시트 새로 만들고 header 세팅
                    if (this.rowOffset > this.MAX_ROWNUM) {
                        this.rowOffset = 0;

                        sheet = this.workbook.createSheet();
                        sheet.setRandomAccessWindowSize(MEMORY_ROW_COUNT);
                        row = sheet.createRow(this.rowOffset++);
                        this.setHeader(row);
                        this.sheetOffset++;
                    }

                    row = sheet.createRow(this.rowOffset++);
                    this.setCell(row, dataMap);
                }
            }
        } catch(Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    void setCell(SXSSFRow row, Map<String, String> item){
        int cellNum = 0;

        for (String colNm : this.bodyColNm) {
            Cell cell = row.createCell(cellNum++);
            cell.setCellValue(item.get(colNm));
        }
    }

    public void download(String fileName) throws IOException {
        String charset = "UTF-8";
        HttpServletResponse response;

        fileName = fileName + ".xlsx";
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        response = ResponseUtil.setResponseHeader(contentType, charset, URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
        try {
            this.workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(this.workbook != null) {
                this.workbook.dispose();
                this.workbook.close();
            }
        }
    }

}
