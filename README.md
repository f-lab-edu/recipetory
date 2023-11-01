# 🥘 RECIPETORY
**Recipe**(레시피)와 Reposi**tory**(리포지토리)의 합성어로, 회원들이 레시피를 공유하고
리뷰와 댓글을 통해 소통할 수 있는 레시피 공유 사이트입니다. 기본적인 소셜 인증, 컨텐츠에 대한 CRUD,
알림 기능 등이 존재합니다.

- 자바 버전 : JAVA 17, Spring Boot 3.1.2
- 서버 스펙 : vCPU 2코어, 1GB 메모리(GCP e2-micro) Scale-out 구성
- 가용 TPS : 600

<br>

# 🎯 프로젝트 목표

- 단순히 구현이 아닌 부하 테스트를 통해 트래픽이 몰리는 상황에서도 **안정적인 서비스를 제공**하는 것을 목표
- RESTful한 API 설계
- git flow 전략[(참고)](https://techblog.woowahan.com/2553/)을 활용한 브랜치 관리
- **객체지향**적인 설계와 테스트하기 좋은 코드를 달성
- 각 도메인 기능에 대한 단위 **테스트 코드**를 작성
- 기획 및 설계부터 클라우드 서버 배포 자동화까지 어플리케이션 서비스가 제작되는 일련의 과정 실습
- UI와 자바스크립트 API 호출 코드를 직접 작성하여 사용자와 협업자 관점에서 서버 코드 이용

<br>

# ❗️주요 기술적 이슈

- [알림 처리를 위한 ApplicationEventPublisher와 커넥션 데드락 문제](https://buchu-doodle.tistory.com/212)
- [검색 고도화를 위한 Elastic Search 사용 (1) : Kafka 인덱싱 비동기 처리](https://buchu-doodle.tistory.com/213)
- [검색 고도화를 위한 Elastic Search 사용 (2) : ElasticSearchClient 쿼리 작성](https://buchu-doodle.tistory.com/214)
- [Artillery를 이용한 부하 테스트](https://buchu-doodle.tistory.com/210)
- [다중 서버를 통한 TPS 개선 : 세션 인증 정보가 풀리는 문제](https://buchu-doodle.tistory.com/216)
- [<단위 테스트>를 읽고 : 과연 테스트 코드가 잘 작성되었다고 할 수 있을까?](https://buchu-doodle.tistory.com/227)
- [인덱스 페이지 구성하기 : ES 스크립트 작성과 캐싱을 이용한 성능 개선](https://buchu-doodle.tistory.com/215)
- [Jenkins와 Docker를 이용한 CI/CD](https://buchu-doodle.tistory.com/211)

<br>

# 📃 프로젝트 구조

### ⚙️ CI/CD 파이프라인 : 클라우드 서버로 GCP와 Vultr를 이용했습니다.
![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcwnaGb%2Fbtsv7bKJrIc%2FrMJcBNKkUEUW3Er9KrUD5k%2Fimg.png)

### 👩‍💻 사용자 요청 흐름
![image](https://github.com/f-lab-edu/recipetory/assets/69233405/a40e0a2b-1cb2-4ddb-b213-bcde8845b4f2)

### 📮 컨텐츠 post시 시퀀스 다이어그램
<img width="850" alt="image" src="https://github.com/f-lab-edu/recipetory/assets/69233405/48625317-0db0-4ce4-9f25-3c297fe3ed9d">



<br>


# 💾 프로젝트 ERD
![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbq3aCN%2FbtsxgbPAa4r%2F2q34ZuNwFYzk7PcybdpCtk%2Fimg.png)

- 레시피와 연관 관계를 맺는 재료(M:N), 태그(1:N), 단계(1:N), 리뷰와 댓글(1:N)
- 유저와 연관 관계를 맺는 레시피(1:N), 팔로우 관계(M:N), 알림(M:N), 리뷰와 댓글(1:N)

<br>

# 🎆 프론트 페이지
![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcxjF2i%2Fbtsxq1k7nMG%2FU7IBhVeIsOvOsNDkK9BTXk%2Fimg.png)
![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbXU8d8%2Fbtsxq34Sl8k%2FCbA61zBvMsSNdRmpLk8tM0%2Fimg.png)
![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbttNVn%2FbtsxrYWAmuO%2FaDkBcjn7Svmi6qjkMIzxfk%2Fimg.png)
![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbFbj49%2FbtsxtnhlNn0%2FwuNDv4tsVY1bkvc65rskH1%2Fimg.png)

- api 요청에 `axios`, UI 구성을 위해 `bootstrap`을 사용했습니다.
