#Build
FROM maven:3.8.3-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ /app/src/
RUN mvn package -DskipTests

#Run
FROM openjdk:17-oracle

WORKDIR /app

COPY --from=build /app/target/wallet-service.jar app.jar

EXPOSE 8001

CMD ["java", "-jar", "app.jar"]
