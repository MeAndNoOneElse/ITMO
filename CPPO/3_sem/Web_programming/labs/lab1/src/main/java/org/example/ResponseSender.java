package org.example;

import com.fastcgi.FCGIInterface;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

public class ResponseSender {
  private final PointValidator validator;
  private final PointValidator hitChecker;
  private final RequestParser parser;
  private final OutputHandler outputHandler;
  private final SimpleDateFormat dateFormatter;

  public ResponseSender() {
    this(new Validator(), new HitChecker(), new JsonParser(), new FastCGIOutputHandler());
  }

  public ResponseSender(PointValidator validator, PointValidator hitChecker, RequestParser parser, OutputHandler outputHandler) {
    this.validator = validator;
    this.hitChecker = hitChecker;
    this.parser = parser;
    this.outputHandler = outputHandler;
    this.dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    this.dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  public void sendResponse() {
    try {
      long startTime = System.nanoTime();

      String requestUri = FCGIInterface.request.params.getProperty("REQUEST_URI");
      if (requestUri == null) {
        sendNotFound("Request URI is missing");
        return;
      }

      String method = FCGIInterface.request.params.getProperty("REQUEST_METHOD");
      if (method == null || !"POST".equalsIgnoreCase(method)) {
        sendMethodNotAllowed("Only POST is allowed. Received: " + method);
        return;
      }

      if (requestUri.equals("/calculate")) {
        handleCalculateAll(startTime);
      } else {
        sendNotFound("");
      }
    } catch (IllegalArgumentException e) {
      sendError(e.getMessage());
    } catch (Exception e) {
      sendServerError("Server error: " + e.getMessage());
    }
  }

  private void handleCalculateAll(long startTime) throws IOException {
    BigDecimal[] data = readRequestBody();
    BigDecimal x = data[0];
    BigDecimal y = data[1];
    BigDecimal r = data[2];

    try {
      validator.validate(x, y, r);
    } catch (IllegalArgumentException e) {
      sendError(e.getMessage());
      return;
    }

    boolean hit = hitChecker.isHit(x, y, r);
    long endTime = System.nanoTime();
    double scriptTimeMs = (endTime - startTime) / 1000000.0D;

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("x", x.toPlainString());
    response.put("y", y.toPlainString());
    response.put("r", r.toPlainString());
    response.put("hit", Boolean.valueOf(hit));
    response.put("shape", "all");
    response.put("currentTime", dateFormatter.format(new Date()));
    response.put("scriptTimeMs", String.format("%.2f", Double.valueOf(scriptTimeMs)));

    sendJson(response);
  }


  private BigDecimal[] readRequestBody() throws IOException {
    FCGIInterface.request.inStream.fill();
    int length = FCGIInterface.request.inStream.available();

    if (length <= 0) {
      throw new IllegalArgumentException("Empty request body");
    }

    ByteBuffer buffer = ByteBuffer.allocate(length);
    int readBytes = FCGIInterface.request.inStream.read(buffer.array(), 0, length);
    byte[] raw = new byte[readBytes];
    buffer.get(raw);
    String request = new String(raw, StandardCharsets.UTF_8);

    return parser.getBigDecimals(request);
  }

  private void sendJson(Map<String, Object> map) {
    String json = toJson(map);
    String httpResponse = String.format(
        "Status: 200 OK\nContent-Type: application/json\nAccess-Control-Allow-Origin: *\nContent-Length: %d\n\n%s\n",
        json.getBytes(StandardCharsets.UTF_8).length, json
    );
    try {
      outputHandler.send(httpResponse);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendError(String message) {
    String json = String.format("{\"error\":\"%s\"}", escapeJson(message));
    String httpResponse = String.format(
        "Status: 400 Bad Request\nContent-Type: application/json\nAccess-Control-Allow-Origin: *\nContent-Length: %d\n\n%s\n",
        json.getBytes(StandardCharsets.UTF_8).length, json
    );
    try {
      outputHandler.send(httpResponse);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendNotFound(String message) {
    String json = String.format("{\"error\":\"%s\"}", escapeJson(message));
    String httpResponse = String.format(
        "Status: 404 Not Found\nContent-Type: application/json\nAccess-Control-Allow-Origin: *\nContent-Length: %d\n\n%s\n",
        json.getBytes(StandardCharsets.UTF_8).length, json
    );
    try {
      outputHandler.send(httpResponse);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendMethodNotAllowed(String message) {
    String json = String.format("{\"error\":\"%s\"}", escapeJson(message));
    String httpResponse = String.format(
        "Status: 405 Method Not Allowed\nContent-Type: application/json\nAccess-Control-Allow-Origin: *\nAllow: POST\nContent-Length: %d\n\n%s\n",
        json.getBytes(StandardCharsets.UTF_8).length, json
    );
    try {
      outputHandler.send(httpResponse);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendServerError(String message) {
    String json = String.format("{\"error\":\"%s\"}", escapeJson(message));
    String httpResponse = String.format(
        "Status: 500 Internal Server Error\nContent-Type: application/json\nAccess-Control-Allow-Origin: *\nContent-Length: %d\n\n%s\n",
        json.getBytes(StandardCharsets.UTF_8).length, json
    );
    try {
      outputHandler.send(httpResponse);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String toJson(Map<String, Object> map) {
    StringBuilder sb = new StringBuilder("{");
    boolean first = true;
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      if (!first) sb.append(",");
      first = false;

      sb.append("\"").append(escapeJson(entry.getKey())).append("\":");
      Object val = entry.getValue();
      if (val instanceof String) {
        sb.append("\"").append(escapeJson((String)val)).append("\"");
      } else {
        sb.append(val);
      }
    }
    sb.append("}");
    return sb.toString();
  }

  private String escapeJson(String str) {
    if (str == null) return "";
    return str.replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
        .replace("\t", "\\t");
  }
}