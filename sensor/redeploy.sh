docker stop rep-sensor
docker rm rep-sensor
mvn clean package docker:build
docker run --name rep-sensor -d rep-sensor