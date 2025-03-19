package kr.co.skcc.oss.com.common.util;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.skcc.oss.com.common.api.dto.excelDto.*;
import kr.co.skcc.oss.com.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * ExcelServiceImpl.java
 *  : Excel 다운로드/업로드 등 Excel과 관련된 API 로직 작성 Class
 *
 * @author 천지은
 * @version 1.0.0
 * @since 2021-09-27, 최초 작성
 */
@Service
@Slf4j
public class ExcelUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("-yyyyMMdd_HHmmss");

    private static String tempFileSavePath;

    private static String uploadTempFileSavePath;

    private static SXSSFWorkbook wb;

    private static SXSSFSheet sheet;

    private static String fontName = "Arial";

    private static SXSSFRow row;

    private static SXSSFCell cell;

    @Value("${app.filePath.tempFile:#{null}}")
    public void setTempFileSavePath(String value){
        this.tempFileSavePath = value;
    }

    @Value("${app.filePath.uploadTemp:#{null}}")
    public void setUploadTempFileSavePath(String value){ this.uploadTempFileSavePath = value;  }

    public static String excelDownload(HttpServletResponse response, List<ExcelDto> excelDto, String fileName) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return excelDownload(excelDto, fileName,  true );
    }
    public static String excelDownload(List<ExcelDto> excelDto, String fileName) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        return excelDownload(excelDto, fileName, true );
    }

    public static String excelDownload(List<ExcelDto> excelDto, String fileName, boolean isDownload) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        String encFile = "";
        wb = new SXSSFWorkbook();
        wb.setCompressTempFiles(true);

        try {
            for (ExcelDto dto : excelDto) {
                createSheet(dto);
            }

            // 다운로드만 할 경우
            if(isDownload) {
                // fileDownload 함수가 encFile 에 값이 있으면 그 파일을 다운로드하고 없으면 메모리에 wb를 write 하는 방식
                // 여기선 메모리에 있는거 바로 다운로드 하려고 일부러 공백을 넣어줌
                fileDownload(encFile, fileName);
                return encFile;
            }

            // 임시파일로 생성할 경우
            return createFile();

        } catch(Exception e){
            log.error(e.getMessage(), e);
            return null;
        } finally{
            if(wb != null) {
                wb.close();
                wb.dispose();
            }
        }
    }

    private static void createSheet(ExcelDto item) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException {
        log.info("** create sheet start");
        // 시트 생성
        sheet = wb.createSheet(item.getSheetName());

        // body 시작위치 return
        int stBodyRowNum = createHeader(item.getHeader());
        createBody(item.getBody(), stBodyRowNum);
    }

    private static int createHeader(ExcelHeaderDto excelHeaderDto) throws IOException {
        log.info("*** create header start");
        int rowNum = excelHeaderDto.getStRownum();
        // 타이틀이 있을 경우 타이틀 세팅 -  Text / img 두가지 버전 존재
        if(!(excelHeaderDto.getTitle() == null || "".equals(excelHeaderDto.getTitle())) || excelHeaderDto.isImgTitle()) {
            rowNum = createTitle(excelHeaderDto);
        }

        //헤더 스타일 - hCellStyle : 완전 default / hCellStyle1 : 회색바탕, 테두리 O, 가운데정렬 / hCellStyle2 : 바탕 X, 테두리 X, 좌측정렬
        List<ExcelHeaderCellType> cellType = excelHeaderDto.getCellType();
        CellStyle hCellStyle = wb.createCellStyle();
        CellStyle hCellStyle1 = createHeaderCellStyle1();
        CellStyle hCellStyle2 = createHeaderCellStyle2();

        //헤더값 & 스타일 세팅
        for(Object header : excelHeaderDto.getHeaderNm()){
            String[] h = (String[])header;
            row = sheet.createRow(rowNum);
            for(int i = 0; i < excelHeaderDto.getHLength(); i++){
                cell = row.createCell(i);
                if(!"".equals(String.valueOf(h[i])))    cell.setCellValue(String.valueOf(h[i]));
                //헤더 스타일 세팅
                if (cellType != null) {
                    // 지정된 row/col 에 맞는 스타일 세팅
                    for (ExcelHeaderCellType item : cellType) {
                        if (item.getRow() == rowNum && item.getCol() == i && item.getCellType() == 0) {
                            cell.setCellStyle(hCellStyle);
                            break;
                        }
                        if (item.getRow() == rowNum && item.getCol() == i && item.getCellType() == 1) {
                            cell.setCellStyle(hCellStyle2);
                            break;
                        }
                        cell.setCellStyle(hCellStyle1);
                    }
                }else{
                    cell.setCellStyle(hCellStyle1);
                }
            }
            rowNum++;
        }

        // 헤더 cell 병합
        if(excelHeaderDto.getHeaderMerge() != null && !excelHeaderDto.getHeaderMerge().isEmpty()) {
            for (Object mergeItem : excelHeaderDto.getHeaderMerge()) {
                ExcelMergeDto dto = (ExcelMergeDto) mergeItem;
                // 관제에서 cell 이 1칸일 경우에도 merge 데이터를 넣어 문제가 되어 예외처리
                if ((dto.getRowStart() != dto.getRowEnd()) || (dto.getColStart() != dto.getColEnd())) {
                    sheet.addMergedRegion(new CellRangeAddress(dto.getRowStart(), dto.getRowEnd(), dto.getColStart(), dto.getColEnd()));
                }
            }
        }

        return rowNum;
    }

    private static int createTitle(ExcelHeaderDto excelHeaderDto) throws IOException {
        log.info("**** create title start");
        int rownum = 0;

        // 타이틀이 이미지일 경우
        if(excelHeaderDto.isImgTitle()){
            row = sheet.createRow(excelHeaderDto.getImgStartRow());
            cell = row.createCell(excelHeaderDto.getImgStartCol());

            // 전달받은 start/end 좌표 기준으로 cell merge 하고 이미지 뿌리기
            sheet.addMergedRegion(new CellRangeAddress(excelHeaderDto.getImgStartRow(), excelHeaderDto.getImgEndRow(), excelHeaderDto.getImgStartCol(), excelHeaderDto.getImgEndCol()));

            InputStream in = new ClassPathResource("template/" + excelHeaderDto.getTitleImgPath()).getInputStream();
            byte[] bytes = IOUtils.toByteArray(in);
            int picIdx = wb.addPicture(bytes, SXSSFWorkbook.PICTURE_TYPE_PNG);
            in.close();
            SXSSFCreationHelper helper = (SXSSFCreationHelper) wb.getCreationHelper();
            SXSSFDrawing drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();

            // 이미지 출력할 cell 위치 지정
            anchor.setRow1(excelHeaderDto.getImgStartRow());
            anchor.setCol1(excelHeaderDto.getImgStartCol());
            // 왜 1을 더해야 맞는지 모르겠음
            anchor.setRow2(excelHeaderDto.getImgEndRow()+1);
            anchor.setCol2(excelHeaderDto.getImgEndCol()+1);

            // 이미지 그리기
            drawing.createPicture(anchor, picIdx);
            rownum = excelHeaderDto.getStRownum();
        }
        // 타이틀이 Text 일 경우
        else {
            CellStyle tCellStyle = createTitleCellStyle();

            // row create - 무조건 0,0 에서 시작 / 높이 설정
            row = sheet.createRow(0);
            row.setHeight((short)1000);

            // cell 설정 - 타이틀 값 & 스타일 적용 -  가로/세로 가운데 정렬, font만 custom해서 default 세팅
            cell = row.createCell(0);
            cell.setCellValue(String.valueOf(excelHeaderDto.getTitle()));
            cell.setCellStyle(tCellStyle);

            // 타이틀 cell 병합 - (0,0) ~ (0,헤더사이즈)
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, excelHeaderDto.getHLength()-1));
            rownum++;
        }
        return rownum;
    }

    private static void createBody(ExcelBodyDto excelBodyDto, int stBodyRowNum) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        log.info("**** create body start");
        int rowNum = stBodyRowNum;

        // body cell style 설정
        CellStyle bCellStyle = createBodyCellStyle();
        // body 세팅
        Map<String, String> dataMap;
        List<ExcelCellType> excelCellTypeList = excelBodyDto.getCellType();
        for(Object body : excelBodyDto.getBody()){
            if(body instanceof Map){
                dataMap = (Map<String, String>)body;
            }else {
                dataMap = BeanUtils.describe(body);
            }
            if(dataMap != null) {
                row = sheet.createRow(rowNum);
                int i = 0;
                for (Object colNmObj : excelBodyDto.getBodyColNm()) {
                    cell = row.createCell(i++);
                    setCellValueAndType(dataMap, excelCellTypeList, String.valueOf(colNmObj), bCellStyle);
                }
            }
            rowNum++;
            if(rowNum % 1000 == 0){
                sheet.setRandomAccessWindowSize(1000);
            }
        }

        // 가로 사이즈 설정
        int i = 0;
        for (Object colNm : excelBodyDto.getBodyColNm()) {
            if(excelCellTypeList != null) {
                Optional<ExcelCellType> excelCellTye = excelCellTypeList.stream().filter(c -> c.getColNm().equals(String.valueOf(colNm))).findFirst();
                if (excelCellTye.isPresent()) {
                    if (excelCellTye.get().getColWidth() != 0) {
                        sheet.setColumnWidth(i++, excelCellTye.get().getColWidth());
                        continue;
                    }
                }
            }
            // 가로사이즈 default 세팅
            sheet.setColumnWidth(i, 4200 );
            i++;
        }
    }
    private static void setCellValueAndType(Map<String, String> dataMap, List<ExcelCellType> excelCellTypeList, String colNm, CellStyle bCellStyle){
        // default
        cell.setCellType(CellType.STRING);
        cell.setCellValue(dataMap.get(colNm));
        bCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("General"));

        // cell Type 구분 ( 현재는 NUMERIC or STRING)
        if (excelCellTypeList != null) {
            // 컬럼명으로 cellType 값 찾기
            Optional<ExcelCellType> excelCellType = excelCellTypeList.stream().filter(c -> c.getColNm().equals(colNm)).findFirst();
            if (excelCellType.isPresent()) {
                if ("NUMERIC".equals(excelCellType.get().getColCellType())) {
                    bCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(Double.valueOf(dataMap.get(colNm)));
                } else if ("DATE".equals(excelCellType.get().getColCellType())) {
                    if (NumberUtils.isNumber(dataMap.get(colNm))) {
                        cell.setCellValue(setDateType(dataMap.get(colNm)));
                    }
                }
            }
        }
        cell.setCellStyle(bCellStyle);
    }

    private static String setDateType(String colVal) {
        int len = colVal.length();
        switch (len) {
            case 6:
                colVal = LocalDate.parse(colVal, DateTimeFormatter.ofPattern("yyMMdd")).format(DateTimeFormatter.ofPattern("yy-MM-dd"));
                break;
            case 8:
                colVal = LocalDate.parse(colVal, DateTimeFormatter.ofPattern("yyyyMMdd")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                break;
            case 14:
                colVal = LocalDateTime.parse(colVal, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                break;
            default:
                break;
        }
        return colVal;
    }

    private static String createFile() {
        String saveFilePath = "";

        saveFilePath = tempFileSavePath + UUID.randomUUID() + ".xlsx";
        // 임시 디렉토리가 존재하지 않을 경우 디렉토리 생성
        if (!new File(tempFileSavePath).exists()) {
            new File(tempFileSavePath).mkdir();
        }
        // 파일 생성
        File file = new File(saveFilePath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // workbook -> 파일에 쓰기
            wb.write(fos);
            // 암호화 여부 & 환경에 따라 Drm 처리
            return saveFilePath;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static void fileDownload(String filePath, String fileName) throws IOException {

        String contentType = "application/octet-stream";
        String charset = "UTF-8";
        String contentTransferEncoding = "binary";

        HttpServletResponse response;
        fileName = fileName + LocalDateTime.now().format(DATE_TIME_FORMATTER);

        if(filePath == null || filePath.isEmpty()) {
            if(wb.getNumberOfSheets() != 0) {
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                response = ResponseUtil.setResponseHeader(contentType, charset, URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
                wb.write(response.getOutputStream());
            }else{
                throw new ServiceException("COM.I0003");
            }
        }else{
            File file;
            OutputStream out;
            String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);

            if ("xlsx".equals(fileType)) {
                fileName = fileName + ".xlsx";
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            } else if ("csv".equals(fileType)) {
                fileName = fileName + ".csv";
                charset = "MS949";
            }

            file = new File(filePath);
            if (!file.exists()) throw new ServiceException("COM.I0013");

            response = ResponseUtil.setResponseHeader(contentType,
                    charset,
                    URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"),
                    contentTransferEncoding,
                    (int) file.length());

            try (InputStream in = new FileInputStream(file)) {
                out = response.getOutputStream();

                byte[] buf = new byte[8096];
                int len = 0;
                while ((len = in.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new ServiceException("COM.I0003");
            }
        }
    }

    private static Font createFont(boolean boldYn, int size) {
        Font font = wb.createFont();
        font.setFontName(fontName);
        font.setFontHeight((short)(size*20));
        font.setBold(boldYn);

        return font;
    }
    private static CellStyle setCellStyle(HorizontalAlignment hAlign, VerticalAlignment vAlign, Font font, String lineYn, String bgColorYn){
        CellStyle cellStyle = wb.createCellStyle();

        cellStyle.setAlignment(hAlign);
        cellStyle.setVerticalAlignment(vAlign);
        cellStyle.setFont(font);

        if("Y".equals(lineYn)){
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
        }else{
            cellStyle.setBorderBottom(BorderStyle.NONE);
            cellStyle.setBorderTop(BorderStyle.NONE);
            cellStyle.setBorderLeft(BorderStyle.NONE);
            cellStyle.setBorderRight(BorderStyle.NONE);
        }

        if("Y".equals(bgColorYn)) {
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else{
            cellStyle.setFillPattern(FillPatternType.NO_FILL);
        }

        return cellStyle;
    }
    public static CellStyle createTitleCellStyle(){
        return setCellStyle(HorizontalAlignment.CENTER, VerticalAlignment.CENTER, createFont(true, 16), "N","N");
    }
    public static CellStyle createHeaderCellStyle1(){
        return setCellStyle(HorizontalAlignment.CENTER, VerticalAlignment.CENTER, createFont(false, 10), "Y","Y");
    }
    public static CellStyle createHeaderCellStyle2(){
        return setCellStyle(HorizontalAlignment.LEFT, VerticalAlignment.CENTER, createFont(true, 10), "N","N");
    }
    public static CellStyle createBodyCellStyle(){
        return setCellStyle(HorizontalAlignment.CENTER, VerticalAlignment.CENTER, createFont(false, 10), "Y","N");
    }

    /*********************************************************************************************************************/
    public static String excelFormDownload(InputStream in, String fileName, List<List<ExcelFormDto>> excelFormDtoList) throws IOException {
        return excelFormDownload(in, fileName, excelFormDtoList, "N");
    }

    public static String excelFormDownload(InputStream in, String fileName, List<List<ExcelFormDto>> excelFormDtoList, String secuFlag) throws IOException {

        String newFilePath = tempFileSavePath + UUID.randomUUID() + ".xlsx";
        try(FileOutputStream outFile = new FileOutputStream(newFilePath)) {

            // 3. 복사한 파일 열기
            XSSFWorkbook workbook = new XSSFWorkbook(in);

            // 4. 시트열기
            int sheetNo = 0;
            XSSFSheet sheet;
            XSSFRow row;
            XSSFCell cell;

            if(!(excelFormDtoList == null || excelFormDtoList.isEmpty())) {
                for (List<ExcelFormDto> item : excelFormDtoList) {
                    sheet = workbook.getSheetAt(sheetNo++);
                    for (ExcelFormDto data : item) {
                        row = sheet.getRow(data.getRow()) == null ? sheet.createRow(data.getRow()) : sheet.getRow(data.getRow());
                        cell = row.getCell(data.getCol()) == null ? row.createCell(data.getCol()) : row.getCell(data.getCol());
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(data.getValue());
                    }
                }
            }

            workbook.write(outFile);
            fileDownload(newFilePath, fileName);

            return newFilePath;
        }catch(Exception e){
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /*********************************************************************************************************************/
    public static String csvDownload(ExcelDto csvData, String fileName) {
        return csvDownload(csvData, fileName, true);
    }

    public static String csvDownload(ExcelDto csvData, String fileName, boolean isDownload) {

        ExcelHeaderDto excelHeaderDto = csvData.getHeader();
        ExcelBodyDto excelBodyDto = csvData.getBody();
        List<ExcelCellType> cellType = excelBodyDto.getCellType();
        List<List<String>> listData;
        List<Integer> typeList;

        try {
            listData = createCsvListData(excelHeaderDto, excelBodyDto);
            typeList = createTypeList(cellType, excelBodyDto.getBodyColNm());
            String tempFile = createCsvFile(listData, typeList);
            if(isDownload) fileDownload(tempFile, fileName);
            return tempFile;

        }catch(Exception e){
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private static List<List<String>> createCsvListData(ExcelHeaderDto excelHeaderDto, ExcelBodyDto excelBodyDto) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<List<String>> listData = new ArrayList<>();

        if (excelHeaderDto != null) {
            if (excelHeaderDto.getHeaderNm() != null) {
                listData.add(List.of(excelHeaderDto.getHeaderNm().get(0)));
            }
        }

        Map<String, String> dataMap;
        for (Object body : excelBodyDto.getBody()) {
            if(body instanceof Map) dataMap = (Map<String, String>)body;
            else                    dataMap = BeanUtils.describe(body);

            List<String> temp = new ArrayList<>();
            for(Object colNmObj : excelBodyDto.getBodyColNm()){
                String colNm = String.valueOf(colNmObj);
                temp.add(dataMap.get(colNm) == null ? "" : dataMap.get(colNm));

            }
            listData.add(temp);
        }

        return listData;
    }
    private static List<Integer> createTypeList(List<ExcelCellType> cellType, List<String> colNmObj){
        List<Integer> typeList = new ArrayList<>();

        if(cellType == null) {
            return null;
        }

        for(int typeChk = 0; typeChk < colNmObj.size(); typeChk++){
            Object obj = colNmObj.get(typeChk);
            Optional<ExcelCellType> excelCellType = cellType.stream().filter(c -> c.getColNm().equals(String.valueOf(obj))).findFirst();
            if (excelCellType.isPresent()) {
                if("NUMERIC".equals(excelCellType.get().getColCellType()) ||
                        "LONGTEXT".equals(excelCellType.get().getColCellType())) {
                    typeList.add(typeChk);
                }
            }
        }
        return typeList;
    }
    private static String createCsvFile(List<List<String>> listData, List<Integer> typeList) {

        String charset = "MS949";
        String saveFilePath = tempFileSavePath + UUID.randomUUID() + ".csv";

        if (!new File(tempFileSavePath).exists()){
            new File(tempFileSavePath).mkdir();
        }

        File csvFile = new File(saveFilePath);
        CSVPrinter csvPrinter;
        OutputStreamWriter osw;
        BufferedWriter bufWriter = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(csvFile), charset);
            bufWriter = new BufferedWriter(osw);
            csvPrinter = new CSVPrinter(bufWriter, CSVFormat.DEFAULT);
            for(List<String> line : listData){
                List<String> writeLine = new ArrayList<>();
                if(typeList == null || typeList.isEmpty()) {
                    for (String str : line) {
                        writeLine.add("=\"" + str.replace("\"", "\"\"") + "\"");
                    }
                }else{
                    for(int typeChk = 0; typeChk < line.size();  typeChk++ ) {
                        String str = line.get(typeChk);
                        if (!typeList.contains(typeChk)) {
                            writeLine.add("=\"" + str.replace("\"", "\"\"") + "\"");
                        } else {
                            writeLine.add(str);
                        }
                    }
                }
                csvPrinter.printRecord(writeLine);
            }
            csvPrinter.flush();

            return saveFilePath;
        } catch(IOException e){
            log.error(e.getMessage(), e);
            return null;
        } finally {
            try {
                if(bufWriter != null) bufWriter.close();
            } catch(IOException e){
                log.error(e.getMessage(), e);
            }
        }
    }

    public static List<ExcelDto> excelUpload(MultipartFile m) throws IOException {

        String filePath = uploadTempFileSavePath + UUID.randomUUID() + "." + FilenameUtils.getExtension(m.getOriginalFilename());

        if (!new File(uploadTempFileSavePath).exists()){
            new File(uploadTempFileSavePath).mkdir();
        }
        m.transferTo(new File(filePath));
        File dm = new File(filePath);

        List<ExcelDto> excelDtoList = new ArrayList<>();
        Workbook wb;

        String originalFileName = m.getOriginalFilename();
        if(originalFileName == null) {
            return null;
        }

        try( InputStream fi = new FileInputStream(dm)) {
            if ("xlsx".equals(FilenameUtils.getExtension(m.getOriginalFilename()))) {
                wb = new XSSFWorkbook(fi);
            } else if ("xls".equals(FilenameUtils.getExtension(m.getOriginalFilename()))) {
                wb = new HSSFWorkbook(fi);
            } else {
                throw new ServiceException("COM.I0015");
            }

            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                Sheet sheet = wb.getSheetAt(i);
                excelDtoList.add(readSheet(sheet));
            }
            return excelDtoList;

        }catch (Exception e) {
            throw new ServiceException("COM.I0031");
        }
    }

    private static ExcelDto readSheet(Sheet sheet){
        ExcelHeaderDto headerDto = new ExcelHeaderDto();
        ExcelBodyDto   bodyDto = new ExcelBodyDto();

        List<String[]> hList = new ArrayList<>();
        List<List<String>> bList  = new ArrayList<>();

        try {
            hList.add(readHeader(sheet));
            headerDto.setHeaderNm(hList);

            int size = sheet.getPhysicalNumberOfRows();
            for (int i = 1; i < size; i++) {
                bList.add(readBody(sheet, i));
            }
            bodyDto.setBody(bList);

            return new ExcelDto(sheet.getSheetName(), headerDto, bodyDto);
        }catch(Exception e){
            throw e;
        }
    }
    private static String[] readHeader(Sheet sheet) {

        Row row;
        Cell cell;

        String[] hArray = new String[0];

        try {
            row = sheet.getRow(0);
            if (row != null) {
                hArray = new String[row.getLastCellNum()];

                for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
                    cell = row.getCell(cellIndex);
                    hArray[cellIndex] = getValue(cell);
                }
            }
            return hArray;
        }catch(Exception e){
            throw e;
        }
    }

    private static List<String> readBody(Sheet sheet, int rowIndex) {
        Row row;
        Cell cell;

        List<String> rList = new ArrayList<>();
        try {
            row = sheet.getRow(rowIndex);
            if(row != null){
                for(int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
                    cell = row.getCell(cellIndex);
                    rList.add(getValue(cell));
                }
            }
            return rList;
        }catch(Exception e){
            throw e;
        }
    }

    private static String getValue(Cell cell) {
        String value = "";

        if(cell == null) value = "";
        else {
            if( cell.getCellType() == CellType.FORMULA ){
                if(CellType.NUMERIC.equals(cell.getCachedFormulaResultType())){
                    value = String.valueOf(Double.valueOf(cell.getNumericCellValue()).intValue());
                }else{
                    cell.getStringCellValue();
                }
            }
            else if( cell.getCellType() == CellType.NUMERIC ) value = String.valueOf(Double.valueOf(cell.getNumericCellValue()).intValue());
            else if( cell.getCellType() == CellType.STRING ) value = cell.getStringCellValue();
            else if( cell.getCellType() == CellType.BOOLEAN ) value = String.valueOf(cell.getBooleanCellValue());
            else if( cell.getCellType() == CellType.ERROR )  value = String.valueOf(cell.getErrorCellValue());
            else if( cell.getCellType() == CellType.BLANK ) value = "";
            else value = cell.getStringCellValue();
        }
        return value;
    }
}
