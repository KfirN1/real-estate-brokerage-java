import java.util.List;

public class Buyer extends User{
    public Buyer(String name, int id) {
        super(name, id);
    }

    public PropertyWithServices chooseServices(Property property, Realtor realtor, List<Integer> selectedServices) {
        PropertyWithServices propertyWithServices = new PropertyWithServices(property);
        List<PropertyService> offeredServices = realtor.suggestPropertyServices();

        // הוספת שירותים מבלי להוסיף כפילויות
        for (int index : selectedServices) {
            if (index >= 0 && index < offeredServices.size()) {
                PropertyService selectedService = offeredServices.get(index);
                if (!propertyWithServices.getServices().contains(selectedService)) { // מניעת כפילויות
                    propertyWithServices.addService(selectedService);
                }
            }
        }

        return propertyWithServices;
    }


    public void buy(Property property, Realtor realtor, List<Integer> selectedServices) {
        PropertyWithServices propertyWithServices = chooseServices(property, realtor, selectedServices);
        propertyWithServices.setOwner(this);
        propertyWithServices.setSold(true);
        PropertyManager.getInstance().updateProperty(propertyWithServices);
        Property updated = PropertyManager.getInstance().getPropertyByAddress(property.getAddress());
    }


    @Override
    public String toString() {
        return "Buyer: " + getName() + " (ID: " + getId() + ")";
    }
}
