FROM adoptopenjdk/openjdk11:latest

RUN mkdir -p /smartthermostat-backend

ADD target/smartthermostat-backend.jar /smartthermostat-backend/smartthermostat-backend.jar

CMD java -Xmx300m -Xss512k -XX:CICompilerCount=2 -Dfile.encoding=UTF-8 -XX:+UseContainerSupport -jar
/smartthermostat-backend/smartthermostat-backend.jar temperatures_backend server