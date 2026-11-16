import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RealtorTest {
    private Realtor realtor;
    private Property property;
    private PropertyService cleaningService, contractService, designService, transportService;

    @BeforeEach
    void setUp() {
        realtor = new Realtor("John Doe", 100);

        property = new Apartment(10, 5, 150, 700000, false, new Seller("Alice", 1));

        cleaningService = new CleaningService();
        contractService = new ContractGuarantorServices();
        designService = new PropertyDesignServices();
        transportService = new TransportServices();
    }

    @Test
    void testUpdateDetails_Success() {
        realtor.updateDetails(property, 750000, true);

        assertEquals(750000, property.getPrice(), "Price should be updated");
        assertTrue(property.isSold(), "Property should be marked as sold");
    }

    @Test
    void testUpdateDetails_FailsWhenSold() {
        property.setSold(true);

        Exception exception = assertThrows(IllegalStateException.class, () ->
                realtor.updateDetails(property, 750000, false));

        assertEquals("Property sold, can't edit property details", exception.getMessage());
    }

    @Test
    void testUpdate() {
        String expectedMessage = "The realtor John Doe received an update: Property status changed!";

        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        realtor.update("Property status changed!");

        assertTrue(outContent.toString().contains(expectedMessage), "Update message should be printed correctly");
    }
}
