package webserver.view;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ViewUtils {
    private static final String PATH = "./src/main/resources/template/";

    public static String readHtmlFile(String fileName) {
        try {
            return Files.readString(Paths.get(PATH + fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return ""; // 파일이 없으면 빈 문자열
        }
    }
}
