import java.io.*;
import java.util.*;

public class RequestHandler {
    private final InputStream input;
    private final PrintStream output;

    public RequestHandler(InputStream input, PrintStream output) {
        this.input = input;
        this.output = output;
    }

    public void handle() throws IOException {
        long startTime = System.nanoTime();

        Map<String, String> params = parseParams(input);
        String xStr = params.get("x");
        String yStr = params.get("y");
        String rStr = params.get("r");

        String result;
        if (isValid(xStr, yStr, rStr)) {
            double x = Double.parseDouble(xStr);
            double y = Double.parseDouble(yStr);
            double r = Double.parseDouble(rStr);

            boolean inside = checkHit(x, y, r);
            result = inside ? "Попадание" : "Промах";
        } else {
            result = "Ошибка: некорректные данные";
        }

        long execTime = (System.nanoTime() - startTime) / 1_000_000;
        String time = new Date().toString();

        sendResponse(xStr, yStr, rStr, result, time, execTime);
    }

    private Map<String, String> parseParams(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String body = reader.readLine();
        Map<String, String> map = new HashMap<>();

        if (body != null) {
            String[] pairs = body.split("&");
            for (String pair : pairs) {
                String[] kv = pair.split("=");
                if (kv.length == 2) {
                    map.put(kv[0], java.net.URLDecoder.decode(kv[1], "UTF-8"));
                }
            }
        }
        return map;
    }

    private boolean isValid(String x, String y, String r) {
        try {
            double xd = Double.parseDouble(x);
            double yd = Double.parseDouble(y);
            double rd = Double.parseDouble(r);

            if (xd < -4 || xd > 4) return false;
            if (yd < -3 || yd > 5) return false;
            if (rd <= 0) return false;

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkHit(double x, double y, double r) {
        // 1. Прямоугольник (нижний левый)
        if (x <= 0 && y <= 0 && x >= -r && y >= -r) {
            return true;
        }

        // 2. Треугольник (нижний правый)
        if (x >= 0 && y <= 0 && y >= -r && x <= r && y >= -x) {
            return true;
        }

        // 3. Четверть круга (верхний левый)
        if (x <= 0 && y >= 0 && (x * x + y * y <= r * r)) {
            return true;
        }

        return false;
    }

    private void sendResponse(String x, String y, String r,
                              String result, String time, long execTime) {
        output.println("Content-type: application/json\n");
        output.println("{");
        output.println("  \"x\": \"" + x + "\",");
        output.println("  \"y\": \"" + y + "\",");
        output.println("  \"r\": \"" + r + "\",");
        output.println("  \"result\": \"" + result + "\",");
        output.println("  \"time\": \"" + time + "\",");
        output.println("  \"execTimeMs\": " + execTime);
        output.println("}");
    }
}
