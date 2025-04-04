# SKCC KMS AI - RA-JAVA-FRAMEWORK-COMMON

## 소개

[RA-JAVA-FRAMEWORK-COMMON]에 오신 것을 환영합니다! 이 프로젝트 문서는 프로젝트 설정을 안내하여 빠르게 시작할 수 있도록 도와드립니다.
이 프로젝트는 공통 관리를 위한 다음과 같은 기능들을 포함하고 있습니다:
- 메뉴관리
  - 트리구조 기반 메뉴 관리 및 기본적인 메뉴 설정 제공
  - 메인/팝업 으로 분류된 화면 정보를 관리, 화면은 버튼과 매핑
- API관리
  - 시스템 호출 API 기본정보 관리
  - 호출된 API에 대한 사용이력 관리
- 기타
  - 코드 관리 / 파일 관리 / 암,복호화 / 마스킹

## 사전 요구사항

- JDK 17 이상
- Lombok
- Docker 및 Docker Compose
- Gradle 8.3 (또는 포함된 Gradle wrapper 사용)

## 프로젝트 구조

프로젝트는 다음 모듈로 구성됩니다:

- `common-service` - 메뉴관리 / 코드관리 / 파일관리 / 기타
- `common-export` - 공통 유틸리티 및 공유 컴포넌트

## 설치 및 설정

1. 저장소 복제:

```bash
git clone <repository-url>
cd common
```

2. Docker Compose를 사용하여 필요한 디팬던시즈 시작:

```bash
# 서비스 시작: zookeeper, kafka, kafka-ui, redis, mysql
docker-compose -f docker-compose.yml up -d
```

3. Start KeyCloak:

```bash
# KeyCloak 시작
docker-compose -f docker-compose-keycloak.yml up -d
```

4. 데이터베이스 초기화:

> [!NOTE]
> Mysql보다 H2 database를 사용하려고 하시면 docker-compose.yml 파일에서 Mysql 서비스를 주석처리 하셔야 됩니다.
> 그 다음은 이 스탭을 무시하고 "H2 데이터베이스로 애플리케이션 실행하기"라는 부분으로 진행하시면 됩니다.
>
> ![mysql-service.png](docs/imgs/mysql-service.png)

```bash
# 컨테이너 목록 확인
  docker ps

# MySQL 컨테이너 실행
  docker exec -it mysql-common bash

# MySQL 로그인
mysql -u root -p

# MySQL 비밀번호 (qwer1234) 입력

# 데이터베이스 생성
CREATE DATABASE OCO;

# 계정 생성
CREATE USER 'com_dev'@'%' IDENTIFIED BY 'qwer1234!';

# 권한 부여
GRANT ALL PRIVILEGES ON OCO.* TO 'com_dev'@'%’;

FLUSH PRIVILEGES;
```

5. IDE로 연결

MySQL Workbench나 MySQL을 지원하는 다른 IDE를 사용하여 MySQL 컨테이너에 연결할 수 있습니다.
설정 단계는 다음과 같습니다:

- MySQL Workbench 또는 사용하려는 IDE를 엽니다.
- 다음 정보로 새 연결을 설정합니다:
  - **호스트명**: 127.0.0.1 (localhost)
  - **포트**: 3306
  - **데이터 베이스**: OCO
  - **사용자이름**: com_dev
  - **비밀번호**: qwer1234!
- **연결 테스트**: MySQL 서버에 접속할 수 있는지 확인합니다.
  필요한 경우 'allowPublicKeyRetrieval=true' 설정을 해야 합니다.

연결이 성공했으면 IDE의 그래픽 인터페이스를 사용하여 데이터베이스를 관리하고, SQL 쿼리를 실행하며, 데이터와 더 쉽게 상호작용을 할 수 있습니다.

DB 및 사용자 생성 완료 후 2개의 sql 파일 (README의 같은 폴더에 있는 query.sql, oif_query.sql, menu.sql)을 실행하셔여야 합니다.

먼저, query.sql 파일의 스크립 명령을 실행하여 테이블을 생성합니다.

다음은 oif_query.sql 파일의 스크립 명령을 실행하여 매뉴 구성을 위한 샘플 데이터를 추가합니다.

다음은 menu.sql 파일의 스크립 명령을 실행하여 매뉴 구성을 위한 샘플 데이터를 추가합니다.

6. 프로젝트 빌드:

```bash
./gradlew clean build
```

> [!NOTE]
>
> `./gradlew: Permission denied` 오류가 발생할 경우 다음 명령들 중에 하나를 실행해 보시면 됩니다.
>  -``sudo chmod +x ./gradlew``
>
>  -``git update-index --chmod=+x gradlew``


7. Swagger 활성화:

application.yml에서 이 줄을 ```enabled: false```에 주석을 달거나 ```enabled: true```로 변경하세요(개발에만 해당).

```code
springdoc:
  api-docs:
    ...
    enabled: false
  swagger-ui:
    ...
    enabled: false
```


## 애플리케이션 실행

1. 서비스 시작:

```bash
./gradlew :common-service:bootRun
```

> [!NOTE]
>
> IntelliJ, Eclipse 등과 같은 IDE로 프로젝트를 실행할 수도 있습니다.
> 해당 프로젝트 시작 시 오류가 발생할 경우는 프로젝트 구성을 확인해야 합니다.
> - JDK 버전
> - Gradle 구성
> - 프로시 차단
> - '.gradle' 폴더를 프로젝트에서 제거하여 2번 스탭 (프로젝트 빌드)로 돌아가서 gradle를 다시 빌드하면 됩니다.

2. 서비스는 `http://localhost:9100/api/com/common/swagger-ui/index.html` 에서 사용 가능합니다
   ![swager.png](docs/imgs/swagger.png)

## H2 데이터베이스로 애플리케이션 실행하기

1. 서비스 시작:

```bash
./gradlew :common-service:bootRun -Pprofile=test
```

2. H2 콘솔 http://localhost:9100/api/h2-console
- H2 콘솔 로그인 설정:
  - **Saved Settings**: Generic H2 (Embedded)
  - **Setting Name**: Generic H2 (Embedded)
  - **Driver Class**: org.h2.Driver
  - **JDBC URL**: jdbc:h2:mem:common
  - **User Name**: sa
  - **Password**:
  - ![h2db.png](docs/imgs/h2db.png)

## 개발

- `./gradlew build`로 모든 모듈 빌드
- `./gradlew test`로 테스트 실행
- `./gradlew bootRun`으로 서비스 로컬 실행

## 데이터베이스 설정

프로젝트에는 초기 설정을 위한 SQL 스크립트가 포함되어 있습니다:

- `query.sql` - 데이터베이스 덤프
- `oif_query.sql` - 데이터베이스 덤프
- `menu.sql` - 메뉴 관련 데이터

![logic-erd.png](docs/imgs/logic-erd.png)

물리 ERD
![physic-erd.png](docs/imgs/physic-erd.png)


## 관련 저장소

- [ra-java-framework-account](https://github.com/skccmygit/ra-java-framework-account)
- [ra-java-framework-work-batch](https://github.com/skccmygit/ra-java-framework-work-batch)
- [ra-java-api-gateway](https://github.com/skccmygit/ra-java-framework-work-batch)

## 추가 리소스

- 자세한 정보는 README.md 파일 참조
- 개별 모듈 문서 참조
- 인프라 설정은 docker-compose 파일 참조

## 문제 해결

- 데이터베이스 연결 문제 발생 시 데이터베이스 컨테이너가 실행 중인지 확인
- IDE의 Console에 나와 있는 오류 로그를 통해 상세 오류 메시지를 확인하거나 프로젝트 폴더에 생성된 파일을 확인합니다.
