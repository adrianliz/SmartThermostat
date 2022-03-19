# 🌡 SmartThermostat

Manage your home temperature efficiently

## 💡 Info about this project
This is a side project developed for learning and practicing:
- Hexagonal architecture
- DDD tactical patterns
- TDD methodology

[Kanban board](https://trello.com/b/kkKnKL49/smartthermostat)

## 🚀 How to deploy it
- Create new ``.env`` file in the resource folder with these variables:
  - TEMPERATURES_SERVER_PORT
  - TEMPERATURES_MQTT_BROKER_URI
  - TEMPERATURES_MQTT_CLIENT_ID
  - TEMPERATURES_MQTT_TOPIC
  - TEMPERATURES_STOMP_ENDPOINT
  - TEMPERATURES_STOMP_TOPIC
  - TEMPERATURES_STOMP_APP_PREFIX
  - TEMPERATURES_STOMP_USER_PREFIX
  - TEMPERATURES_DATABASE_HOST
  - TEMPERATURES_DATABASE_PORT
  - TEMPERATURES_DATABASE_NAME
  - TEMPERATURES_DATABASE_USER
  - TEMPERATURES_DATABASE_PASSWORD

- Execute: ``docker-compose --env-file src/main/resources/.env build``
- Execute: ``docker-compose --env-file src/main/resources/.env up``
