

# Создаем образ с JRE для запуска приложения
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию для приложения
WORKDIR /app

# Копируем собранный JAR-файл из стадии сборки
COPY --from=builder /app/target/QuizApp-0.0.1-SNAPSHOT.jar /app/quizapp.jar

# Указываем команду для запуска приложения
CMD ["java", "-jar", "/app/quizapp.jar"]