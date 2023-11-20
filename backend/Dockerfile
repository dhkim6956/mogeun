FROM openjdk:11

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

RUN apt-get install -y tzdata

RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "/app.jar"]
