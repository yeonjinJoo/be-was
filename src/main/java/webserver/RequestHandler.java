package webserver;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HTTPRequest;
import webserver.http.HTTPRequestParser;
import webserver.http.HTTPResponse;
import webserver.http.HTTPResponseWriter;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final HTTPRequestParser httpRequestParser = new HTTPRequestParser();
    private static final HTTPResponseWriter httpResponseWriter = new HTTPResponseWriter();
    private static final int CONNECTION_TIMEOUT = 20000; // Tomcat 기본 설정 (20s)

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
                HTTPResponse httpResponse = Router.route(httpRequest);

                // 3. response 생성 & send
                httpResponseWriter.write(dos, httpRequest.getVersion(), httpResponse);
                dos.flush();

                requestCount++;
            } catch (SocketTimeoutException e){ // connection 타임아웃
                logger.debug("Socket timeout : Closing connection: {}. Handled {} requests on this connection.", e.getMessage(), requestCount);
                break;
            } catch (SocketException e){ // connection 오류
                logger.debug("Socket disconnected unexpectedly : {}. Handled {} requests on this connection.", e.getMessage(), requestCount);
                break;
            } catch (IllegalArgumentException e) {
                logger.debug("Invalid argument : {}." + e.getMessage());
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
}
