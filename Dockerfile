# Step 1: 빌드 단계
FROM amazoncorretto:21 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 및 프로젝트 메타파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# gradlew 실행 권한 추가
RUN chmod +x gradlew

# Gradle 종속성 캐싱
RUN ./gradlew dependencies --no-daemon || true

# 전체 소스 복사 및 빌드 실행
COPY src src
RUN ./gradlew build -x test --no-daemon

# Step 2: 실행 단계 (JRE만 포함하여 최적화)
FROM amazoncorretto:21 AS runner

WORKDIR /app

# 빌드된 JAR 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
