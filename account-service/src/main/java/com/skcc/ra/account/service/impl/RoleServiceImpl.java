package com.skcc.ra.account.service.impl;

import com.skcc.ra.account.api.dto.domainDto.*;
import com.skcc.ra.account.repository.*;
import org.springframework.transaction.annotation.Transactional;
import com.skcc.ra.account.adaptor.client.CommonClient;
import com.skcc.ra.account.api.dto.domainDto.*;
import com.skcc.ra.account.api.dto.requestDto.UserDeptRoleDto;
import com.skcc.ra.account.api.dto.responseDto.ifDto.RoleMappedUserIDto;
import com.skcc.ra.account.api.dto.responseDto.ifDto.UserRoleDeptMappingIDto;
import com.skcc.ra.account.api.type.AuthType;
import com.skcc.ra.account.domain.account.Account;
import com.skcc.ra.account.domain.auth.Role;
import com.skcc.ra.account.domain.auth.RoleMenu;
import com.skcc.ra.account.domain.auth.RoleScrenBttn;
import com.skcc.ra.account.domain.auth.pk.RoleMenuPK;
import com.skcc.ra.account.domain.auth.pk.RoleScrenBttnPK;
import com.skcc.ra.account.domain.auth.pk.UserRolePK;
import com.skcc.ra.account.domain.baseAuth.UserRoleDept;
import com.skcc.ra.account.domain.baseAuth.UserRoleDeptMapping;
import com.skcc.ra.account.domain.baseAuth.pk.UserRoleDeptMappingPK;
import com.skcc.ra.account.domain.hist.RoleMenuHist;
import com.skcc.ra.account.domain.hist.RoleScrenBttnHist;
import com.skcc.ra.account.domain.hist.UserRoleDeptMappingHis;
import com.skcc.ra.account.domain.hist.UserRoleHist;
import com.skcc.ra.account.domain.loginCert.RoleBasedApi;
import com.skcc.ra.account.repository.*;
import com.skcc.ra.account.service.RoleService;
import com.skcc.ra.common.api.dto.domainDto.BttnDto;
import com.skcc.ra.common.api.dto.domainDto.ScrenDto;
import com.skcc.ra.common.api.dto.excelDto.ExcelBodyDto;
import com.skcc.ra.common.api.dto.excelDto.ExcelDto;
import com.skcc.ra.common.api.dto.excelDto.ExcelHeaderDto;
import com.skcc.ra.common.api.dto.excelDto.ExcelMergeDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.MenuIDto;
import com.skcc.ra.common.domain.menu.Scren;
import com.skcc.ra.common.exception.ServiceException;
import com.skcc.ra.common.util.ExcelUtil;
import com.skcc.ra.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    UserActvyLogServiceImpl userActvyLogServiceImpl;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleMenuRepository roleMenuRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    RoleScrenBttnRepository roleScrenBttnRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleMenuHistRepository roleMenuHistRepository;

    @Autowired
    RoleScrenBttnHistRepository roleScrenBttnHistRepository;

    @Autowired
    UserRoleDeptRepository userRoleDeptRepository;

    @Autowired
    UserRoleHistRepository userRoleHistRepository;

    @Autowired
    UserRoleDeptMappingRepository userRoleDeptMappingRepository;

    @Autowired
    UserRoleDeptMappingHisRepository userRoleDeptMappingHisRepository;

    @Autowired
    private CommonClient commonClient;

    @Autowired
    private RoleBasedApiRepository roleBasedApiRepository;

    @Override
    public RoleDto findRole(String userRoleId){
        Optional<Role> oRole = roleRepository.findByUserRoleId(userRoleId);
        return oRole.map(Role::toApi).orElse(null);
    }

    @Override
    public Page<RoleDto> findRoleList(String roleNm, Pageable pageable){

        if (roleNm == null) roleNm = "";
        return roleRepository.findRoleList(roleNm, pageable);
    }

    @Override
    public List<RoleMappedUserIDto> findRoleUser(String userRoleId, List<String> deptcdList, String userNm){

        if ("".equals(userRoleId) || userRoleId == null) throw new ServiceException("COM.I0003");

        String deptcdListYn = "Y";
        if (deptcdList == null || deptcdList.isEmpty()) deptcdListYn = "N";

        if (userNm == null) userNm = "";

        return accountRepository.findRoleAccountList(userRoleId, deptcdList, deptcdListYn, userNm);
    }

    @Override
    public void createRoleUserList(List<UserRoleDto> userRoleDtoList) throws UnknownHostException {

        String crudClCd = "";
        List<UserRoleHist> userRoleHistList = new ArrayList<>();
        String ipAddr = RequestUtil.getClientIP();
        // useYn 이 Y로 넘어온 경우에는 추가 / N 이면 삭제로 판단하여 처리
        // userRole 확인 - 기존에 있을 경우 - delete -> insert -> 이력 insert
        // userRole 확인 - 기존에 없을 경우 - insert -> 이력 insert
        for (UserRoleDto item : userRoleDtoList) {
            if ("Y".equals(item.getUseYn())) {
                crudClCd = "C";
                UserRolePK userRolePK = new UserRolePK(item.getUserRoleId(), item.getUserid());
                if (userRoleRepository.existsById(userRolePK)) {
                    crudClCd = "U";
                }
                // 추가
                userRoleRepository.save(item.toEntity());
            } else {
                crudClCd = "D";
                UserRolePK userRolePK = new UserRolePK(item.getUserRoleId(), item.getUserid());
                if (userRoleRepository.existsById(userRolePK)) {
                    // 삭제
                    userRoleRepository.deleteById(userRolePK);
                }
            }
            // 이력 쌓기
            AccountDto account = accountRepository.findUserDeptInfo(item.getUserid());

            userRoleHistList.add(UserRoleHist.builder()
                    .userid(item.getUserid())
                    .userRoleId(item.getUserRoleId())
                    .chngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                    .athrtyReqstSeq(item.getAthrtyReqstSeq())
                    .crudClCd(crudClCd)
                    .deptcd(account.getDeptcd())
                    .deptNm(account.getDeptNm())
                    .chngUserIpaddr(ipAddr)
                    .chngResonCntnt(item.getChngResonCntnt())
                    .build());
        }
        // 이력 일괄 저장
       userRoleHistRepository.saveAll(userRoleHistList);
    }

    @Override
    public List<RoleDto> findUserRole(String userid) {
        return roleRepository.findUserRole(userid);
    }

    @Override
    public RoleDto createRole(Role role){
        if(roleRepository.existsById(role.getUserRoleId())) throw new ServiceException("COM.I1019");

        Role entity = roleRepository.save(role);
        return entity.toApi();
    }

    @Override
    public RoleDto modifyRole(Role role){
        if(!roleRepository.existsById(role.getUserRoleId())) throw new ServiceException("COM.I1018");

        Role entity = roleRepository.save(role);
        return entity.toApi();
    }

    @Override
    public List<MenuIDto> findRoleMenu(String userRoleId) {
        if("".equals(userRoleId) || userRoleId == null) throw new ServiceException("COM.I0003");
        return roleMenuRepository.findByUserRoleId(userRoleId);
    }

    @Override
    public RoleMenu createRoleMenu(RoleMenu roleMenu){
        RoleMenuPK pk = new RoleMenuPK(roleMenu.getUserRoleId(), roleMenu.getMenuId());
        if(roleMenuRepository.existsById(pk)) {
            roleMenuRepository.deleteById(pk);
        }
        return roleMenuRepository.save(roleMenu);
    }

    @Override
    public void deleteRoleMenu(RoleMenu roleMenu){
        RoleMenuPK pk = new RoleMenuPK(roleMenu.getUserRoleId(), roleMenu.getMenuId());
        if(roleMenuRepository.existsById(pk)) {
            roleMenuRepository.deleteById(pk);
        }
    }

    @Override
    public void modifyRoleMenu(List<RoleMenuDto> roleMenuDtoList) throws UnknownHostException {
        String crud;
        String ipAddr = RequestUtil.getClientIP();
        Scren scren;
        List<RoleScrenBttnDto> roleScrenBttnDtoList = new ArrayList<>();
        try {
            for (RoleMenuDto item : roleMenuDtoList) {
                if ("Y".equals(item.getAuthYn())) {
                    createRoleMenu(item.toEntity());
                    crud = "C";

                //권한부여되어있지 않은 상위 메뉴 찾아서..?
                } else {
                    deleteRoleMenu(item.toEntity());
                    crud = "D";

                //권한부여되어있는 하위 메뉴 찾아서..?
                }
                String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

                RoleMenuHist hist = new RoleMenuHist(item.getUserRoleId(),
                                                    item.getMenuId(),
                                                    currentTime,
                                                    crud,
                                                    ipAddr,
                                                    item.getAthrtyReqstSeq(),
                                                    item.getChngResonCntnt());
                // 여기에 이력 insert
                roleMenuHistRepository.save(hist);

                // 매핑된 메인화면 권한까지 일괄 부여..
                scren = roleMenuRepository.findScrenByMenuId(item.getMenuId());
                if (scren != null){
                   roleScrenBttnDtoList.add(new RoleScrenBttnDto(scren.getScrenId(),
                                                           "ALL",
                                                                 item.getUserRoleId(),
                                                                 item.getAuthYn(),
                                                                 item.getAthrtyReqstSeq(),
                                                                 item.getChngResonCntnt()));

                }
            }
            // 매핑된 메인화면 권한까지 일괄 부여..
            this.modifyRoleScrenBttn(roleScrenBttnDtoList);

        } catch (Exception e) {
            throw new ServiceException("COM.E0001");
        }
    }

    @Override
    public List<ScrenDto> findRoleScren(String userRoleId, String chrgTaskGroupCd, String screnClCd, String screnNm) {
        if ("".equals(userRoleId) || userRoleId == null) throw new ServiceException("COM.I0003");
        if (chrgTaskGroupCd == null) chrgTaskGroupCd = "";
        if (screnClCd == null) screnClCd = "";
        if (screnNm == null) screnNm = "";

        return commonClient.findUseScren(chrgTaskGroupCd, screnClCd, screnNm, "Y");
    }

    @Override
    public List<BttnDto> findRoleBttn(String userRoleId, String screnId) {
        if ("".equals(screnId) || screnId == null) throw new ServiceException("COM.I0003");
        List<String> roleList = new ArrayList<>();
        roleList.add(userRoleId);
        return roleScrenBttnRepository.findByScrenId(roleList, screnId);
    }

    @Override
    public void modifyRoleScrenBttn(List<RoleScrenBttnDto> roleScrenBttnDtoList) throws UnknownHostException {
        String crud;
        List<RoleScrenBttnHist> roleScrenBttnHistList = new ArrayList<>();
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));;
        String ipAddr = RequestUtil.getClientIP();

        for (RoleScrenBttnDto item : roleScrenBttnDtoList) {
            // 메뉴/화면 역할 매핑 화면에서 넘어올 경우 ALL로 넘어옴
            // 메뉴 권한 부여시 매핑된 메인 화면 권한 일괄 부여.. --> 편의기능
            if (!"ALL".equals(item.getBttnId())) {
                RoleScrenBttn rtnVal;
                if ("Y".equals(item.getAuthYn())) {
                    rtnVal = createRoleScrenBttn(item.toEntity());
                    crud = "C";
                } else {
                    rtnVal = deleteRoleScrenBttn(item.toEntity());
                    crud = "D";
                }
                if (rtnVal != null) {
                    roleScrenBttnHistList.add(new RoleScrenBttnHist(item.getUserRoleId()
                            , item.getScrenId()
                            , item.getBttnId()
                            , currentTime
                            , crud
                            , ipAddr
                            , item.getAthrtyReqstSeq()
                            , item.getChngResonCntnt()));
                }
            } else {
                ScrenDto screnDto = commonClient.findByScrenId(item.getScrenId());
                List<BttnDto> bttnDto = screnDto.getBttnList();
                for (BttnDto bttn : bttnDto) {
                    if ("Y".equals(item.getAuthYn())) {
                        createRoleScrenBttn(new RoleScrenBttn(screnDto.getScrenId()
                                , bttn.getBttnId()
                                , item.getUserRoleId()));
                        crud = "C";
                    } else {
                        deleteRoleScrenBttn(new RoleScrenBttn(screnDto.getScrenId()
                                , bttn.getBttnId()
                                , item.getUserRoleId()));
                        crud = "D";
                    }
                    roleScrenBttnHistList.add(new RoleScrenBttnHist(item.getUserRoleId()
                            , screnDto.getScrenId()
                            , bttn.getBttnId()
                            , currentTime
                            , crud
                            , ipAddr
                            , item.getAthrtyReqstSeq()
                            , item.getChngResonCntnt()));
                }
            }
        }
        roleScrenBttnHistRepository.saveAll(roleScrenBttnHistList);
    }

    @Override
    public RoleScrenBttn createRoleScrenBttn(RoleScrenBttn roleScrenBttn) {
        RoleScrenBttn entity = null;
        RoleScrenBttnPK pk = new RoleScrenBttnPK(roleScrenBttn.getScrenId()
                                                , roleScrenBttn.getBttnId()
                                                , roleScrenBttn.getUserRoleId());

        if (roleScrenBttnRepository.existsById(pk)) {
            return null;
            // roleScrenBttnRepository.deleteById(pk);
        }

        entity = roleScrenBttnRepository.save(roleScrenBttn);

        // redis 에도 적용
        // 1. redis에 있는지 확인
        Optional<RoleBasedApi> oRoleBasedApi = roleBasedApiRepository.findByUserRoleIdAndScrenIdAndBttnId(roleScrenBttn.getUserRoleId()
                                                                                                        , roleScrenBttn.getScrenId()
                                                                                                        , roleScrenBttn.getBttnId());
        // 2. redis에 존재하면 return
        if (oRoleBasedApi.isPresent()) return entity;

        // 3. 버튼 정보 찾기
        BttnDto btn = roleScrenBttnRepository.findBttnInfo(roleScrenBttn.getScrenId(), roleScrenBttn.getBttnId());

        // 4. 버튼이 없으면 return
        if (btn == null)    return entity;
        if (btn.getApiId() == null || 0  == btn.getApiId())   return entity;

        // 5. onm 에서 api 정보 찾기
        Map<String, String> apiInfo = commonClient.findApiInfo(btn.getApiId());
        if (apiInfo == null)    return entity;

        // 6. redis에 저장
        RoleBasedApi roleBasedApi = new RoleBasedApi();
        roleBasedApi.setUserRoleId(roleScrenBttn.getUserRoleId());
        roleBasedApi.setScrenId(roleScrenBttn.getScrenId());
        roleBasedApi.setBttnId(roleScrenBttn.getBttnId());
        roleBasedApi.setApiId(Integer.parseInt(apiInfo.get("apiId")));
        roleBasedApi.setHttMethodVal(apiInfo.get("httMethodVal"));
        roleBasedApi.setApiLocUrladdr(apiInfo.get("apiLocUrladdr"));
        roleBasedApiRepository.save(roleBasedApi);

        return entity;
    }

    @Override
    public RoleScrenBttn deleteRoleScrenBttn(RoleScrenBttn roleScrenBttn) {
        RoleScrenBttnPK pk = new RoleScrenBttnPK(roleScrenBttn.getScrenId()
                                                , roleScrenBttn.getBttnId()
                                                , roleScrenBttn.getUserRoleId());
        if (roleScrenBttnRepository.existsById(pk)) {
            roleScrenBttnRepository.deleteById(pk);

            // redis 에도 적용
            // 1. redis에 있는지 확인
            Optional<RoleBasedApi> oRoleBasedApi = roleBasedApiRepository.findByUserRoleIdAndScrenIdAndBttnId(roleScrenBttn.getUserRoleId()
                                                                        , roleScrenBttn.getScrenId()
                                                                        , roleScrenBttn.getBttnId());
            // 2. redis에 있으면 삭제
            if (oRoleBasedApi.isPresent()) {
                log.debug("roleBasedApi : " + oRoleBasedApi.get());
                roleBasedApiRepository.deleteById(oRoleBasedApi.get().getId());
            }
            return roleScrenBttn;
        } else {
            return null;
        }
    }

    @Override
    public List<RoleDto> findMenuRole(String menuId){
        return roleMenuRepository.findByMenuId(menuId);
    }

    @Override
    public List<RoleDto> findScrenRole(String screnId){
        return roleMenuRepository.findByScrenId(screnId);
    }

    @Override
    public List<UserRoleDeptDto> findMappedDept(String deptOrTeamNm) {

        if (deptOrTeamNm == null) deptOrTeamNm = "";

        List<UserRoleDeptDto> result;
        List<UserRoleDeptMappingIDto> userRoleDeptDtoList = userRoleDeptRepository.findMappedDept(deptOrTeamNm);

        result = userRoleDeptDtoList.stream().map(UserRoleDeptDto::new).collect(Collectors.toList());

        return result;
    }

    @Override
    public List<RoleDto> findMappedRole(String roleDeptTeamCd, String roleMappReofoCd) {
        return userRoleDeptRepository.findMappedRole(roleDeptTeamCd, roleMappReofoCd);
    }


    @Override
    public void createUserRoleDept(List<UserRoleDept> userRoleDeptList) {
        for (UserRoleDept userRoleDept : userRoleDeptList) {
            userRoleDept.setAccntCratAutoYn("Y");
        }
        userRoleDeptRepository.saveAll(userRoleDeptList);
    }

    @Override
    public void roleMappingDataExcelDownload(String roleDeptTeamClCd) {
        List<ExcelDto> excelDto = new ArrayList<>();
        ExcelHeaderDto header = new ExcelHeaderDto();
        List<ExcelMergeDto> excelMergeDto = new ArrayList<>();
        ExcelBodyDto body = new ExcelBodyDto();

        String roleDeptTeamClNm = "부서";
        String[] arrayHeaderDeptNm = {"구분코드","구분명","부서코드","부서명","직책코드","직책명","역할ID","역할명","사용여부","권한부여여부"};
        String[] arrayHeaderTeamNm = {"구분코드","구분명","고객센터팀코드","고객센터팀명","직위코드","직위명","역할ID","역할명","사용여부","권한부여여부"};

        String[] arrayHeaderNm = arrayHeaderDeptNm;
        if ("2".equals(roleDeptTeamClCd)) {
            roleDeptTeamClNm = "고객센터팀";
            arrayHeaderNm = arrayHeaderTeamNm;
        }

        String fileName = "역할기준매핑_" + roleDeptTeamClNm;
        //시트명 세팅
        String sheetName = "역할기준매핑";

        // 헤더 세팅 Row 별 column merge

        // 헤더 길이
        header.setHLength(arrayHeaderNm.length);

        // 헤더 List 생성
        List<String[]> headerList = new ArrayList<>();
        headerList.add(arrayHeaderNm);

        // 헤더 List set
        header.setHeaderNm(headerList);

        // 헤더 Merge 리스트
        header.setHeaderMerge(excelMergeDto);

        // body 데이터 영문명
        String[] arrayBodyColNm = {"roleDeptTeamClCd","roleDeptTeamClNm","roleDeptTeamCd","roleDeptTeamNm","roleMappReofoCd",
                "roleMappReofoNm", "userRoleId", "userRoleNm", "useYn", "authYn"};
        List<String> bodyColNm = Arrays.asList(arrayBodyColNm);

        HashMap<String, String> map = new HashMap<>();

        List<UserRoleDeptMappingIDto> userRoleDeptMappingIDtoList = userRoleDeptRepository.findMappedDeptRole(roleDeptTeamClCd);

        body.setBodyColNm(bodyColNm);
        body.setBody(userRoleDeptMappingIDtoList);

        excelDto.add(new ExcelDto(sheetName, header, body));

        try {
            ExcelUtil.excelDownload(excelDto, fileName);
        } catch (Exception e) {
            throw new ServiceException("COM.I0034");
        }

    }

    @Override
    public void roleMappingFormatExcelDownload() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<ExcelDto> excelDto = new ArrayList<>();
        ExcelHeaderDto header = new ExcelHeaderDto();
        List<ExcelMergeDto> excelMergeDto = new ArrayList<>();
        ExcelBodyDto body = new ExcelBodyDto();

        String fileName = "역할기준매핑_등록양식";
        //시트명 세팅
        String sheetName = "통합조회";

        // 헤더 세팅 Row 별 column merge
        String[] arrayHeaderNm = {"구분코드","구분(부서/고객센터팀)","부서/고객센터팀코드","부서/고객센터팀명","직책/직위코드","직책/직위명","역할ID","역할명","사용여부","역할사용여부"};

        // 헤더 길이
        header.setHLength(arrayHeaderNm.length);

        // 헤더 List 생성
        List<String[]> headerList = new ArrayList<>();
        headerList.add(arrayHeaderNm);

        // 헤더 List set
        header.setHeaderNm(headerList);

        // 헤더 Merge 리스트
        header.setHeaderMerge(excelMergeDto);

        // body 데이터 영문명
        String[] arrayBodyColNm = {"roleDeptTeamClCd","roleDeptTeamClNm","roleDeptTeamCd","roleDeptTeamNm","roleMappReofoCd",
                                   "roleMappReofoNm", "userRoleId", "userRoleNm", "useYn", "authYn"};
        List<String> bodyColNm = Arrays.asList(arrayBodyColNm);

        List<Object> obj = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();

        map.put("roleDeptTeamClCd","2");
        map.put("roleDeptTeamClNm","고객센터팀");
        map.put("roleDeptTeamCd","200");
        map.put("roleDeptTeamNm","고객서비스팀");
        map.put("roleMappReofoCd","300");
        map.put("roleMappReofoNm","팀원");
        map.put("userRoleId","Z001");
        map.put("userRoleNm","기본사용자");
        map.put("useYn","Y");
        map.put("authYn","Y");
        obj.add(map);

        map.put("roleDeptTeamClCd","1");
        map.put("roleDeptTeamClNm","부서");
        map.put("roleDeptTeamCd","170100");
        map.put("roleDeptTeamNm","서귀포지사");
        map.put("roleMappReofoCd","300");
        map.put("roleMappReofoNm","팀원");
        map.put("userRoleId","Z001");
        map.put("userRoleNm","기본사용자");
        map.put("useYn","Y");
        map.put("authYn","Y");
        obj.add(map);

        body.setBodyColNm(bodyColNm);
        body.setBody(obj);

        excelDto.add(new ExcelDto(sheetName, header, body));

        ExcelUtil.excelDownload(excelDto, fileName);
    }

    @Override
    public  List<UserRoleDeptMappingDto> uploadDeptAndRole(MultipartFile file) throws IOException {
        List<ExcelDto> excelDtoList = ExcelUtil.excelUpload(file);
        List<UserRoleDeptMappingDto> result = new ArrayList<>();
        try {
            List<List<String>> list = excelDtoList.get(0).getBody().getBody();

            // 엑셀양식 순서 : 구분코드, 구분(부서/고객센터팀), 부서코드/고객센터팀코드, 부서명/고객센터팀명, 직책코드/직위코드, 직책명/직위명, 역할ID, 역할명, 역할사용여부
            result = list.stream().map(item -> new UserRoleDeptMappingDto(item.get(0),
                    item.get(1),
                    item.get(2),
                    item.get(3),
                    item.get(4),
                    item.get(5),
                    item.get(6),
                    item.get(7),
                    item.get(8),
                    item.get(9))).collect(Collectors.toList());

        } catch (Exception e) {
            throw new ServiceException("COM.I1045");
        }
        return result;
    }

    @Override
    public void saveDeptAndRole(List<UserRoleDeptMappingDto> userRoleDeptMappingDto) {

        List<UserRoleDeptMapping> cList = new ArrayList<>();
        List<UserRoleDeptMapping> dList = new ArrayList<>();
        List<UserRoleDept> userRoleDeptList = new ArrayList<>();
        List<UserRoleDeptMappingHisDto> hisList = new ArrayList<>();

        try {
            for (UserRoleDeptMappingDto item : userRoleDeptMappingDto) {
                // 부서 기준 사용여부
                UserRoleDept userRoleDept = new UserRoleDept(item.getRoleDeptTeamCd(),
                        item.getRoleMappReofoCd(),
                        item.getRoleDeptTeamClCd(),
                        // AccntCratAutoYn 값을 현재는 사용하지 않기 때문에 useYn과 동일하게 처리
                        item.getAccntCratAutoYn() == null ? item.getUseYn() : item.getAccntCratAutoYn(),
                        item.getUseYn());
                // 중복입력방지
                if (!userRoleDeptList.contains(userRoleDept)) {
                    userRoleDeptList.add(userRoleDept);
                }

                boolean flag = true;
                String crudClCd = "";
                UserRoleDeptMappingPK pk = new UserRoleDeptMappingPK(item.getRoleDeptTeamCd(), item.getRoleMappReofoCd(), item.getUserRoleId());
                UserRoleDeptMapping userRoleDeptMapping = new UserRoleDeptMapping(pk);

                if (!userRoleDeptMappingRepository.existsById(pk)) {
                    if ("Y".equals(item.getAuthYn())) {
                        cList.add(userRoleDeptMapping);
                        crudClCd = "C";
                    } else {
                        flag = false;
                    }
                } else {
                    if ("Y".equals(item.getAuthYn())) {
                        cList.add(userRoleDeptMapping);
                        crudClCd = "U";
                    } else {
                        dList.add(userRoleDeptMapping);
                        crudClCd = "D";
                    }
                }

                if (flag) {
                    hisList.add(new UserRoleDeptMappingHisDto(item.getRoleDeptTeamCd(),
                            item.getRoleMappReofoCd(),
                            item.getUserRoleId(),
                            crudClCd,
                            item.getAthrtyReqstSeq(),
                            item.getChngResonCntnt()));
                }
            }
            saveUserRoleDeptMapping(userRoleDeptList, cList, dList, hisList);
        } catch (Exception e) {
            throw new ServiceException("COM.E0001");
        }
    }

    @Override
    public void modifyRoleDeptList(List<UserDeptRoleDto> userDeptRoleDto) {
        List<UserRoleDept> userRoleDeptList = new ArrayList<>();
        List<UserRoleDeptMapping> cList = new ArrayList<>();
        List<UserRoleDeptMapping> dList = new ArrayList<>();
        List<UserRoleDeptMappingHisDto> hisList = new ArrayList<>();

        try {
            for (UserDeptRoleDto item : userDeptRoleDto) {

                UserRoleDeptDto userRoleDeptDto = item.getUserRoleDeptDto();
                // AccntCratAutoYn 값을 현재는 사용하지 않기 때문에 useYn과 동일하게 처리
                userRoleDeptDto.setAccntCratAutoYn(userRoleDeptDto.getUseYn());
                userRoleDeptList.add(userRoleDeptDto.toEntity());

                String roleDeptTeamCd = userRoleDeptDto.getRoleDeptTeamCd();
                String roleMappReofoCd = userRoleDeptDto.getRoleMappReofoCd();

                for (UserRoleDeptMappingDto mappingDto : item.getUserRoleDeptMappingDtoList()) {
                    String crudClCd = "";
                    boolean flag = true;
                    UserRoleDeptMappingPK pk = new UserRoleDeptMappingPK(roleDeptTeamCd, roleMappReofoCd, mappingDto.getUserRoleId());
                    UserRoleDeptMapping userRoleDeptMapping = new UserRoleDeptMapping(pk);

                    if (!userRoleDeptMappingRepository.existsById(pk)) {
                        if ("Y".equals(mappingDto.getAuthYn())) {
                            cList.add(userRoleDeptMapping);
                            crudClCd = "C";
                        } else {
                            flag = false;
                        }
                    } else {
                        if ("Y".equals(mappingDto.getAuthYn())) {
                            cList.add(userRoleDeptMapping);
                            crudClCd = "U";
                        } else {
                            dList.add(userRoleDeptMapping);
                            crudClCd = "D";
                        }
                    }
                    if (flag) {
                        hisList.add(new UserRoleDeptMappingHisDto(userRoleDeptDto.getRoleDeptTeamCd(),
                                userRoleDeptDto.getRoleMappReofoCd(),
                                mappingDto.getUserRoleId(),
                                crudClCd,
                                mappingDto.getAthrtyReqstSeq(),
                                mappingDto.getChngResonCntnt()));
                    }
                }
            }
            saveUserRoleDeptMapping(userRoleDeptList, cList, dList, hisList);
        } catch (Exception e) {
            throw new ServiceException("COM.E0001");
        }
    }
    public void saveUserRoleDeptMapping(List<UserRoleDept> userRoleDeptList,
                                        List<UserRoleDeptMapping> cList,
                                        List<UserRoleDeptMapping> dList,
                                        List<UserRoleDeptMappingHisDto> hisList) throws UnknownHostException {
        if (!userRoleDeptList.isEmpty()) {
            userRoleDeptRepository.saveAll(userRoleDeptList);

            if (!cList.isEmpty()) {
                userRoleDeptMappingRepository.saveAll(cList);
            }
            if (!dList.isEmpty()) {
                for (UserRoleDeptMapping item : dList) {
                    userRoleDeptMappingRepository.deleteById(new UserRoleDeptMappingPK(item.getRoleDeptTeamCd()
                            , item.getRoleMappReofoCd()
                            , item.getUserRoleId()));
                }
            }
            if (!hisList.isEmpty()) {
                saveUserRoleDeptMappingHis(hisList);
            }
        }
    }

    public void saveUserRoleDeptMappingHis(List<UserRoleDeptMappingHisDto> hisList) throws UnknownHostException {
        List<UserRoleDeptMappingHis> userRoleDeptMappingHisList = new ArrayList<>();

        for(UserRoleDeptMappingHisDto item : hisList) {
            userRoleDeptMappingHisList.add(new UserRoleDeptMappingHis(item.getRoleDeptTeamCd(),
                                                                    item.getRoleMappReofoCd(),
                                                                    item.getUserRoleId(),
                                                                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
                                                                    item.getCrudClCd(),
                                                                    RequestUtil.getClientIP(),
                                                                    item.getAthrtyReqstSeq(),
                                                                    item.getChngResonCntnt()));
        }

        userRoleDeptMappingHisRepository.saveAll(userRoleDeptMappingHisList);
    }

    @Override
    public List<RoleDto> searchRoleAuthByUserid(String userid) {
        return roleRepository.searchRoleAuthByUserid(userid);
    }

    @Override
    public List<RoleDto> searchReqRoleAuthByAthrtyReqstSeq( Integer athrtyReqstSeq) {
        return roleRepository.searchReqRoleAuthByAthrtyReqstSeq(athrtyReqstSeq);
    }

    @Override
    public String checkPriAuth(String clientIp, String screnId, String dwnldResonCntnt) throws UnknownHostException {

        String authYn = "N";
        String roleYn = "N";
        String ipYn = "N";

        // IP 정보를 안넘겨주면 return "N"
        if (clientIp == null || "".equals(clientIp)) return authYn;

        // 1. 역할 체크 --> 로그인 사용자 ID 체크 && 권한 C004 여부 체크
        String userid = RequestUtil.getLoginUserid();
        // 계정이 없을 경우 return "N"
        if ("".equals(userid) || "System".equals(userid)) return authYn;

        List<RoleDto> roleList = roleRepository.findUserRole(userid);

        // 역할이 없을 경우 return "N"
        if (roleList == null || roleList.isEmpty()) return authYn;

        for (RoleDto roleDto : roleList) {
            // C004(개인정보) 역할이 있을 경우에만 "Y" 로 변경
            if(AuthType.AUTH_INDIV.getCode().equals(roleDto.getUserRoleId())){
                roleYn = "Y";
                break;
            }
        }

        // 2. IP 체크
        Optional<Account> oAccount = accountRepository.findByUserid(userid);
        // 사용자정보가 없을 경우 return "N"
        if (oAccount.isEmpty()) return authYn;

        String ip = oAccount.get().getUserIpaddr();

        // ip정보가 안들어 있을 경우 return "N"
        if (ip == null || "".equals(ip)) return authYn;

        // 전달받은 IP와 등록되어 있는 IP가 일치할 경우에만 "Y" 로 변경
        if (clientIp.equals(ip)) {
            ipYn = "Y";
        }
        // 역할권한 / IP 모두 일치할 경우에만 Y 리턴
        if ("Y".equals(roleYn) && "Y".equals(ipYn)) {
            authYn = "Y";

            userActvyLogServiceImpl.regUserActvyLog(UserActvyLogDto.builder()
                    .userid(userid)
                    .userActvyTypeCd("4")
                    .systmCtgryCd("ARG")
                    .screnId(screnId)
                    .lastChngrId(userid)
                    .connIpaddr(clientIp)
                    .dwnldResonCntnt(dwnldResonCntnt)
                    .build());
        }

        return authYn;
    }
}
