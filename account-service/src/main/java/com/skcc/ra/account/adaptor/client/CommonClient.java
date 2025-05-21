package com.skcc.ra.account.adaptor.client;

import com.skcc.ra.common.api.dto.domainDto.*;
import com.skcc.ra.common.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

@FeignClient(name = "common-service", url = "${feign.common-service.url:http://localhost:9100}", configuration = {FeignConfig.class})
public interface CommonClient {
    @GetMapping("/api/v1/com/common/dept/deptcd")
    DeptDto searchDeptcd(@RequestParam String deptcd);

    @GetMapping("/api/v1/com/common/userBasic/teamLeader")
    UserBasicDto searchTeamLeader(@RequestParam(required = false, value = "deptcd") String deptcd,
                                  @RequestParam(required = false, value = "empno") String empno);

    @GetMapping("/api/v1/com/common/cmmnCd/dtl")
    List<CmmnCdDtlDto> findByCmmnCd(@RequestParam String cmmnCd);

    @GetMapping("/api/v1/com/common/menu/scren/dtl")
    ScrenDto findByScrenId(@RequestParam String screnId);

    @GetMapping("/api/v1/com/common/menu/dtl")
    MenuDto findByMenuId(@RequestParam String menuId);

    @GetMapping("/api/v1/com/common/userBasic")
    List<UserBasicDto> searchUserBasic(@RequestParam(required = false, value = "userNm") String userNm,
                                       @RequestParam(required = false, value = "deptcdList") List<String> deptcdList,
                                       @RequestParam(required = false, value = "empno") String empno);

    @GetMapping("/api/v1/com/common/menu/scren/use")
    Page<ScrenDto> findUseScren(@RequestParam(required = false, value = "chrgTaskGroupCd") String chrgTaskGroupCd,
                                @RequestParam(required = false, value = "screnClCd") String screnClCd,
                                @RequestParam(required = false, value = "screnNm") String screnNm,
                                @RequestParam(required = false, value = "isUnpaged") String isUnpaged);

    @GetMapping("/api/v1/com/common/apiMgt/apiId")
    HashMap<String,String> findApiInfo(@RequestParam Integer apiId);
}

