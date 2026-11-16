import java.util.HashMap;

public class FileReaderFactory {
    public static FileReaderStrategy createFileReader(String type) {
        return switch (type.toLowerCase()) {
            case "json" -> new JsonFileReaderStrategy(UserService.getInstance());
            default -> throw new IllegalArgumentException("Unknown file reader type: " + type);
        };
    }
}
