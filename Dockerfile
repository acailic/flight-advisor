FROM maven:3-jdk-11-slim
EXPOSE 8080
COPY . /app/
WORKDIR /app
RUN mvn clean package -DskipTests
ENTRYPOINT echo java -jar target/flight-advisor-1.0-SNAPSHOT.jar; exec java -jar target/flight-advisor-1.0-SNAPSHOT.jar