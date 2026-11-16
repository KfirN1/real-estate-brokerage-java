import java.util.List;

public interface FileReaderStrategy {
    List<Property> readPropertiesFromFile (String filePath);
}
