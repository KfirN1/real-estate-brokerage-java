import java.util.*;
import java.util.stream.Collectors;

//singleton
public class PropertyManager {
    private static PropertyManager instance;
    private FileReaderStrategy fileReaderStrategy;
    private PropertyCollectionStrategy propertyCollection;


    private PropertyManager () {
        this.propertyCollection = new HashMapPropertyCollectionStrategy();
    }

    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }
    public void addProperty (Property property) {
        propertyCollection.addProperty(property);
    }
    public void removeProperty (Property property) {
        propertyCollection.removeProperty(property);
    }
    public void updateProperty(Property updatedProperty) {
        Property existingProperty = getPropertyByAddress(updatedProperty.getAddress());
        if (existingProperty != null) {
            removeProperty(existingProperty);
        }
        addProperty(updatedProperty);
    }
    public void clearProperties() {
        if (propertyCollection != null) {
            propertyCollection.clear();
        }
    }



    public List<Property> getAllProperties () {
        return propertyCollection.getAllProperties();
    }

    public PropertyCollectionStrategy getPropertyCollection() {
        return propertyCollection;
    }

    public void printProperties () {
        for (Property property : getAllProperties()) {
            System.out.println(property);
        }
    }
    public Property getPropertyByAddress(String address) {
        for (Property property : getAllProperties()) {
            if (property.getAddress().equals(address)) {
                return property;
            }
        }
        return null;
    }


    private double getRadius(Property property1, Property property2) {
        return Math.abs(property1.getAvenue() - property2.getAvenue()) +
                Math.abs(property1.getStreet() - property2.getStreet());
    }

    private boolean isWithInBounds (Property property1, Property property2, double radius) {
        double totalDistance = getRadius(property1,property2);
        return totalDistance <= radius;
    }

    public List<Property> getSoldProperties (Property property1, double radius) {
        return propertyCollection.getSoldProperties().stream().filter(property -> isWithInBounds(property ,property1, radius)).collect(Collectors.toList());
    }
    public List<Property> getUnSoldProperties (Property property1, double radius) {
        return propertyCollection.getUnSoldProperties().stream().filter(property -> isWithInBounds(property, property1, radius)).collect(Collectors.toList());
    }
    public double getAverageRegionalPrices(Property property1, double radius) {
        List<Property> propertiesInRadius = propertyCollection.getAllProperties().stream()
                .filter(property -> isWithInBounds(property, property1, radius)).collect(Collectors.toList());
        if (propertiesInRadius.isEmpty()) {
            return 0;
        }

        double totalPricePerSquarMeter = 0;
        int count = 0;

        for (Property property : propertiesInRadius) {
            double pricePerSquareMeter = property.getPrice()/property.getArea();
            totalPricePerSquarMeter += pricePerSquareMeter;
            count++;
        }
        return count > 0 ? totalPricePerSquarMeter/count : 0;
    }
    public List<Property> getPropertiesByPricePerSquareMeter(Property property1, double radius, String comparison) {
        double pricePerSquareMeter = property1.getPrice() / property1.getArea();

        List<Property> propertiesInRadius = propertyCollection.getAllProperties().stream()
                .filter(property -> isWithInBounds(property, property1, radius))
                .collect(Collectors.toList());

        List<Property> filteredProperties = new ArrayList<>();

        for (Property property : propertiesInRadius) {
            double currentPricePerSquareMeter = property.getPrice() / property.getArea();
            switch (comparison) {
                case "higher":
                    if (currentPricePerSquareMeter > pricePerSquareMeter) {
                        filteredProperties.add(property);
                    }
                    break;
                case "lower":
                    if (currentPricePerSquareMeter < pricePerSquareMeter) {
                        filteredProperties.add(property);
                    }
                    break;
                case "equal":
                    if (currentPricePerSquareMeter == pricePerSquareMeter) {
                        filteredProperties.add(property);
                    }
                    break;
                default:
                    System.out.println("Invalid comparison value.");
                    break;
            }
        }

        return filteredProperties;
    }

    public void setFileReaderStrategy(FileReaderStrategy newFileReader) {
        if (newFileReader == null) {
            throw new IllegalArgumentException("File reader strategy cannot be null");
        }
        if (this.propertyCollection != null) {
            this.propertyCollection.clear();
        }
        this.fileReaderStrategy = newFileReader;
    }
    public void loadPropertiesFromFileAndAddToManager(String filePath, boolean clearBeforeLoading, UserService userService) {
        if (clearBeforeLoading) {
            propertyCollection.clear();
        }
        if (userService.getUsersCollection().isEmpty()) {
            throw new IllegalStateException("Error: There are no users in the system! Users must be created before reading the assets.");
        }

        List<Property> properties = fileReaderStrategy.readPropertiesFromFile(filePath);

        // נוודא שהנכסים נטענו כראוי
        if (properties.isEmpty()) {
            throw new IllegalStateException("No properties were loaded from the file. Please check the file contents.");
        }

        // הוספת הנכסים ל-PropertyManager
        properties.forEach(this::addProperty);
    }



    public void setCollectionStrategy(PropertyCollectionStrategy newStrategy) {
        if (newStrategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }

        List<Property> properties = propertyCollection.getAllProperties();
        for (Property property : properties) {
            newStrategy.addProperty(property);
        }

        this.propertyCollection = newStrategy;
    }


    public void printInternalApartmentsDetails(Property property) {
        List<InternalApartment> internalApartments = propertyCollection.getAllProperties().stream()
            .filter(p -> p instanceof InternalApartment)
            .map(p -> (InternalApartment) p)
            .filter(internalApartment -> internalApartment.getStreet() == property.getStreet() && internalApartment.getAvenue() == property.getAvenue())
            .collect(Collectors.toList());

            if (internalApartments.isEmpty()) {
                System.out.println("There are no internal apartments for this property.");
            }
            else {
                StringBuilder details = new StringBuilder();
                details.append("Internal apartments for property at " + property.getAddress() + ": \n");
                for (InternalApartment apartment : internalApartments) {
                    details.append(apartment.toString()).append("\n");
                }
                System.out.println(details.toString());
            }
        }
}
