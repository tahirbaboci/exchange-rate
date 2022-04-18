FROM openjdk:11
LABEL maintainer="Tahir Baboci"

ADD build/libs/exchange-rate-0.0.1.jar exchange-rate.jar

ENTRYPOINT ["java","-jar","exchange-rate.jar"]