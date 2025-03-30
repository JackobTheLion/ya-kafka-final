# Kafka final

## Описание docker-compose.yml
* kafka-1-X - первый кластер кафка. Работает на портах 9094-9096 
* kafka-2-X - первый кластер кафка. Работает на портах 9097-9099
* ksqldb-server - ksqldb сервер, порт 8088
* kafka-connect - коннектор, использующийся для зеркалирования кластера, загрузки данных о товарах в файл и в hdfs, порт 8083
* ui - веб интерфейс для кафки, порт 8085
* hadoop-namenode - неймнода хадупа, порт 9000 для доступа, 9870 - web-ui
* hadoop-datanode-X - датаноды
* analytics - сервис аналитики для предоставления рекомендаций
* prometheus - порт 9090
* grafana - порт 3000

## Описание модулей
* commons - модуль с общими классами
* analytics - модуль аналитики и предоставления рекомендаций. Читает hdfs и пишет рекомендации в кафка
* client-api - клиентское приложение. позволяет искать по существующим товарам, отправляя поисковые запросы в кафку, и получать рекомендации
* shop-api - позволяет отправлять товары в кафку, читая файл data.json
* goods-filter - отвечает за фильтарцию товаров, читает raw топик, и пишет в отфильтрованный

## Сборка и запуск
1. Соберем проект
```bash
mvn clean package
```
2. Запустим докер и все контейнеры
```bash
docker-compose up -d
```
3. Создадим топики с помощтю скрипта create-topics.sh
4. Запустим коннекторы run-connectors.sh
5. Создадим ksql таблицу recommendations.sh
6. Импортируем в графану дешборд infra/grafana/dashboards/Kafka Connect.json
7. Настроим алерты:  
![alert.png](screenshots/alert.png)  
![notification_setup.png](screenshots/notification_setup.png)  
Убедимся, что они приходят в Телеграм  
![alert_sample.png](screenshots/alert_sample.png)

8. Запустим фильтр товаров
```bash
java -jar ./goods-filter/target/goods-filter-1.0-SNAPSHOT-jar-with-dependencies.jar
```
9. Создадим фильтр для товара. Отправим в топик balcklist сообщение вида
```json
{
  "goodsName": "часы",
  "add": true
}
```
10. Убедимся, товар с таким именем не пройдет запустив
```bash
java -jar ./shop-api/target/shop-api-1.0-SNAPSHOT-jar-with-dependencies.jar
```

11. Удалим товар из черного списка, отправив сообщение вида
```json
{
  "goodsName": "часы",
  "add": false
}
```
12. Запустим client-api. Убедимся, что работает поиск и выставление рекомендаций.