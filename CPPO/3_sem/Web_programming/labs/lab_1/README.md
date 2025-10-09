#Для запуска на helios:
- Пробросить порты на гелиосе ``` ssh -p 2222 s465029@helios.cs.ifmo.ru -L 8787:localhost:8787```
- ```text httpd -f ~/web/lab1/httpd-root/conf/httpd-conf-helios.conf -k restart```
- ```java -jar -DFCGI_PORT=8788 httpd-root/fcgi-bin/lab1.jar```
- Подключаемся в браузере к http://localhost:8787
