# Изменения и исправления в лабораторной работе №1

## Обзор
Этот документ описывает все изменения, внесенные для исправления критических ошибок в FastCGI сервере для проверки попадания точки в область.

## Исправленные ошибки

### 1. RequestHandler.java - Неправильное формирование JSON ответа

**Проблема:** После заголовка `Content-Type` стоял `\n\n`, а затем JSON выводился построчно с `println`, что создавало невалидный JSON с лишними символами новой строки.

**Решение:**
- Использован `StringBuilder` для формирования JSON в одну строку
- Метод `sendResponse()` использует `System.out.print()` (без ln) для вывода JSON
- JSON формируется без лишних символов новой строки

```java
private static void sendSuccessResponse(double x, double y, double r, boolean result) {
    StringBuilder json = new StringBuilder();
    json.append("{");
    json.append("\"success\":true,");
    json.append("\"x\":").append(x).append(",");
    json.append("\"y\":").append(y).append(",");
    json.append("\"r\":").append(r).append(",");
    json.append("\"result\":").append(result);
    json.append("}");
    
    sendResponse(json.toString());
}

private static void sendResponse(String jsonContent) {
    System.out.println(CONTENT_TYPE + ": " + APPLICATION_JSON);
    System.out.println();
    System.out.print(jsonContent);  // Без newline!
}
```

### 2. RequestHandler.java - Неправильное чтение POST данных

**Проблема:** `BufferedReader.readLine()` читал только первую строку, но для FastCGI нужно читать все данные исходя из `CONTENT_LENGTH`.

**Решение:**
- Чтение `CONTENT_LENGTH` из переменных окружения FastCGI
- Использование `FCGIInterface.request.inStream.read()` с буфером нужного размера
- Корректный парсинг POST данных из байтового буфера

```java
private static Map<String, String> parseParams(String contentLengthStr) throws IOException {
    Map<String, String> params = new HashMap<>();
    
    if (contentLengthStr == null || contentLengthStr.isEmpty()) {
        return params;
    }
    
    try {
        int contentLength = Integer.parseInt(contentLengthStr);
        if (contentLength > 0) {
            byte[] buffer = new byte[contentLength];
            int bytesRead = FCGIInterface.request.inStream.read(buffer, 0, contentLength);
            
            if (bytesRead > 0) {
                String data = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                String[] pairs = data.split("&");
                
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length == 2) {
                        params.put(keyValue[0], keyValue[1]);
                    }
                }
            }
        }
    } catch (NumberFormatException e) {
        // Invalid content length
    }
    
    return params;
}
```

### 3. RequestHandler.java - Обработка переменных окружения FastCGI

**Проблема:** Отсутствовала обработка переменных окружения FastCGI (`REQUEST_METHOD` и `CONTENT_LENGTH`).

**Решение:**
- Чтение `REQUEST_METHOD` для проверки типа запроса
- Чтение `CONTENT_LENGTH` для определения размера POST данных
- Проверка метода запроса (разрешен только POST)

```java
// Read environment variables
String requestMethod = FCGIInterface.request.params.getProperty("REQUEST_METHOD");
String contentLengthStr = FCGIInterface.request.params.getProperty("CONTENT_LENGTH");

if (!"POST".equals(requestMethod)) {
    sendErrorResponse("Only POST method is allowed");
    continue;
}
```

### 4. RequestHandler.java - Неправильная валидация Y

**Проблема:** Условие `numValue <= -3 || numValue >= 5` исключало граничные значения -3 и 5.

**Решение:**
- Изменено на `numValue < -3 || numValue > 5`
- Теперь диапазон [-3, 5] включает граничные значения

```java
private static boolean validateParams(double x, double y, double r) {
    // Y: must be in range [-3, 5] - inclusive boundaries
    if (y < -3 || y > 5) {
        return false;
    }
    // ... остальные проверки
}
```

### 5. script.js - Неправильная валидация Y

**Проблема:** Условие `numValue <= -3 || numValue >= 5` исключало граничные значения.

**Решение:**
- Изменено на `numValue < -3 || numValue > 5`
- Обновлено сообщение об ошибке: "Y must be in range [-3, 5]"

```javascript
function validateY(numValue) {
    // ... проверка на NaN
    
    // Y must be in range [-3, 5] - inclusive boundaries
    if (numValue < -3 || numValue > 5) {
        errorMsg.textContent = 'Y must be in range [-3, 5]';
        yInput.classList.add('error');
        return false;
    }
    
    errorMsg.textContent = '';
    yInput.classList.remove('error');
    return true;
}
```

## Добавленные файлы

### 1. httpd.conf - Конфигурация Apache

Создан файл конфигурации Apache для развертывания на Helios:
- Настройка порта (заглушка для замены)
- ProxyPass для `/cgi-bin/area.fcgi` к FastCGI серверу через Unix сокет
- Настройка для статических файлов (HTML, CSS, JS)
- Логирование

### 2. run.sh - Скрипт компиляции и запуска

Создан bash скрипт для автоматизации:
- Проверка наличия fastcgi-lib.jar
- Компиляция Java файлов с правильным classpath
- Запуск FastCGI сервера на Unix сокете
- Автоматическая очистка сокета при завершении

### 3. README.md - Инструкция по развертыванию

Создано подробное руководство с:
- Описанием проекта и области проверки
- Инструкциями по установке на Helios
- Описанием структуры проекта
- Параметрами валидации
- Примерами использования

### 4. index.html - Веб-интерфейс

Создана HTML страница с:
- Формой для ввода параметров X, Y, R
- Canvas для визуализации области и точек
- Таблицей результатов проверки
- Адаптивным дизайном

### 5. Дополнительные файлы

- `lib/README.md` - инструкции по установке fastcgi-lib.jar
- `test/JsonTest.java` - тест для проверки JSON форматирования и валидации
- `.gitignore` - игнорирование build артефактов и логов

## Дополнительные улучшения

### Обработка ошибок
- Отправка JSON с сообщением об ошибке при некорректных данных
- Проверка типа запроса (только POST)
- Валидация всех параметров на сервере

### Логирование
- Настроены логи Apache (access.log и error.log)
- Возможность добавления отладочного вывода в консоль через System.err

### JSON формирование
- Использование StringBuilder для эффективного формирования строк
- Единообразный формат для всех ответов
- Валидный JSON без лишних символов

## Проверка исправлений

Создан тест `test/JsonTest.java` который проверяет:
1. ✅ JSON не содержит лишних символов новой строки
2. ✅ Валидация Y корректно принимает граничные значения [-3, 5]
3. ✅ Валидация Y корректно отклоняет значения вне диапазона

Результаты теста:
```
Testing JSON formatting...

Success Response:
{"success":true,"x":1.0,"y":2.5,"r":3.0,"result":true}

Error Response:
{"success":false,"error":"Invalid parameters"}

✓ Success JSON has no newlines
✓ Error JSON has no newlines

Testing Y validation (valid range: [-3, 5])...
Y = -3.1: ✗ Invalid
Y = -3.0: ✓ Valid
Y = -2.9: ✓ Valid
Y = 0.0: ✓ Valid
Y = 4.9: ✓ Valid
Y = 5.0: ✓ Valid
Y = 5.1: ✗ Invalid
```

## Заключение

Все критические ошибки исправлены:
- ✅ JSON формируется корректно без лишних символов
- ✅ POST данные читаются полностью с учетом CONTENT_LENGTH
- ✅ Валидация Y правильная (диапазон [-3, 5] включительно)
- ✅ Добавлены все необходимые конфигурационные файлы
- ✅ Создана документация и инструкции

Приложение готово к развертыванию на Helios после установки fastcgi-lib.jar и настройки конфигурации Apache.
