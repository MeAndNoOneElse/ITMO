import com.fastcgi.FCGIInterface;

import java.io.IOException;

public class AreaCheckServer {

    public static void main(String[] args) throws IOException {
        new AreaCheckServer().start();
    }

    public void start() throws IOException {
        FCGIInterface fcgi = new FCGIInterface();
        while (fcgi.FCGIaccept() >= 0) {
            new RequestHandler(System.in, System.out).handle();
        }
    }
}

