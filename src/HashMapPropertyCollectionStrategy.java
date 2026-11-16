import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HashMapPropertyCollectionStrategy implements PropertyCollectionStrategy {
    private Map<Boolean, List<Property>> properties;
    public HashMapPropertyCollectionStrategy() {
        properties = new HashMap<>();
        if (!properties.containsKey(true)) {
            properties.put(true, new ArrayList<>());
        }
        if (!properties.containsKey(false)) {
            properties.put(false, new ArrayList<>());
        }
    }

    @Override
    public void addProperty(Property property) {
        List<Property> propertyList = properties.get(property.isSold());
        if (propertyList == null) {
            propertyList = new ArrayList<>();
            properties.put(property.isSold(), propertyList);
        }
        propertyList.add(property);
    }

    @Override
    public void updateProperty(Property updatedProperty) {
        removeProperty(updatedProperty);
        addProperty(updatedProperty);
    }


    @Override
    public void removeProperty(Property property) {
        properties.values().forEach(list -> list.removeIf(p -> p.getAddress().equals(property.getAddress())));
    }


    @Override
    public List<Property> getAllProperties() {
        List<Property> allProperties = new ArrayList<>();
        allProperties.addAll(properties.get(true));
        allProperties.addAll(properties.get(false));
        return allProperties;
    }

    @Override
    public List<Property> getSoldProperties() {
        return properties.get(true);
    }

    @Override
    public List<Property> getUnSoldProperties() {
        return properties.get(false);
    }

    @Override
    public void clear() {
        properties.get(true).clear();
        properties.get(false).clear();
    }
}
