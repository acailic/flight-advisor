mvn clean install
docker build -t acailic/flight-advisor .
docker run -p 8080:8080 flight-advisor
