package webserver;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final HTTPRequestParser httpRequestParser = new HTTPRequestParser();
    private static final HTTPResponseWriter httpResponseWriter = new HTTPResponseWriter();
    private static final String baseResourcePath = "./src/main/resources/static";
    private static final Map<Integer, String> STATUS_MESSAGES = Map.of(
            200, "OK",
            404, "Not Found",
            500, "Internal Server Error"
    );
    private static final int CONNECTION_TIMEOUT = 10000; // Tomcat 기본 설정 (20s)

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        // TCP connection timeout 시간 설정
        configureTimeout();

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // 요청 처리
            handleConnection(in, out);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void configureTimeout(){
        try{
            connection.setSoTimeout(CONNECTION_TIMEOUT);
        } catch (SocketException e){
            logger.debug("Failed to set socket timeout: {}", e.getMessage());
        }
    }

    private void handleConnection(InputStream in, OutputStream out) throws IOException{
        DataOutputStream dos = new DataOutputStream(out);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        int requestCount = 0;

        while(true){
            try {
                // 1. request 파싱
                HTTPRequest httpRequest = httpRequestParser.parse(br);

                // 클라이언트가 창 닫아서 connection 종료
                if(httpRequest == null){
                    logger.debug("Client closed TCP connection. Handled {} requests on this connection.", requestCount);
                    break;
                }
                // 2. headers 출력
                logRequestHeaders(httpRequest);

                // 3. 요청 처리
                byte[] body;
                HTTPResponse httpResponse;
                try { // 200 - 정상 처리
                    body = readFile(httpRequest.getPath());
                    String contentType = httpRequest.getContentType();
                    httpResponse = new HTTPResponse(200, contentType, body);
                } catch (FileNotFoundException e) { // 404 - 파일 존재 x
                    body = "<h1>404 Not Found</h1>".getBytes();
                    httpResponse = new HTTPResponse(404, body);
                } catch (IOException e) { // 500 - 서버 오류
                    body = "<h1>500 Internal Server Error</h1>".getBytes();
                    httpResponse = new HTTPResponse(500, body);
                }

                // 3. response 생성 & send
                String version = httpRequest.getVersion();
                int statusCode = httpResponse.getStatusCode();
                String statusMessage = STATUS_MESSAGES.get(statusCode);
                String contentType = httpResponse.getContentType();

                httpResponseWriter.addResponseHeader(dos, version, statusCode, statusMessage, contentType, body.length);
                httpResponseWriter.addResponseBody(dos, body);
                dos.flush();

                requestCount++;
            } catch (SocketTimeoutException e){ // connection 타임아웃
                logger.debug("Socket timeout. Closing connection: {}. Handled {} requests on this connection.", e.getMessage(), requestCount);
                break;
            } catch (SocketException e){ // connection 오류
                logger.debug("Client disconnected unexpectedly: {}. Handled {} requests on this connection.", e.getMessage(), requestCount);
                break;
            }
        }
    }

    private void logRequestHeaders(HTTPRequest request){
        StringBuilder sb = new StringBuilder();
        sb.append("\n------------\n");
        String[] tokens = request.getRawHeaders().split("\r\n");
        for (String token : tokens) {
            sb.append(token + "\n");
        }
        sb.append("------------\n");

        logger.debug(sb.toString());
    }

    private byte[] readFile(String path) throws IOException {
        // 기본 페이지는 index.html
        if ("/".equals(path)) {
            path = "/index.html";
        }

        File file = new File(baseResourcePath + path);

        // 파일이 존재하지 않는 경우
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException(file.getPath());
        }

        String resourcePath = baseResourcePath + path;

        try (InputStream is = new FileInputStream(resourcePath)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            int read;
            while ((read = is.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }

            return baos.toByteArray();
        }
    }
}
