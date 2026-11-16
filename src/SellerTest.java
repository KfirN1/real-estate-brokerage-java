import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SellerTest {
    private Seller seller;
    private Property property;
    private Realtor realtor;
    private UserService userService = UserService.getInstance();

    @BeforeEach
    void setUp() {
        PropertyManager.getInstance().clearProperties();
        UserService.getInstance().getUsersCollection().clear();

        seller = new Seller("Alice", 1);
        realtor = new Realtor("Charlie", 2);
        property = new Apartment(10, 5, 120, 500000, false, seller);

        PropertyManager.getInstance().addProperty(property);

    }

    @Test
    void testNotifyObservers() {
        seller.addObserver(realtor);
        assertDoesNotThrow(() -> seller.notifyObservers("Property update!"));
    }

    @Test
    void testDeleteProperty() {
        seller.addObserver(realtor);

        assertTrue(userService.getUserProperties(seller).contains(property), "Property should exist before deletion");
        assertTrue(userService.getAllProperties().contains(property), "Property should be in the global list before deletion");

        seller.deleteProperty(property);

        assertFalse(userService.getUserProperties(seller).contains(property), "Property should be removed from user's properties");
        assertFalse(userService.getAllProperties().contains(property), "Property should be removed from all properties");
    }
}
