FROM openjdk:8-jdk-alpine
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ADD usercenter-0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar","--com.stalary.lightmq.consumer=true"]
EXPOSE 7200