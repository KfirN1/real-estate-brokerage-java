import java.util.Arrays;
import java.util.List;

public class Realtor extends User implements observer {
    public Realtor(String name, int id) {
        super(name, id);
    }

    public void updateDetails (Property property, double newPrice, boolean newStatus) {
        if (property.isSold()){
            throw new IllegalStateException ("Property sold, can't edit property details");
        }
        property.setPrice(newPrice);
        property.setSold(newStatus);
    }
    public List<PropertyService> suggestPropertyServices () {
        return Arrays.asList(
                new CleaningService(),
                new ContractGuarantorServices(),
                new PropertyDesignServices(),
                new TransportServices()
        );
    }

    @Override
    public void update(String message) {
        System.out.println("The realtor " + getName() + " received an update: " + message);
    }
    @Override
    public String toString() {
        return "Realtor: " + getName() + " (ID: " + getId() + ")";
    }
}
