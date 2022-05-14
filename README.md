# 🌡 SmartThermostat

Manage your home temperature efficiently

## 💡 Info about this project
This is a side project developed for learning and practicing:
- Hexagonal architecture
- DDD tactical patterns
- TDD methodology

- [Kanban board](https://trello.com/b/kkKnKL49/smartthermostat)

## 🚀 How to deploy it
- Create new ``.env[.dev]`` file in ``apps/main/resources`` (see ``.env.dev`` example)

- Execute ``gradle bootJar``

- Execute: ``docker-compose --file docker-compose-dev.yml --env-file apps/main/resources/.env.dev build && docker-compose --file docker-compose-dev.yml --env-file apps/main/resources/.env.dev up``
for local development

- Execute: ``docker-compose --env-file apps/main/resources/.env build && docker-compose --env-file apps/main/resources/.env up`` for production setup
