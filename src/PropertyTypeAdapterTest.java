import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.*;

public class PropertyTypeAdapterTest {

    private UserService userService;
    private PropertyManager propertyManager;
    private PropertyTypeAdapter adapter;
    private Gson gson;
    private Seller seller;

    @BeforeEach
    public void setUp() {
        userService = UserService.getInstance();
        userService.getUsersCollection().clear();
        propertyManager = PropertyManager.getInstance();
        propertyManager.getPropertyCollection().clear();
        seller = new Seller("TestSeller", 100);
        userService.addUser(seller);
        adapter = new PropertyTypeAdapter(userService);
        gson = new GsonBuilder()
                .registerTypeAdapter(Property.class, adapter)
                .create();
    }

    @Test
    public void testWriteApartment() throws IOException {
        Apartment apt = new Apartment(5, 4, 150, 1800000, false, seller);

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        adapter.write(jsonWriter, apt);
        jsonWriter.close();

        String jsonOutput = stringWriter.toString();
        assertTrue(jsonOutput.contains("\"street\":5"), "JSON should contain street 5");
        assertTrue(jsonOutput.contains("\"avenue\":4"), "JSON should contain avenue 4");
        assertTrue(jsonOutput.contains("\"area\":150"), "JSON should contain area 150");
        assertTrue(jsonOutput.contains("\"price\":1800000"), "JSON should contain price 1800000");
        assertTrue(jsonOutput.contains("\"isSold\":false"), "JSON should indicate isSold false");
        assertTrue(jsonOutput.contains("\"owner ID\":100"), "JSON should contain owner ID 100");
    }

    @Test
    public void testReadApartment() throws IOException {
        String json = "{"
                + "\"street\":5,"
                + "\"avenue\":4,"
                + "\"area\":150,"
                + "\"price\":1800000,"
                + "\"isSold\":false,"
                + "\"owner ID\":100"
                + "}";

        JsonReader jsonReader = new JsonReader(new StringReader(json));
        Property property = adapter.read(jsonReader);

        assertNotNull(property, "Property should not be null");
        assertTrue(property instanceof Apartment, "Property should be an instance of Apartment");
        assertEquals(5, property.getStreet());
        assertEquals(4, property.getAvenue());
        assertEquals(150, property.getArea());
        assertEquals(1800000, property.getPrice());
        assertFalse(property.isSold());
        assertNotNull(property.getOwner(), "Owner should not be null");
        assertEquals("TestSeller", property.getOwner().getName());
    }

    @Test
    public void testReadInternalApartment() throws IOException {
        Apartment mainApartment = new Apartment(5, 4, 150, 1800000, false, seller);
        propertyManager.addProperty(mainApartment);
        String json = "{"
                + "\"street\":5,"
                + "\"avenue\":4,"
                + "\"area\":30,"
                + "\"price\":600000,"
                + "\"isSold\":false,"
                + "\"owner ID\":100,"
                + "\"internalNumber\":1,"
                + "\"mainApartment\": {"
                + "\"address\": \"(5,4)\""
                + "}}";

        JsonReader jsonReader = new JsonReader(new StringReader(json));
        Property property = adapter.read(jsonReader);

        assertNotNull(property, "Property should not be null");
        assertTrue(property instanceof InternalApartment, "Property should be an instance of InternalApartment");
        InternalApartment internal = (InternalApartment) property;
        assertEquals(1, internal.getInternalNumber());

        String expectedAddress = "(5,4,1)";
        assertEquals(expectedAddress, internal.getAddress(), "Internal apartment address should be " + expectedAddress);
    }
}
