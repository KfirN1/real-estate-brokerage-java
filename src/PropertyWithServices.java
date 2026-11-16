import java.util.ArrayList;
import java.util.List;
//decorator
public class PropertyWithServices extends Property {
    private Property baseProperty;
    private List<PropertyService> services = new ArrayList<>();

    public PropertyWithServices(Property baseProperty) {
        super(baseProperty.getStreet(), baseProperty.getAvenue(), baseProperty.getArea(), baseProperty.getPrice(), baseProperty.isSold(), baseProperty.getOwner());
        this.baseProperty = baseProperty;
    }
    public void setOwner(Buyer buyer) {
        super.setOwner(buyer);
        if (baseProperty != null) {
            baseProperty.setOwner(buyer);
        }
    }
    public void setSold(boolean sold) {
        super.setSold(sold);
        if (baseProperty != null) {
            baseProperty.setSold(sold);
        }
    }



    public List<PropertyService> getServices() {
        return services;
    }


    public void addService(PropertyService service) {
        services.add(service);
    }

    public double getTotalPrice() {
        double totalPrice = baseProperty.getPrice();
        for (PropertyService service : services) {
            totalPrice += service.getServiceCost();
        }
        return totalPrice;
    }

    public List<String> getServicesDescription() {
        List<String> descriptions = new ArrayList<>();
        for (PropertyService service : services) {
            descriptions.add(service.getDescription());
        }
        return descriptions;
    }
    @Override
    public String getAddress() {
        return baseProperty.getAddress();
    }
    @Override
    public String toString() {
        return String.format("%s | Services: %s | Total Price: $%.2f",
                baseProperty.toString(),
                services.isEmpty() ? "None" : services.toString(),
                getTotalPrice());
    }
}
