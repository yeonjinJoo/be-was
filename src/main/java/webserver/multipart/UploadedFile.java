package webserver.multipart;

public class UploadedFile {
    private final String fieldName;        // e.g. "image"
    private final String originalFilename; // e.g. "cat.png"
    private final String contentType;      // e.g. "image/png"
    private final byte[] data;             // file bytes

    public UploadedFile(String fieldName, String originalFilename, String contentType, byte[] data) {
        this.fieldName = fieldName;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.data = data;
    }

    public String getFieldName() { return fieldName; }
    public String getOriginalFilename() { return originalFilename; }
    public String getContentType() { return contentType; }
    public byte[] getData() { return data; }
}
