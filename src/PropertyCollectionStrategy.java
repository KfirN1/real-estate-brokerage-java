import java.util.List;

public interface PropertyCollectionStrategy {
    void addProperty (Property property);
    void updateProperty (Property property);
    void removeProperty (Property property);
    List<Property> getAllProperties ();
    public List<Property> getSoldProperties ();
    public List<Property> getUnSoldProperties ();
    public void clear();
    }
