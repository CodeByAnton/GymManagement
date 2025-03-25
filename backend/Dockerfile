
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /workspace

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn package -DskipTests

FROM openjdk:17-jdk-slim

COPY --from=build /workspace/target/backend.jar backend.jar

RUN apt-get update && apt-get install -y netcat

COPY entrypoint.sh .

RUN sed -i 's/\r$//' entrypoint.sh

RUN chmod +x entrypoint.sh

ENTRYPOINT ["/bin/bash", "/entrypoint.sh"]