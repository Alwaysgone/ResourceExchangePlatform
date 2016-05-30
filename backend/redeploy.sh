docker stop rep-backend
docker rm rep-backend
mvn clean package docker:build
docker run --name rep-backend -p 8080:8080 -d rep-backend