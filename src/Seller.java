import java.util.ArrayList;
import java.util.List;

public class Seller extends User {
    private List<observer> observers = new ArrayList<>();
    public Seller(String name, int id) {
        super(name, id);
    }

    public List<observer> getObservers() {
        return observers;
    }

    public void addObserver (observer observer) {
        observers.add(observer);
    }
    public void removeObserver (observer observer) {
        observers.remove(observer);
    }
    protected void notifyObservers (String message) {
        for (observer observer : observers) {
            observer.update(message);
        }
    }

    public void deleteProperty (Property property) {
        getUserService().getUserProperties(this).remove(property);
        PropertyManager.getInstance().removeProperty(property);
        notifyObservers("property " + property.getAddress() + " sold.");
    }
    @Override
    public String toString() {
        return "Seller: " + getName() + " (ID: " + getId() + ")";
    }
}
