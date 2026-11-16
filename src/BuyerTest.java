import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class BuyerTest {
    private Buyer buyer;
    private Realtor realtor;
    private Property property;
    private PropertyService service1, service2, service3;
    private PropertyManager propertyManager;

    @BeforeEach
    void setUp() {
        PropertyManager.getInstance().clearProperties();
        UserService.getInstance().getUsersCollection().clear();

        buyer = new Buyer("John Doe", 101);
        realtor = new Realtor("Alice", 202);
        property = new Apartment(5, 4, 150, 1800000, false, null);

        // שמירת השירותים של המתווך **ברשימה תקנית**
        List<PropertyService> services = realtor.suggestPropertyServices();
        service1 = services.get(0); // com.realestate.services.services.CleaningService
        service2 = services.get(1); // com.realestate.services.ContractGuarantorServices
        service3 = services.get(2); // com.realestate.services.PropertyDesignServices

        propertyManager = PropertyManager.getInstance();
        propertyManager.addProperty(property);
    }



    @Test
    void testChooseServices_ValidSelection() {
        List<Integer> selectedServices = Arrays.asList(0, 2); // בחירת com.realestate.services.services.CleaningService ו- com.realestate.services.PropertyDesignServices
        PropertyWithServices propertyWithServices = buyer.chooseServices(property, realtor, selectedServices);

        assertEquals(2, propertyWithServices.getServices().size(), "The total number of services added is incorrect.");
        assertTrue(propertyWithServices.getServicesDescription().contains(service1.getDescription()),
                "The Cleaning service was not added correctly.");
        assertTrue(propertyWithServices.getServicesDescription().contains(service3.getDescription()),
                "The Property Design service was not added correctly.");
    }




    @Test
    void testChooseServices_InvalidSelectionIgnored() {
        List<Integer> selectedServices = Arrays.asList(0, 5, -1); // רק השירות הראשון תקף
        PropertyWithServices propertyWithServices = buyer.chooseServices(property, realtor, selectedServices);

        assertEquals(1, propertyWithServices.getServices().size(), "Only one valid service should be selected.");
        assertTrue(propertyWithServices.getServicesDescription().contains(service1.getDescription()),
                "The Cleaning service was not added correctly.");
    }



    @Test
    void testBuy_PropertyOwnershipTransferred() {
        List<Integer> selectedServices = Arrays.asList(1, 2);

        buyer.buy(property, realtor, selectedServices);

        assertEquals(buyer, property.getOwner(), "The buyer should be the new owner of the property.");
        assertTrue(property.isSold(), "The property should be marked as 'sold'.");
    }

    @Test
    void testBuy_TotalPriceCalculation() {
        List<Integer> selectedServices = Arrays.asList(0, 1);
        PropertyWithServices propertyWithServices = buyer.chooseServices(property, realtor, selectedServices);

        double expectedTotalPrice = property.getPrice() + service1.getServiceCost() + service2.getServiceCost();
        assertEquals(expectedTotalPrice, propertyWithServices.getTotalPrice(), 0.01, "The final price doesn't seem right.");
    }


    @Test
    void testBuy_PropertyUpdatedInManager() {
        List<Integer> selectedServices = Arrays.asList(0, 2);
        buyer.buy(property, realtor, selectedServices);

        Property updatedProperty = propertyManager.getPropertyByAddress(property.getAddress());
        assertNotNull(updatedProperty, "הנכס צריך להיות במנהל הנכסים.");
        assertEquals(buyer, updatedProperty.getOwner(), "הקונה צריך להיות הבעלים החדש במנהל הנכסים.");
    }
}

