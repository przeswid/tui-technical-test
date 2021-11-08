FROM adoptopenjdk/openjdk11:alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE
ADD ${JAR_FILE} /app/app.jar
EXPOSE 8090
ENV JAVA_OPTS=""
ENTRYPOINT ["java $JAVA_OPTS","-jar","/app/app.jar"]