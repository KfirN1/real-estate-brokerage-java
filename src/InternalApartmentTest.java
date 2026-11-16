import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InternalApartmentTest {

    @BeforeEach
    public void setUp () {
        PropertyManager.getInstance().getAllProperties().clear();
        UserService.getInstance().getUsersCollection().clear();
    }

    @Test
    public void testGetAddressOneLevel() {
        Seller seller = new Seller("Dan", 123456789);
        Apartment baseApartment = new Apartment(5, 4, 150, 1800000, false, seller);
        InternalApartment internal1 = new InternalApartment(baseApartment, 1, 30, 600000, false, seller);

        String expectedAddress = "(5,4,1)";
        assertEquals(expectedAddress, internal1.getAddress(), "The internal apartment address should be " + expectedAddress);
    }

    @Test
    public void testGetAddressTwoLevels() {
        Seller seller = new Seller("Dan", 123456789);
        Apartment baseApartment = new Apartment(5, 4, 150, 1800000, false, seller);
        InternalApartment internal1 = new InternalApartment(baseApartment, 1, 30, 600000, false, seller);
        InternalApartment internal2 = new InternalApartment(internal1, 2, 15, 300000, false, seller);

        String expectedAddress = "(5,4,1,2)";
        assertEquals(expectedAddress, internal2.getAddress(), "The second level address should be " + expectedAddress);
    }
}
