package webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

import config.DependencyLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final DependencyLoader LOADER = new DependencyLoader();
    private static final int DEFAULT_PORT = 8080;
    private static final int THREAD_POOL_SIZE = 30; // Thread 수 고정
    private static final int QUEUE_SIZE = 100; // 요청 최대 적재 수

    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            ExecutorService executor = new ThreadPoolExecutor(
                    THREAD_POOL_SIZE,
                    THREAD_POOL_SIZE,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(QUEUE_SIZE),
                    new ThreadPoolExecutor.AbortPolicy() // 서버가 더 이상 요청을 받을 수 없을 때 즉시 실패로 알림
            );

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            try{
                while(true){
                    connection = listenSocket.accept();
                    try{
                        executor.execute(new RequestHandler(connection, LOADER.router));
                    } catch (Exception e) {
                        logger.error("Request rejected: server overloaded", e.getMessage());
                        connection.close();
                    }
                }
            } catch (IOException e) {
                logger.error("Server socket error", e.getMessage());
            } finally {
                executor.shutdown();
            }
        }
    }
}
