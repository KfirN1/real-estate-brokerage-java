import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PropertyManagerTest {

    private PropertyManager propertyManager;
    private UserService userService;
    private Seller seller;
    private Apartment apt1, apt2, apt3;

    @BeforeEach
    public void setUp() {
        propertyManager = PropertyManager.getInstance();
        userService = UserService.getInstance();

        propertyManager.getPropertyCollection().clear();

        userService.getUsersCollection().clear();

        seller = new Seller("Dan", 123456789);
        userService.addUser(seller);

        apt1 = new Apartment(5, 4, 150, 1800000, false, seller);
        apt2 = new Apartment(5, 5, 100, 1200000, true, seller);
        apt3 = new Apartment(6, 4, 200, 2500000, false, seller);

        propertyManager.addProperty(apt1);
        propertyManager.addProperty(apt2);
        propertyManager.addProperty(apt3);
    }

    @Test
    public void testAddProperty() {
        List<Property> allProps = propertyManager.getAllProperties();
        assertEquals(3, allProps.size(), "There should be 3 properties in the collection.");
        assertTrue(allProps.contains(apt1));
        assertTrue(allProps.contains(apt2));
        assertTrue(allProps.contains(apt3));
    }

    @Test
    public void testUpdateProperty() {
        apt1.setPrice(1900000);
        propertyManager.updateProperty(apt1);

        Property updated = propertyManager.getPropertyByAddress(apt1.getAddress());
        assertNotNull(updated, "Updated property should not be null");
        assertEquals(1900000, updated.getPrice(), "The price should be updated to 1900000.");
    }

    @Test
    public void testGetPropertyByAddress() {
        Property found = propertyManager.getPropertyByAddress(apt2.getAddress());
        assertNotNull(found, "Property should be located by address");
        assertEquals(apt2.getPrice(), found.getPrice(), "The price should match.");
    }

    @Test
    public void testGetSoldAndUnSoldProperties() {
        List<Property> soldProps = propertyManager.getSoldProperties(apt1, 10);
        List<Property> unsoldProps = propertyManager.getUnSoldProperties(apt1, 10);

        assertEquals(1, soldProps.size(), "There should be one sold property.");
        assertTrue(soldProps.contains(apt2));
        assertEquals(2, unsoldProps.size(), "There should be 2 unsold properties.");
        assertTrue(unsoldProps.contains(apt1));
        assertTrue(unsoldProps.contains(apt3));
    }

    @Test
    public void testGetAverageRegionalPrices() {
        double avg = propertyManager.getAverageRegionalPrices(apt1, 10);
        assertEquals(12166.67, avg, 1, "The average should be around 12166.67");
    }

    @Test
    public void testGetPropertiesByPricePerSquareMeter() {
        List<Property> higher = propertyManager.getPropertiesByPricePerSquareMeter(apt1, 10, "higher");
        assertEquals(1, higher.size(), "There should  be one property with a higher price per square meter.");
                assertTrue(higher.contains(apt3));

        List<Property> equal = propertyManager.getPropertiesByPricePerSquareMeter(apt1, 10, "equal");
        assertEquals(2, equal.size(), "There should be two properties with a price per square meter equal to 12,000.");
                assertTrue(equal.contains(apt1));
        assertTrue(equal.contains(apt2));

        List<Property> lower = propertyManager.getPropertiesByPricePerSquareMeter(apt1, 10, "lower");
        assertEquals(0, lower.size(), "There are no properties with a price per square meter lower than 12,000.");
    }
}
