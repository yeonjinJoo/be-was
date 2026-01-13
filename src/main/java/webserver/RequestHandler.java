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
    private Dispatcher dispatcher;

    public RequestHandler(Socket connectionSocket, Dispatcher dispatcher) {
        this.connection = connectionSocket;
        this.dispatcher = dispatcher;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        configureTimeout();

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            handleConnection(in, out);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    // TCP connection timeout 시간 설정
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
                HTTPRequest httpRequest = httpRequestParser.parse(br);
                if(httpRequest == null){
                    logger.debug("Client closed TCP connection. Handled {} requests on this connection.", requestCount);
                    break;
                }

                logRequestHeaders(httpRequest);
                dispatcher.dispatch(httpRequest, dos, httpResponseWriter);

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
