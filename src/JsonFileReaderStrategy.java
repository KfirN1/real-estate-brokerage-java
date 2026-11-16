import com.google.gson.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class JsonFileReaderStrategy implements FileReaderStrategy {
    private final UserService userService;
    private final Gson gson;
    private final PropertyFactory propertyFactory;

    public JsonFileReaderStrategy(UserService userService) {
        this.userService = userService;
        this.gson = new GsonBuilder().create();
        this.propertyFactory = new PropertyFactory(userService);
    }

    @Override
    public List<Property> readPropertiesFromFile(String filePath) {
        List<JsonObject> jsonObjects = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                jsonObjects.add(element.getAsJsonObject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // **שלב 1: יצירת כל הדירות הראשיות בלבד**
        Map<String, Property> propertiesMap = new HashMap<>();
        List<JsonObject> internalJsonObjects = new ArrayList<>();

        for (JsonObject obj : jsonObjects) {
            if (obj.has("internalNumber")) {
                internalJsonObjects.add(obj);
            } else {
                // קריאה ל-Factory עם נתונים גולמיים
                Property mainProperty = propertyFactory.createProperty(
                        obj.get("street").getAsInt(),
                        obj.get("avenue").getAsInt(),
                        obj.get("area").getAsDouble(),
                        obj.get("price").getAsDouble(),
                        obj.get("isSold").getAsBoolean(),
                        obj.has("owner ID") ? obj.get("owner ID").getAsInt() : null
                );

                propertiesMap.put(mainProperty.getAddress(), mainProperty);
            }
        }

        // **שלב 2: יצירת דירות פנימיות**
        boolean progressMade;
        do {
            progressMade = false;
            Iterator<JsonObject> iterator = internalJsonObjects.iterator();
            while (iterator.hasNext()) {
                JsonObject internalObj = iterator.next();
                String mainAddress = internalObj.getAsJsonObject("mainApartment").get("address").getAsString();

                // בודק אם הדירה הראשית כבר קיימת
                Property mainProperty = propertiesMap.get(mainAddress);
                if (mainProperty != null) {
                    Property internalProperty = propertyFactory.createInternalProperty(
                            mainProperty,
                            internalObj.get("internalNumber").getAsInt(),
                            internalObj.get("area").getAsDouble(),
                            internalObj.get("price").getAsDouble(),
                            internalObj.get("isSold").getAsBoolean(),
                            internalObj.has("owner ID") ? internalObj.get("owner ID").getAsInt() : null
                    );

                    propertiesMap.put(internalProperty.getAddress(), internalProperty);

                    // הוספת הדירה הפנימית לדירה הראשית
                    if (mainProperty instanceof Apartment) {
                        ((Apartment) mainProperty).getInternalApartments().add((InternalApartment) internalProperty);
                    } else if (mainProperty instanceof InternalApartment) {
                        ((InternalApartment) mainProperty).getMainApartment().getInternalApartments().add((InternalApartment) internalProperty);
                    }

                    iterator.remove();
                    progressMade = true;
                }
            }
        } while (progressMade && !internalJsonObjects.isEmpty());

        if (!internalJsonObjects.isEmpty()) {
            throw new IllegalArgumentException("Some internal apartments could not be linked to a main apartment. Check addresses.");
        }

        return new ArrayList<>(propertiesMap.values());
    }
}
