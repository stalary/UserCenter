FROM openjdk:8-jdk-alpine
ADD usercenter-0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar","--com.stalary.lightmq.consumer=true"]
EXPOSE 7200