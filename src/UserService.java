import java.util.*;

//deligation
//singleton
public class UserService {
    private static UserService instance;
    private Map<Integer, User> usersCollection;
    private PropertyManager propertyManager = PropertyManager.getInstance();

    private UserService () {
        this.usersCollection = new HashMap<>();
    }
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }
    public boolean compareID (User user1) {
        return usersCollection.containsKey(user1.getId());
    }
    public List<Property> getUserProperties (User user) {
        List<Property> userProperties = new ArrayList<>();
        for (Property property : getAllProperties()) {
            if (property.getOwner() == user) {
                userProperties.add(property);
            }
        }
        return userProperties;
    }
    public Map<Integer, User> getUsersCollection() {
        return usersCollection;
    }
    public void addUser (User user) {
        usersCollection.put(user.getId(), user);
    }

    public List<Property> getAllProperties () {
        return propertyManager.getAllProperties();
    }
}
