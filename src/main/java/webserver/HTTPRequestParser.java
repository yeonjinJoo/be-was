package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HTTPRequestParser {
    public HTTPRequest parse(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder(); // for headers
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        // requestLine 처리
        String requestLine = br.readLine();
        sb.append(requestLine + "\r\n");
        String[] tokens = requestLine.split(" ");
        String method = tokens[0];
        String path = tokens[1];
        String version = tokens[2];

        // 브라우저는 HTTP 요청을 보내고 connection을 끊지 않는다.
        // HTTP 요청 헤더는 빈 줄로 종료되며, 연결이 유지되는 경우 readLine()은 null을 반환하지 않기 때문에
        // 빈 줄을 기준으로 헤더 읽기를 종료해야 한다.
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            sb.append(line + "\r\n");
        }
        String headers = sb.toString();

        return new HTTPRequest(method, path, headers, version);
    }
}
