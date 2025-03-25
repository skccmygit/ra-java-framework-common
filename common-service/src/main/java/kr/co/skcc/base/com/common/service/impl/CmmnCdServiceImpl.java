package kr.co.skcc.base.com.common.service.impl;

import org.springframework.transaction.annotation.Transactional;
import kr.co.skcc.base.com.common.api.dto.domainDto.CmmnCdDtlDto;
import kr.co.skcc.base.com.common.api.dto.domainDto.CmmnCdDto;
import kr.co.skcc.base.com.common.api.dto.responseDto.ifDto.CmmnCdIDto;
import kr.co.skcc.base.com.common.domain.cmmnCd.CmmnCd;
import kr.co.skcc.base.com.common.domain.cmmnCd.CmmnCdDtl;
import kr.co.skcc.base.com.common.domain.cmmnCd.pk.CmmnCdDtlPK;
import kr.co.skcc.base.com.common.exception.ServiceException;
import kr.co.skcc.base.com.common.repository.CmmnCdDtlRepository;
import kr.co.skcc.base.com.common.repository.CmmnCdRepository;
import kr.co.skcc.base.com.common.service.CmmnCdService;
import kr.co.skcc.base.com.common.util.ExcelStream;
import kr.co.skcc.base.com.common.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
public class CmmnCdServiceImpl implements CmmnCdService {

    @Autowired
    private CmmnCdRepository cmmnCdRepository;

    @Autowired
    private CmmnCdDtlRepository cmmnCdDtlRepository;

    @Override
    public CmmnCdDto create(CmmnCdDto cmmnCdDto) {

        if(cmmnCdRepository.existsById(cmmnCdDto.getCmmnCd())) throw new ServiceException("COM.I0006");

        CmmnCd entity = cmmnCdRepository.save(cmmnCdDto.toEntity());
        return entity.toApi();
    }

    @Override
    public CmmnCdDto update(CmmnCdDto cmmnCdDto) {

        if(!cmmnCdRepository.existsById(cmmnCdDto.getCmmnCd()))    throw new ServiceException("COM.I0004");

        CmmnCd entity = cmmnCdRepository.save(cmmnCdDto.toEntity());
        return entity.toApi();
    }

    @Override
    public List<CmmnCdDto> findCmmnCdList(String chrgTaskGroupCd, String cmmnCdNm){

        chrgTaskGroupCd = chrgTaskGroupCd == null ? "" : chrgTaskGroupCd;
        cmmnCdNm = cmmnCdNm == null ? "" : cmmnCdNm;

        List<CmmnCd> list = cmmnCdRepository.findCmmnCdNm(chrgTaskGroupCd, cmmnCdNm,
                Sort.by("chrgTaskGroupCd").and(Sort.by("cmmnCd").ascending()));

        List<CmmnCdDtlDto> cmmnCdDtlDtoList = ObjectUtil.toDtoList(cmmnCdDtlRepository.findByCmmnCd("CHRG_TASK_GROUP_CD", ""), CmmnCdDtlDto.class);

        return list.stream().map(m -> new CmmnCdDto(m,  cmmnCdDtlDtoList.stream()
                                                                        .filter(i->i.getCmmnCdVal().equals(m.getChrgTaskGroupCd()))
                                                                        .findFirst().get().getCmmnCdValNm())
                                ).collect(Collectors.toList());

    }

    @Override
    public void excelDownloadCmmnCd(String chrgTaskGroupCd, String cmmnCdNm) throws IOException {
        chrgTaskGroupCd = chrgTaskGroupCd == null ? "" : chrgTaskGroupCd;
        cmmnCdNm = cmmnCdNm == null ? "" : cmmnCdNm;

        String fileName = "공통코드";
        //시트명 세팅
        //String sheetName = "공통코드";

        String[] arrayHeaderNm = { "시스템명","담당업무그룹","공통코드","공통코드명","공통코드설명","상세코드길이","공통코드사용여부","상세코드","상세코드명"
                ,"상세코드설명","상세코드순번","상세코드사용여부","상위공통코드","상위공통코드명","상위상세코드","상위상세코드명","참조속성명1","참조속성명2","참조속성명3"
                ,"참조속성명4","참조속성명5","참조속성명6","참조속성명7","참조속성명8","참조속성명9","참조속성명10","참조속성값1","참조속성값2","참조속성값3"
                ,"참조속성값4","참조속성값5","참조속성값6","참조속성값7","참조속성값8","참조속성값9","참조속성값10" };

        String[] arrayBodyColNm = {"systmClNm","chrgTaskGroupNm","cmmnCd","cmmnCdNm","cmmnCdDesc","cmmnCdValLenth","useYn","cmmnCdVal","cmmnCdValNm"
                ,"cmmnCdValDesc","sortSeqn","dtlUseYn","superCmmnCd","superCmmnCdNm","superCmmnCdVal","superCmmnCdValNm","refrnAttrNm1","refrnAttrNm2","refrnAttrNm3"
                ,"refrnAttrNm4","refrnAttrNm5","refrnAttrNm6","refrnAttrNm7","refrnAttrNm8","refrnAttrNm9","refrnAttrNm10","refrnAttrVal1","refrnAttrVal2","refrnAttrVal3"
                ,"refrnAttrVal4","refrnAttrVal5","refrnAttrVal6","refrnAttrVal7","refrnAttrVal8","refrnAttrVal9","refrnAttrVal10"};

        ExcelStream e = new ExcelStream(arrayHeaderNm, arrayBodyColNm);
        int allCount = cmmnCdRepository.excelDataCount(chrgTaskGroupCd, cmmnCdNm);
        int size = 5000;
        int maxExcelSize = 200000;


        if(allCount == 0) throw new ServiceException("COM.I0033");
        if(allCount > maxExcelSize) throw new ServiceException("COM.I0032");

        int pageSize = allCount % size == 0 ? allCount/size : allCount/size + 1;

        List<CmmnCdIDto> cmmnCdExcelIDto;
        for(int i = 0; i < pageSize; i++){
            int offset = (i * size);
            log.info("allCount : {}, pageSize : {}, size : {}, i : {}, offset = {} ", allCount, pageSize, size, i, offset);
            cmmnCdExcelIDto = cmmnCdRepository.findCmmnCdAndCmmnCdDtl(chrgTaskGroupCd, cmmnCdNm, offset, size);
            e.start(cmmnCdExcelIDto);
        }
        e.download(fileName);

    }

    @Override
    public CmmnCdDtlDto createDtl(CmmnCdDtlDto cmmnCdDtlDto) {

        CmmnCdDtlPK pk = new CmmnCdDtlPK(cmmnCdDtlDto.getCmmnCd(), cmmnCdDtlDto.getCmmnCdVal());
        if(cmmnCdDtlRepository.existsById(pk))  throw new ServiceException("COM.I0006");

        Optional<CmmnCd> oCmmnCd = cmmnCdRepository.findById(cmmnCdDtlDto.getCmmnCd());
        if(oCmmnCd.isEmpty())    throw new ServiceException("COM.I0004");

        if(oCmmnCd.get().getCmmnCdValLenth() < cmmnCdDtlDto.getCmmnCdVal().length()) throw new ServiceException("COM.I0035");

        CmmnCdDtl entity = cmmnCdDtlRepository.save(cmmnCdDtlDto.toEntity());

        return entity.toApi();
    }

    @Override
    public CmmnCdDtlDto updateDtl(CmmnCdDtlDto cmmnCdDtlDto){

        CmmnCdDtlPK pk = new CmmnCdDtlPK(cmmnCdDtlDto.getCmmnCd(),cmmnCdDtlDto.getCmmnCdVal());
        if(!cmmnCdDtlRepository.existsById(pk)) throw new ServiceException("COM.I0004");

        Optional<CmmnCd> oCmmnCd = cmmnCdRepository.findById(cmmnCdDtlDto.getCmmnCd());
        if(oCmmnCd.isEmpty())    throw new ServiceException("COM.I0004");
        if(oCmmnCd.get().getCmmnCdValLenth() < cmmnCdDtlDto.getCmmnCdVal().length()) throw new ServiceException("COM.I0035");

        CmmnCdDtl entity = cmmnCdDtlRepository.save(cmmnCdDtlDto.toEntity());

        return entity.toApi();
    }

    @Override
    public HashMap<String, Object> findByCmmnCdDtlList(List<String> cmmnCdList) {
        HashMap<String, Object> obj = new HashMap<>();

        for(String cmmnCd : cmmnCdList){
            List<CmmnCdDtlDto> list = findByCmmnCd(cmmnCd);
            if(list != null) {
                obj.put(cmmnCd, list.stream().filter(m -> "Y".equals(m.getUseYn())).collect(Collectors.toList()));
            }else{
                obj.put(cmmnCd, new ArrayList<>());
            }
        }
        if(obj.isEmpty()) throw new ServiceException("COM.I0005");

        return obj;
    }

    @Override
    public List<CmmnCdDtlDto> findByCmmnCd(String cmmnCd) {
        return ObjectUtil.toDtoList(cmmnCdDtlRepository.findByCmmnCd(cmmnCd,"" ), CmmnCdDtlDto.class);
    }


    @Override
    public void updateDtlList(List<CmmnCdDtlDto> cmmnCdDtlDtoList){
        for(CmmnCdDtlDto item : cmmnCdDtlDtoList) {
            this.updateDtl(item);
        }
    }

    @Override
    public List<CmmnCdDtlDto> findCmmnCdDtlByCondition(String chrgTaskGroupCd, String cmmnCdNm){

        chrgTaskGroupCd = chrgTaskGroupCd == null ? "" : chrgTaskGroupCd;
        cmmnCdNm = cmmnCdNm == null ? "" : cmmnCdNm;

        return cmmnCdDtlRepository.findCmmnCdDtl(chrgTaskGroupCd, cmmnCdNm);
    }

    @Override
    public List<CmmnCdDtlDto> searchCmmnCdDtlBySuperfindByCmmnCd(String cmmnCd, String superCmmnCd, String superCmmnCdVal) {

        return (superCmmnCd == null && superCmmnCdVal == null) ?
                ObjectUtil.toDtoList(cmmnCdDtlRepository.findByCmmnCd(cmmnCd,"" ), CmmnCdDtlDto.class) :
                cmmnCdDtlRepository.findSuperCmmnCd(cmmnCd, superCmmnCd, superCmmnCdVal, Sort.by("sortSeqn"));
    }

}
