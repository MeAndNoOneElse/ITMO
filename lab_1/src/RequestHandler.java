import com.fastcgi.FCGIInterface;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    
    public static void main(String[] args) {
        FCGIInterface fcgi = new FCGIInterface();
        
        while (fcgi.FCGIaccept() >= 0) {
            try {
                // Read environment variables
                String requestMethod = FCGIInterface.request.params.getProperty("REQUEST_METHOD");
                String contentLengthStr = FCGIInterface.request.params.getProperty("CONTENT_LENGTH");
                
                if (!"POST".equals(requestMethod)) {
                    sendErrorResponse("Only POST method is allowed");
                    continue;
                }
                
                // Parse POST data
                Map<String, String> params = parseParams(contentLengthStr);
                
                // Validate and process
                try {
                    double x = Double.parseDouble(params.get("x"));
                    double y = Double.parseDouble(params.get("y"));
                    double r = Double.parseDouble(params.get("r"));
                    
                    // Validate parameters
                    if (!validateParams(x, y, r)) {
                        sendErrorResponse("Invalid parameters");
                        continue;
                    }
                    
                    // Check if point is in area
                    boolean result = checkArea(x, y, r);
                    
                    // Send response
                    sendSuccessResponse(x, y, r, result);
                    
                } catch (NumberFormatException e) {
                    sendErrorResponse("Invalid number format");
                }
                
            } catch (Exception e) {
                sendErrorResponse("Internal server error: " + e.getMessage());
            }
        }
    }
    
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
    
    private static boolean validateParams(double x, double y, double r) {
        // X: must be one of the predefined values (typically -4, -3, -2, -1, 0, 1, 2, 3, 4)
        // Y: must be in range [-3, 5] - inclusive boundaries
        // R: must be positive (typically 1, 2, 3, 4, 5)
        
        if (y < -3 || y > 5) {
            return false;
        }
        
        if (r <= 0 || r > 5) {
            return false;
        }
        
        // X validation (allowing reasonable range)
        if (x < -5 || x > 5) {
            return false;
        }
        
        return true;
    }
    
    private static boolean checkArea(double x, double y, double r) {
        // Check if point (x, y) is within the defined area with radius r
        
        // Rectangle in the second quadrant (x <= 0, y >= 0)
        if (x <= 0 && y >= 0) {
            return x >= -r && y <= r / 2;
        }
        
        // Triangle in the fourth quadrant (x >= 0, y <= 0)
        if (x >= 0 && y <= 0) {
            return y >= -x - r;
        }
        
        // Quarter circle in the third quadrant (x <= 0, y <= 0)
        if (x <= 0 && y <= 0) {
            return x * x + y * y <= r * r;
        }
        
        // First quadrant - no area
        return false;
    }
    
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
    
    private static void sendErrorResponse(String message) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"success\":false,");
        json.append("\"error\":\"").append(message).append("\"");
        json.append("}");
        
        sendResponse(json.toString());
    }
    
    private static void sendResponse(String jsonContent) {
        System.out.println(CONTENT_TYPE + ": " + APPLICATION_JSON);
        System.out.println();
        System.out.print(jsonContent);
    }
}
