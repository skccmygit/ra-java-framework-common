package kr.co.skcc.base.com.common.domain.apiInfo.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppType {
    A("API"),
    B("배치"),
    I("연동");

    private final String name;
}
