import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class PropertyFactoryTest {
    private PropertyFactory propertyFactory;
    private UserService userService = UserService.getInstance();
    private PropertyManager propertyManager = PropertyManager.getInstance();

    @BeforeEach
    void setUp() {
        PropertyManager.getInstance().clearProperties();
        UserService.getInstance().getUsersCollection().clear();

        // יצירת משתמשים
        Seller seller1 = new Seller("Alice", 1);
        Seller seller2 = new Seller("Charlie", 3);
        Buyer buyer = new Buyer("Bob", 2);

        // יצירת Factory
        propertyFactory = new PropertyFactory(userService);
    }

    @Test
    void testCreateProperty_Success() {
        Property property = propertyFactory.createProperty(10, 5, 100, 500000, false, 1);

        assertNotNull(property, "Property should be created successfully");
        assertEquals(10, property.getStreet(), "Street number mismatch");
        assertEquals(5, property.getAvenue(), "Avenue number mismatch");
        assertEquals(100, property.getArea(), "Area mismatch");
        assertEquals(500000, property.getPrice(), "Price mismatch");
        assertFalse(property.isSold(), "Property should not be sold initially");
        assertNotNull(property.getOwner(), "Owner should not be null");
        assertEquals(1, property.getOwner().getId(), "Owner ID mismatch");
    }

    @Test
    void testCreateProperty_WithoutOwner() {
        Property property = propertyFactory.createProperty(12, 8, 120, 600000, false, null);

        assertNotNull(property, "Property should be created successfully without an owner");
        assertNull(property.getOwner(), "Owner should be null for properties without an assigned owner");
    }

    @Test
    void testCreateProperty_WithDuplicateAddress() {
        propertyFactory.createProperty(15, 7, 90, 400000, false, 1);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                propertyFactory.createProperty(15, 7, 80, 300000, false, 3));

        assertEquals("Property with address (15,7) already exists.", exception.getMessage());
    }


    @Test
    void testCreateProperty_BuyerAsOwner_Failure() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                propertyFactory.createProperty(20, 10, 110, 700000, false, 2)); // ID 2 הוא קונה

        assertEquals("A buyer cannot own a property! ID: 2", exception.getMessage());
    }

    @Test
    void testCreateInternalProperty_Success() {
        Property mainProperty = propertyFactory.createProperty(10, 5, 200, 800000, false, 1);
        propertyManager.addProperty(mainProperty);

        Property internalProperty = propertyFactory.createInternalProperty(mainProperty, 1, 80, 300000, false, 3);

        assertNotNull(internalProperty, "Internal Apartment should be created successfully");
        assertTrue(internalProperty instanceof InternalApartment, "Should be an instance of InternalApartment");
        assertEquals(1, ((InternalApartment) internalProperty).getInternalNumber(), "Internal number mismatch");
        assertEquals(mainProperty, ((InternalApartment) internalProperty).getMainApartment(), "Main apartment mismatch");
        assertEquals(3, internalProperty.getOwner().getId(), "Owner ID mismatch for internal apartment");
    }

    @Test
    void testCreateInternalProperty_DuplicateInternalNumber() {
        Property mainProperty = propertyFactory.createProperty(10, 5, 200, 800000, false, 1);
        propertyManager.addProperty(mainProperty);

        propertyFactory.createInternalProperty(mainProperty, 1, 80, 300000, false, 3);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                propertyFactory.createInternalProperty(mainProperty, 1, 70, 250000, false, 3));

        assertEquals("Internal Apartment with address (10,5,1) already exists.", exception.getMessage());
    }

    @Test
    void testCreateInternalProperty_ExceedingArea() {
        Property mainProperty = propertyFactory.createProperty(10, 5, 150, 700000, false, 1);
        propertyManager.addProperty(mainProperty);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                propertyFactory.createInternalProperty(mainProperty, 1, 160, 300000, false, 3));

        assertEquals("The area of the inner apartment cannot be larger than the area of the main apartment.", exception.getMessage());
    }

    @Test
    void testCreateInternalProperty_ExceedingTotalInternalArea() {
        Property mainProperty = propertyFactory.createProperty(10, 5, 150, 700000, false, 1);
        propertyManager.addProperty(mainProperty);

        propertyFactory.createInternalProperty(mainProperty, 1, 100, 300000, false, 3);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                propertyFactory.createInternalProperty(mainProperty, 2, 60, 250000, false, 3));

        assertEquals("The total area of the internal apartments (including the new one) cannot be greater than the area of the main apartment.", exception.getMessage());
    }

    @Test
    void testCreateInternalProperty_MainApartmentDoesNotExist() {
        // יצירת דירה שלא נוספה ל-PropertyManager
        Property fakeMainApartment = new Apartment(10, 5, 200, 800000, false, userService.getUsersCollection().get(1));

        // ניסיון להוסיף דירה פנימית על בסיס הדירה המזויפת
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                propertyFactory.createInternalProperty(fakeMainApartment, 1, 80, 300000, false, 3));

        assertEquals("Main apartment does not exist in the system.", exception.getMessage());
    }



    @Test
    void testCreateInternalProperty_BuyerAsOwner_Failure() {
        Property mainProperty = propertyFactory.createProperty(10, 5, 150, 600000, false, 1);
        propertyManager.addProperty(mainProperty);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                propertyFactory.createInternalProperty(mainProperty, 1, 50, 200000, false, 2)); // ID 2 הוא קונה

        assertEquals("A buyer cannot own a property! ID: 2", exception.getMessage());
    }
}
