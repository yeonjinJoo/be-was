package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class HTTPRequestParser {
    public HTTPRequest parse(BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder(); // for headers
        HashMap<String, String> headers = new HashMap<>();

        // requestLine 처리
        String requestLine;
        if((requestLine = br.readLine()) == null){
            return null;
        }
        sb.append(requestLine + "\r\n");
        String[] tokens = requestLine.split(" ");
        String method = tokens[0];
        String path = tokens[1];
        String version = tokens[2];

        // HTTP/1.1 HTTP 요청을 한 번 보내고 connection이 종료되지 않고 유지된다.
        // 한 번의 HTTP 요청 헤더는 빈 줄로 종료되며, 연결이 유지되는 경우 readLine()은 null을 반환하지 않기 때문에
        // 빈 줄을 기준으로 헤더 읽기를 종료해야 한다.
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            int idx = line.indexOf(":");
            if (idx > 0) {
                String key = line.substring(0, idx).trim();
                String value = line.substring(idx + 1).trim();
                headers.put(key, value);
            }
            sb.append(line).append("\r\n");
        }

        String rawHeaders = sb.toString();
        return new HTTPRequest(method, path, headers, rawHeaders, version);
    }
}
