# Solving SQL Injection Vulnerabilities in Java

Run the service:

    gradle build
    docker build -t busservicedb -f Dockerfile-db .
    docker build -t busservice -f Dockerfile .
    docker-compose up
