## Мини-отчет о дз по работа с Базами данных в java 

---

Для начала, нужно запустить PostgreSQL. 
Самый быстрый вариант для данной домашней работы это запуск в Docker контейнере.

```shell
docker run --name PosgreSQL_Java_hw -e POSTGRES_PASSWORD=Jed3924KK -d -p 5432:5432 postgres
```
Далее нужно зайти внутрь базы данных
```shell
docker run -it --rm --link PosgreSQL_Java_hw:postgres postgres psql -h postgres -U postgres
```
Ну или как сделал я и использовал возможности IDE и подключился через нее)

Создал таблицу пользователей:
```postgresql
CREATE TABLE Users (
   id SERIAL PRIMARY KEY,
   name VARCHAR(255) NOT NULL,
   password_hash VARCHAR(255) NOT NULL
);
```
Далее написал класс UserAuth. SQL инъекции не работают, все работает через бд, а значит что программу можно перезапускать и все будет нормально работать.
