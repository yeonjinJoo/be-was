package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

public class HTTPResponseWriter {
    public void addResponseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

    public void addResponseHeader(DataOutputStream dos,
                               String version,
                               int statusCode,
                               String statusMessage,
                               String contentType,
                               int lengthOfBodyContent) throws IOException {
        dos.writeBytes(version + " " + statusCode + " " + statusMessage + "\r\n");
        dos.writeBytes("Content-Type: "+  contentType + "\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }
}
