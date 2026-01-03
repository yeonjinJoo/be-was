package webserver.http;

import java.io.DataOutputStream;
import java.io.IOException;

public class HTTPResponseWriter {
    public void addResponseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
    }

    public void addResponseHeader(DataOutputStream dos,
                               String version,
                               HTTPResponse httpResponse) throws IOException {
        dos.writeBytes(version + " " + httpResponse.getStatusCode() + " " + httpResponse.getStatusMessage() + "\r\n");
        dos.writeBytes("Content-Type: "+  httpResponse.getContentType() + "\r\n");
        dos.writeBytes("Content-Length: " + httpResponse.getBody().length + "\r\n");
        dos.writeBytes("\r\n");
    }
}
