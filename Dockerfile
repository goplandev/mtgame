# 기본 이미지
FROM openjdk:11
LABEL maintainer="devgo@goplan.co.kr"
# 데이터 보존을 위한 Volume 마운트
VOLUME /tmp
#외부에 노출할 포트
EXPOSE 8080 80
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} mtgame.jar

# 실행 명령
#ENTRYPOINT ["java","-Djava.security.egd", "-Xdebug","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005","-jar","/myapp.jar"]
ENTRYPOINT ["java","-jar","/mtgame.jar"]