docker rm batch-practice-db
docker run --name batch-practice-db -e MYSQL_ROOT_PASSWORD=12 -e MYSQL_DATABASE=batch -d -p 3316:3306 mysql
