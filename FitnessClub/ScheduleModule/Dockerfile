# Используем легковесный образ с Java 17
FROM openjdk:17-jdk-alpine

# Создаём том для временных файлов (необязательно)
VOLUME /tmp

# Копируем собранный jar-файл в контейнер
COPY target/*.jar app.jar

# Запускаем приложение
ENTRYPOINT ["java","-jar","/app.jar"]