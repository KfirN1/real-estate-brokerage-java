import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class JsonFileReaderStrategyTest {
    private JsonFileReaderStrategy jsonFileReaderStrategy;
    private UserService userService = UserService.getInstance();
    private static final String TEST_JSON_FILE = "test_resources/properties.json";

    @BeforeEach
    void setUp() throws IOException {
        PropertyManager.getInstance().clearProperties();
        UserService.getInstance().getUsersCollection().clear();

        Seller seller = new Seller("Alice", 1);
        Seller secondSeller = new Seller("Bob", 2);

        // יצירת אסטרטגיית הקריאה עם השירות
        jsonFileReaderStrategy = new JsonFileReaderStrategy(userService);

        // יצירת קובץ JSON לבדיקה
        createTestJsonFile();
    }

    @Test
    void testReadPropertiesFromFile_SuccessfulLoad() {
        List<Property> properties = jsonFileReaderStrategy.readPropertiesFromFile(TEST_JSON_FILE);

        // לוודא שכל הנכסים נטענו
        assertNotNull(properties, "The property list should not be null");
        assertEquals(3, properties.size(), "The number of loaded properties is incorrect");

        // בדיקה של נכס רגיל
        Property firstProperty = properties.get(0);
        assertEquals(10, firstProperty.getStreet());
        assertEquals(5, firstProperty.getAvenue());
        assertEquals(100, firstProperty.getArea());
        assertEquals(500000, firstProperty.getPrice());

        // בדיקה של דירה פנימית
        Property secondProperty = properties.get(1);
        assertTrue(secondProperty instanceof InternalApartment, "The second property should be an internal apartment");
        assertEquals(1, ((InternalApartment) secondProperty).getInternalNumber(), "Internal apartment number mismatch");
    }

    @Test
    void testReadPropertiesFromFile_InvalidFile() {
        List<Property> properties = jsonFileReaderStrategy.readPropertiesFromFile("non_existent.json");

        assertNotNull(properties, "The method should return an empty list instead of null");
        assertTrue(properties.isEmpty(), "The list should be empty for an invalid file");
    }

    @Test
    void testReadPropertiesFromFile_InternalApartmentsLinkedCorrectly() {
        List<Property> properties = jsonFileReaderStrategy.readPropertiesFromFile(TEST_JSON_FILE);

        // מציאת הדירה הראשית
        Property mainApartment = properties.stream()
                .filter(p -> p.getAddress().equals("(10,5)"))
                .findFirst()
                .orElse(null);

        assertNotNull(mainApartment, "Main apartment should exist");

        // לוודא שהדירות הפנימיות נקשרו
        if (mainApartment instanceof Apartment) {
            List<InternalApartment> internalApartments = ((Apartment) mainApartment).getInternalApartments();
            assertEquals(1, internalApartments.size(), "Main apartment should have 1 internal apartment");
            assertEquals(1, internalApartments.get(0).getInternalNumber(), "Internal apartment number should be 1");
        } else {
            fail("Main apartment should be an instance of Apartment");
        }
    }

    /**
     * יוצר קובץ JSON לדוגמא המכיל נכסים ראשיים ודירות פנימיות.
     */
    private void createTestJsonFile() throws IOException {
        File file = new File(TEST_JSON_FILE);
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("""
                [
                    {
                        "street": 10,
                        "avenue": 5,
                        "area": 100,
                        "price": 500000,
                        "isSold": false,
                        "owner ID": 1
                    },
                    {
                        "street": 10,
                        "avenue": 5,
                        "internalNumber": 1,
                        "area": 50,
                        "price": 200000,
                        "isSold": false,
                        "owner ID": 2,
                        "mainApartment": {
                            "address": "(10,5)"
                        }
                    },
                    {
                        "street": 12,
                        "avenue": 6,
                        "area": 120,
                        "price": 700000,
                        "isSold": true,
                        "owner ID": 1
                    }
                ]
                """);
        }
    }
}
