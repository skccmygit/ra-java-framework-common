# SKCC KMS AI - RA-JAVA-FRAMEWORK-COMMON

## 소개

[RA-JAVA-FRAMEWORK-COMMON]에 오신 것을 환영합니다! 이 프로젝트 문서는 프로젝트 설정을 안내하여 빠르게 시작할 수 있도록 도와드립니다.
이 프로젝트는 공통 관리를 위한 다음과 같은 기능들을 포함하고 있습니다:
- 메뉴관리
  - 트리구조 기반 메뉴 관리 및 기본적인 메뉴 설정 제공
  - 메인/팝업 으로 분류된 화면 정보를 관리, 화면은 버튼과 매핑
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

```bash
# 컨테이너 목록 확인
  docker ps

# MySQL 컨테이너 실행 - <container_id> 값을 실제 ID로 변경	
  docker exec -it <container_id> bash

# MySQL 로그인
mysql -u root -p

# MySQL 비밀번호 입력

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

프로젝트 빌드:

```bash
./gradlew clean build
```

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

2. 서비스는 `http://localhost:9100/api/com/common/swagger-ui/index.html` 에서 사용 가능합니다
   ![swager.png](docs/imgs/swagger.png)

## 개발

- `./gradlew build`로 모든 모듈 빌드
- `./gradlew test`로 테스트 실행
- `./gradlew bootRun`으로 서비스 로컬 실행

## 데이터베이스 설정

프로젝트에는 초기 설정을 위한 SQL 스크립트가 포함되어 있습니다:

- `query.sql` - 데이터베이스 덤프
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
- 자세한 오류 메시지는 debug.log 확인