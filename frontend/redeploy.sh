docker stop rep-frontend
docker rm rep-frontend
docker build -t rep-frontend .
docker run --name rep-frontend -p 80:80 -d rep-frontend