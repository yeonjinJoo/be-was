package application;

import webserver.exception.webexception.BadRequestException;
import webserver.multipart.UploadedFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

public class ImageStorage {

    private final Path uploadDir;

    // 예: new FileStorage("uploads")
    public ImageStorage(String dirName) {
        this.uploadDir = Paths.get(dirName);
    }

    // 저장 후 저장된 파일명 반환
    public String save(UploadedFile file) {
        validateImage(file);

        try {
            Files.createDirectories(uploadDir);

            String ext = extractExtension(file.getOriginalFilename());
            String savedName = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);

            Path target = uploadDir.resolve(savedName);
            Files.write(target, file.getData(), StandardOpenOption.CREATE_NEW);

            return savedName;
        } catch (IOException e) {
            throw new IllegalStateException("이미지 저장 실패", e);
        }
    }

    public void deleteQuietly(String savedName) {
        try {
            Files.deleteIfExists(uploadDir.resolve(savedName));
        } catch (IOException ignore) {}
    }

    private void validateImage(UploadedFile file) {
        String ct = file.getContentType() == null ? "" : file.getContentType().toLowerCase();
        if (!ct.startsWith("image/")) {
            throw BadRequestException.invalidImageType();
        }

        String ext = extractExtension(file.getOriginalFilename());
        Set<String> allowed = Set.of("png", "jpg", "jpeg", "gif", "webp");
        if (!ext.isEmpty() && !allowed.contains(ext)) {
            throw BadRequestException.invalidImageExtension();
        }

        // 크기 제한 5MB
        int maxBytes = 5 * 1024 * 1024;
        if (file.getData() == null || file.getData().length == 0) {
            throw BadRequestException.emptyImageFile();
        }
        if (file.getData().length > maxBytes) {
            throw BadRequestException.imageSizeExceeded(5);
        }
    }

    private String extractExtension(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) return "";
        return filename.substring(dot + 1).toLowerCase();
    }
}

