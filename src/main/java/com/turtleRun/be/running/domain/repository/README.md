# Running Domain Repository (DDD Pattern)

이 디렉토리는 러닝 도메인의 Repository 패턴을 DDD(Domain-Driven Design) 원칙에 따라 구현한 코드들을 포함합니다.

## 구조

### Repository 인터페이스

#### 1. RunningSessionRepository
- **목적**: 러닝 세션의 영속성 관리
- **주요 기능**:
  - 플레이어별 러닝 세션 조회
  - 기간별 러닝 세션 조회
  - 거리/지속시간 기반 필터링
  - 페이스/속도 기반 정렬
  - 플레이어별 통계 집계

#### 2. RunningHeartRateRepository
- **목적**: 심박수 데이터의 영속성 관리
- **주요 기능**:
  - 세션별 심박수 데이터 조회
  - 시간 범위별 심박수 데이터 조회
  - 최고/최저/평균 심박수 계산
  - 심박수 범위별 필터링

#### 3. RunningRoutePointRepository
- **목적**: GPS 경로 포인트 데이터의 영속성 관리
- **주요 기능**:
  - 세션별 경로 포인트 조회
  - 시간 범위별 경로 포인트 조회
  - 최고 속도/고도 포인트 조회
  - 좌표 범위별 필터링
  - 시작/종료 포인트 조회

#### 4. RunningSplitRepository
- **목적**: 러닝 스플릿 데이터의 영속성 관리
- **주요 기능**:
  - 세션별 스플릿 조회
  - 최고/최저 페이스 스플릿 조회
  - 심박수 기반 스플릿 필터링
  - 거리/지속시간 기반 필터링

#### 5. RunningSessionStatisticsRepository
- **목적**: 러닝 세션 통계 데이터의 영속성 관리
- **주요 기능**:
  - 세션별 통계 조회
  - 심박수/보폭/케이던스 기반 필터링
  - VO2max/훈련 효과 기반 필터링
  - 전체 평균 통계 조회

## DDD 패턴 적용

### 1. Repository 패턴
- 각 엔티티별로 전용 Repository 인터페이스 제공
- Spring Data JPA를 활용한 데이터 접근 추상화
- 도메인 로직과 데이터 접근 로직의 분리

### 2. 도메인 서비스
- `domain.service` 패키지에 도메인 서비스 구현
- 복잡한 도메인 로직을 Repository와 함께 처리
- 트랜잭션 경계 관리

### 3. 애플리케이션 서비스
- `application.service` 패키지에 애플리케이션 서비스 구현
- 사용자 요청을 도메인 서비스로 위임
- 외부 시스템과의 협력 조정

## 사용 예시

```java
// Repository 직접 사용
@Autowired
private RunningSessionRepository runningSessionRepository;

List<RunningSession> sessions = runningSessionRepository.findByPlayerOrderByStartTimeDesc(player);

// 도메인 서비스 사용
@Autowired
private RunningSessionDomainService runningSessionDomainService;

RunningSession session = runningSessionDomainService.createRunningSession(player, startTime);

// 애플리케이션 서비스 사용
@Autowired
private RunningApplicationService runningApplicationService;

RunningSession session = runningApplicationService.startRunningSession(player);
```

## 특징

1. **타입 안전성**: 메서드 이름 기반 쿼리 생성으로 컴파일 타임 검증
2. **성능 최적화**: 필요한 데이터만 조회하는 메서드 제공
3. **확장성**: 새로운 조회 요구사항에 대한 메서드 추가 용이
4. **테스트 용이성**: Repository 인터페이스로 인한 모킹 가능
5. **도메인 중심**: 비즈니스 요구사항에 맞는 메서드명과 구조

## 주의사항

1. **N+1 문제 방지**: `@Query`를 사용한 최적화된 쿼리 작성
2. **트랜잭션 관리**: 도메인 서비스에서 `@Transactional` 적절히 사용
3. **메모리 사용량**: 대용량 데이터 조회 시 페이징 고려
4. **인덱스 최적화**: 자주 사용되는 조회 조건에 대한 인덱스 설계 필요 