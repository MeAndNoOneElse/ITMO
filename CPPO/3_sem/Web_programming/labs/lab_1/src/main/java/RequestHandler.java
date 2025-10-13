//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.fastcgi.FCGIInterface;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";

    public static void main(String[] var0) {
        FCGIInterface var1 = new FCGIInterface();

        while(var1.FCGIaccept() >= 0) {
            try {
                String var2 = FCGIInterface.request.params.getProperty("REQUEST_METHOD");
                String var3 = FCGIInterface.request.params.getProperty("CONTENT_LENGTH");
                if (!"POST".equals(var2)) {
                    sendErrorResponse("Only POST method is allowed");
                } else {
                    Map var4 = parseParams(var3);

                    try {
                        double var5 = Double.parseDouble((String)var4.get("x"));
                        double var7 = Double.parseDouble((String)var4.get("y"));
                        double var9 = Double.parseDouble((String)var4.get("r"));
                        if (!validateParams(var5, var7, var9)) {
                            sendErrorResponse("Invalid parameters");
                        } else {
                            boolean var11 = checkArea(var5, var7, var9);
                            sendSuccessResponse(var5, var7, var9, var11);
                        }
                    } catch (NumberFormatException var12) {
                        sendErrorResponse("Invalid number format");
                    }
                }
            } catch (Exception var13) {
                sendErrorResponse("Internal server error: " + var13.getMessage());
            }
        }

    }

    private static Map<String, String> parseParams(String var0) throws IOException {
        HashMap var1 = new HashMap();
        if (var0 != null && !var0.isEmpty()) {
            try {
                int var2 = Integer.parseInt(var0);
                if (var2 > 0) {
                    byte[] var3 = new byte[var2];
                    int var4 = FCGIInterface.request.inStream.read(var3, 0, var2);
                    if (var4 > 0) {
                        String var5 = new String(var3, 0, var4, StandardCharsets.UTF_8);
                        String[] var6 = var5.split("&");

                        for(String var10 : var6) {
                            String[] var11 = var10.split("=");
                            if (var11.length == 2) {
                                var1.put(var11[0], var11[1]);
                            }
                        }
                    }
                }
            } catch (NumberFormatException var12) {
            }

            return var1;
        } else {
            return var1;
        }
    }

    private static boolean validateParams(double var0, double var2, double var4) {
        if (!(var2 < (double)-3.0F) && !(var2 > (double)5.0F)) {
            if (!(var4 <= (double)0.0F) && !(var4 > (double)5.0F)) {
                return !(var0 < (double)-5.0F) && !(var0 > (double)5.0F);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean checkArea(double var0, double var2, double var4) {
        if (var0 <= (double)0.0F && var2 >= (double)0.0F) {
            return var0 >= -var4 && var2 <= var4 / (double)2.0F;
        } else if (var0 >= (double)0.0F && var2 <= (double)0.0F) {
            return var2 >= -var0 - var4;
        } else if (var0 <= (double)0.0F && var2 <= (double)0.0F) {
            return var0 * var0 + var2 * var2 <= var4 * var4;
        } else {
            return false;
        }
    }

    private static void sendSuccessResponse(double var0, double var2, double var4, boolean var6) {
        StringBuilder var7 = new StringBuilder();
        var7.append("{");
        var7.append("\"success\":true,");
        var7.append("\"x\":").append(var0).append(",");
        var7.append("\"y\":").append(var2).append(",");
        var7.append("\"r\":").append(var4).append(",");
        var7.append("\"result\":").append(var6);
        var7.append("}");
        sendResponse(var7.toString());
    }

    private static void sendErrorResponse(String var0) {
        StringBuilder var1 = new StringBuilder();
        var1.append("{");
        var1.append("\"success\":false,");
        var1.append("\"error\":\"").append(var0).append("\"");
        var1.append("}");
        sendResponse(var1.toString());
    }

    private static void sendResponse(String var0) {
        System.out.println("Content-Type: application/json");
        System.out.println();
        System.out.print(var0);
    }
}
