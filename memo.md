
현재 서버의 물리적 실체는 본가에 둔 컴퓨터본체인데; 24시간 돌아가니 신경쓰인다는 아빠의 의견을 반영하여 쓸 때만 WOL을 이용하여 켜고 평소에는 꺼둔다.

## 작업환경

- STS
- VSCode
- Git
- Sourcetree

깔아

- 자바 21
- node.js
- postgresql
- maven




## Github 올라간 것 말고

루트 디렉토리의 `.gitignore` 파일을 보라.

##### DB 설정

운영: 사용자 만들고 백엔드 설정에 추가

##### AWS S3 키

AWS IAM 액세스 키를 여기 추가

백엔드 `xyz.arinmandri.playground.MyDeepestSecret.java` (gitignore에 있음)

```
public static final String AWS_ACCESS_KEY_ID = "...";
public static final String AWS_SECRET_ACCESS_KEY = "...";
```






## 실행모드

두 가지 모드가 있다.

- `lodev`: 개발
- `prod`: 운영

### 프론트엔드

* 환경변수: 프론트엔드 루트에 `.env.모드이름` 파일이 있어서 여기에 실행모드마다 다른 설정값(환경변수)이 있다. 환경변수는 `VITE_`로 시작해야 한다.
* 실행/빌드: `package.json`에서 스크립트를 보자. `dev`는 `vite --mode lodev`로 개발 모드로 실행된다. `build`는 `build-only`를 실행하는데 여기서 `vite build --mode prod` 이렇게 운영모드로 빌드한다.

### 백엔드

* Maven 프로필: `pom.xml`
* 모드별 설정파일: `application-모드이름.properties` (현재 없는데)
* DB 설정 차이 없음. 똑같이 로컬 위치에서 DB에서 데이터 뽑아서 줌.









## 개발

### 소스 위치

전체 소스를 한 git 저장소로 관리한다.

* 프론트엔드: `front/`
* 백엔드: `back/`
* DB: `dbsetting.sql`

### 실행

##### 프론트엔드

프론트엔드 루트 디렉토리에서

```
npm run dev
npm run dev --mode lodev # 모드 지정하는 법
```


##### 백엔드

STS4에서: 그 프로젝트 우클릭 > Run As > Spring Boot App

윈도우 CMD에서 JAR 실행
```
# 프로젝트 루트 디렉토리에서
java -jar .\back\target\playground-0.0.1-SNAPSHOT.jar > .\back.log
```
```
# 백엔드 디렉토리에서
mvn clean package
java -jar .\target\playground-0.0.1-SNAPSHOT.jar > ..\back.log
```







## 업데이트

1. 작업한 걸 커밋하고 깃허브에 푸시한다.
2. 서버에서 `~/playground` 위치에서 관리중.
3. 설정 등 git에서 제외한 변경사항은 직접 갖다넣기.
4. `update.sh` 를 실행한다.





## 서버 확인

##### 백엔드

로그 파일 `back.log`

##### ...

express 메모

```
pm2 start back/server.js --name playground-backend  # PM2로 백엔드 실행
pm2 stop playground-backend
pm2 restart playground-backend
pm2 list  # PM2 실행 현황 보기
tail -n1000 -f ~/.pm2/logs/playground-backend-out.log  # PM2 백엔드 로그 확인
tail -n1000 -f ~/.pm2/logs/playground-backend-error.log  # PM2 백엔드 에러로그 확인
```
