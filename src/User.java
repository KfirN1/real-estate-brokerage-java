import java.util.ArrayList;
import java.util.List;

public abstract class User {
    private String name;
    private final int id;
    private UserService userService = UserService.getInstance();
    private PropertyManager propertyManager = PropertyManager.getInstance();

    public User (String name, int id) {
        this.name = name;
        this.id = id;
        if (userService.compareID(this)) {
            throw new IllegalArgumentException("User with ID " + id + " already exists!");
        }
        userService.addUser(this);
    }

    public UserService getUserService() {
        return userService;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public List<Property> getOwnerProperties () {
        return userService.getUserProperties(this);
    }

    public double getAverageRegionalPrices(Property property, double radius) {
        return propertyManager.getAverageRegionalPrices(property, radius);
    }

    public List<Property> getSoldProperties (Property property, double radius) {
        return propertyManager.getSoldProperties(property, radius);
    }

    public List<Property> getUnSoldProperties (Property property, double radius) {
        return propertyManager.getUnSoldProperties(property, radius);
    }
    public List<Property> getPropertiesByPricePerSquareMeter (Property property, double radius, String comparison) {
        return propertyManager.getPropertiesByPricePerSquareMeter(property, radius, comparison);
    }

    public void viewPropertyDetails (Property property) {
        propertyManager.getPropertyByAddress(property.getAddress());
    }
    public Property getPropertyByAddress(String address) {
        return propertyManager.getPropertyByAddress(address);
    }

}
