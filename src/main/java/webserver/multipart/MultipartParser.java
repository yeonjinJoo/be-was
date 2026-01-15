package webserver.multipart;

import webserver.http.HTTPRequest;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class MultipartParser {

    public static void applyTo(HTTPRequest request) {
        String contentType = request.getHeader("content-type");
        if (contentType == null || !contentType.startsWith("multipart/form-data")) {
            return; // multipart 아니면 아무것도 안 함
        }

        String boundary = extractBoundary(contentType);
        byte[] body = request.getRawBody();

        byte[] delimiter = ("--" + boundary).getBytes(StandardCharsets.ISO_8859_1);
        List<byte[]> parts = split(body, delimiter);

        for (byte[] part : parts) {
            if (part.length == 0) continue;
            part = trimCrlf(part);

            int headerEnd = indexOf(part, "\r\n\r\n".getBytes(StandardCharsets.ISO_8859_1));
            if (headerEnd < 0) continue;

            byte[] headerBytes = Arrays.copyOfRange(part, 0, headerEnd);
            byte[] dataBytes = Arrays.copyOfRange(part, headerEnd + 4, part.length);

            Map<String, String> headerMap = parsePartHeaders(new String(headerBytes, StandardCharsets.ISO_8859_1));
            String disp = headerMap.getOrDefault("content-disposition", "");

            String name = extractDispositionValue(disp, "name");
            String filename = extractDispositionValue(disp, "filename");
            if (name == null) continue;

            if (filename == null || filename.isBlank()) {
                // 텍스트는 bodyParams로
                String value = new String(dataBytes, StandardCharsets.UTF_8);
                request.putBodyParam(name, value);
            } else {
                // 파일은 files로
                String partCt = headerMap.getOrDefault("content-type", "application/octet-stream");
                UploadedFile file = new UploadedFile(name, filename, partCt, dataBytes);

                request.getFiles()
                        .computeIfAbsent(name, k -> new ArrayList<>())
                        .add(file);
            }
        }
    }

    private static String extractBoundary(String contentType) {
        for (String token : contentType.split(";")) {
            token = token.trim();
            if (token.startsWith("boundary=")) {
                String b = token.substring("boundary=".length());
                if (b.startsWith("\"") && b.endsWith("\"")) b = b.substring(1, b.length() - 1);
                return b;
            }
        }
        throw new IllegalArgumentException("Boundary not found");
    }

    private static Map<String, String> parsePartHeaders(String headerText) {
        Map<String, String> map = new HashMap<>();
        for (String line : headerText.split("\r\n")) {
            int idx = line.indexOf(":");
            if (idx > 0) {
                map.put(line.substring(0, idx).trim().toLowerCase(),
                        line.substring(idx + 1).trim());
            }
        }
        return map;
    }

    private static String extractDispositionValue(String cd, String key) {
        for (String token : cd.split(";")) {
            token = token.trim();
            if (token.startsWith(key + "=")) {
                String v = token.substring((key + "=").length()).trim();
                if (v.startsWith("\"") && v.endsWith("\"")) v = v.substring(1, v.length() - 1);
                return v;
            }
        }
        return null;
    }

    private static List<byte[]> split(byte[] body, byte[] delimiter) {
        List<byte[]> parts = new ArrayList<>();
        int first = indexOf(body, delimiter);
        if (first < 0) return parts;

        int start = first + delimiter.length;
        while (true) {
            int next = indexOf(body, delimiter, start);
            if (next < 0) break;
            parts.add(Arrays.copyOfRange(body, start, next));
            start = next + delimiter.length;
        }
        return parts;
    }

    private static int indexOf(byte[] src, byte[] target) { return indexOf(src, target, 0); }

    private static int indexOf(byte[] src, byte[] target, int from) {
        outer:
        for (int i = from; i <= src.length - target.length; i++) {
            for (int j = 0; j < target.length; j++) {
                if (src[i + j] != target[j]) continue outer;
            }
            return i;
        }
        return -1;
    }

    private static byte[] trimCrlf(byte[] part) {
        int s = 0, e = part.length;
        if (e - s >= 2 && part[s] == '\r' && part[s + 1] == '\n') s += 2;
        if (e - s >= 2 && part[e - 2] == '\r' && part[e - 1] == '\n') e -= 2;
        // 종료 파트 방어
        if (e - s >= 2 && part[s] == '-' && part[s + 1] == '-') return new byte[0];
        return Arrays.copyOfRange(part, s, e);
    }
}

