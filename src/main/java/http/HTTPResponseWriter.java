package http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class HTTPResponseWriter {

    public void write(DataOutputStream dos,
                      String version,
                      HTTPResponse response) throws IOException {
        writeHeaders(dos, version, response);
        writeBody(dos, response.getBody());
    }

    private void writeHeaders(DataOutputStream dos,
                              String version,
                              HTTPResponse response) throws IOException {
        // status line
        dos.writeBytes(version + " " + response.getStatusCode() + " " + response.getStatusMessage() + "\r\n");

        // Content-Type (있는 경우에만)
        String contentType = response.getContentType();
        if (contentType != null && !contentType.isBlank()) {
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
        }

        // extra headers (Location 등)
        for (Map.Entry<String, String> h : response.getHeaders().entrySet()) {
            dos.writeBytes(h.getKey() + ": " + h.getValue() + "\r\n");
        }

        // Content-Length (항상)
        byte[] body = response.getBody();
        int length = (body == null) ? 0 : body.length;
        dos.writeBytes("Content-Length: " + length + "\r\n");

        // header/body separator
        dos.writeBytes("\r\n");
    }

    private void writeBody(DataOutputStream dos, byte[] body) throws IOException {
        if (body == null || body.length == 0) return;
        dos.write(body, 0, body.length);
    }
}
