import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Seller seller = new Seller("dan", 123456789);
        Buyer buyer = new Buyer("ron", 24);
        Realtor realtor = new Realtor("zohar", 10);

        UserService userService = UserService.getInstance();

        PropertyManager propertyManager = PropertyManager.getInstance();
        propertyManager.setCollectionStrategy(PropertyCollectionFactory.createCollection("hashMap"));
        propertyManager.setFileReaderStrategy(FileReaderFactory.createFileReader("json"));
        String filePath = "data/properties.json";
        propertyManager.loadPropertiesFromFileAndAddToManager(filePath, false, userService);

        System.out.println(propertyManager.getPropertyByAddress("(5,4)"));
        //buyer.buy(propertyManager.getPropertyByAddress("(5,4)"), realtor);
        System.out.println(propertyManager.getPropertyByAddress("(5,4)"));
        //System.out.println(buyer.getOwnerProperties());

        //System.out.println(userService.getUsersCollection());





        /*propertyManager.printInternalApartmentsDetails();
        List<Property> properties = propertyManager.getUnSoldProperties(propertyManager.getPropertyByAddress("(5,4)"), 8);
        for (Property property : properties) {
            System.out.println(property);
        }
        System.out.println(propertyManager.getPropertyByAddress("(5,4)").getOwner());
        System.out.println(seller.getSoldProperties(propertyManager.getPropertyByAddress("(5,4)"),7));
        *///propertyManager.printProperties();
    }
}

