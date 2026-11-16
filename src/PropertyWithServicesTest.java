import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class PropertyWithServicesTest {
    private Property baseProperty;
    private PropertyWithServices propertyWithServices;
    private PropertyService cleaningService = new CleaningService();
    private PropertyService transportServices = new TransportServices();
    private PropertyService contractLawyerService = new ContractGuarantorServices();

    @BeforeEach
    void setUp() {
        baseProperty = new Apartment(10, 5, 100, 500000, false, new Seller("Alice", 1));
        propertyWithServices = new PropertyWithServices(baseProperty);

    }

    @Test
    void testInitialSetup() {
        assertEquals(baseProperty.getStreet(), propertyWithServices.getStreet());
        assertEquals(baseProperty.getAvenue(), propertyWithServices.getAvenue());
        assertEquals(baseProperty.getArea(), propertyWithServices.getArea());
        assertEquals(baseProperty.getPrice(), propertyWithServices.getPrice());
        assertEquals(baseProperty.isSold(), propertyWithServices.isSold());
        assertEquals(baseProperty.getOwner(), propertyWithServices.getOwner());
    }

    @Test
    void testAddService() {
        propertyWithServices.addService(cleaningService);
        assertEquals(1, propertyWithServices.getServices().size());
        assertTrue(propertyWithServices.getServices().contains(cleaningService));
    }

    @Test
    void testGetTotalPrice_WithServices() {
        propertyWithServices.addService(cleaningService);
        propertyWithServices.addService(transportServices);
        propertyWithServices.addService(contractLawyerService);

        double expectedTotalPrice = baseProperty.getPrice() + cleaningService.getServiceCost()
                + transportServices.getServiceCost() + contractLawyerService.getServiceCost();

        assertEquals(expectedTotalPrice, propertyWithServices.getTotalPrice(), 0.01);
    }

    @Test
    void testGetTotalPrice_WithoutServices() {
        assertEquals(baseProperty.getPrice(), propertyWithServices.getTotalPrice());
    }

    @Test
    void testGetServicesDescription() {
        propertyWithServices.addService(cleaningService);
        propertyWithServices.addService(transportServices);

        List<String> descriptions = propertyWithServices.getServicesDescription();
        assertEquals(2, descriptions.size());
        assertTrue(descriptions.contains("Cleaning"));
        assertTrue(descriptions.contains("Moving"));
    }

    @Test
    void testSetOwner_SynchronizationWithBaseProperty() {
        Buyer buyer = new Buyer("Bob", 2);
        propertyWithServices.setOwner(buyer);
        propertyWithServices.setSold(true);

        assertEquals(buyer, propertyWithServices.getOwner());
        assertEquals(buyer, baseProperty.getOwner());
        assertTrue(propertyWithServices.isSold());
        assertTrue(baseProperty.isSold());
    }
}


