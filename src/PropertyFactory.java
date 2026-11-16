import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;

import java.util.List;

public class PropertyFactory {
    private final UserService userService;

    public PropertyFactory(UserService userService) {
        this.userService = userService;
    }

    public Property createProperty(int street, int avenue, double area, double price, boolean isSold, Integer ownerId) {
        User owner = getOwnerIfExists(ownerId);

        String address = "(" + street + "," + avenue + ")";
        if (PropertyManager.getInstance().getPropertyByAddress(address) != null) {
            throw new IllegalArgumentException("Property with address " + address + " already exists.");
        }

        Property newProperty = new Apartment(street, avenue, area, price, isSold, owner);
        PropertyManager.getInstance().addProperty(newProperty);
        return newProperty;
    }


    public Property createInternalProperty(Property mainApartment, int internalNumber, double area, double price, boolean isSold, Integer ownerId) {
            if (mainApartment == null || PropertyManager.getInstance().getPropertyByAddress(mainApartment.getAddress()) == null) {
                throw new IllegalArgumentException("Main apartment does not exist in the system.");
            }

            User owner = getOwnerIfExists(ownerId);

        // בדיקות שטח - נבדוק קודם שהשטח לא חורג מהשטח של דירת האם
        if (area > mainApartment.getArea()) {
            throw new IllegalArgumentException("The area of the inner apartment cannot be larger than the area of the main apartment.");
        }

        double totalInternalArea = area;
        if (mainApartment instanceof Apartment) {
            List<InternalApartment> internals = ((Apartment) mainApartment).getInternalApartments();
            for (InternalApartment ia : internals) {
                totalInternalArea += ia.getArea();
            }
        }
        if (totalInternalArea > mainApartment.getArea()) {
            throw new IllegalArgumentException("The total area of the internal apartments (including the new one) cannot be greater than the area of the main apartment.");
        }
        if (mainApartment.getInternalApartments().stream()
                .anyMatch(apartment -> apartment.getInternalNumber() == internalNumber)) {
            throw new IllegalArgumentException("Internal Apartment with address (" + mainApartment.getStreet() +
                    "," + mainApartment.getAvenue() + "," + internalNumber + ") already exists.");
        }


        // יצירת כתובת דירה פנימית
        String internalAddress = mainApartment.getAddress().replace(")", "") + "," + internalNumber + ")";
        Property newProperty = new InternalApartment(mainApartment, internalNumber, area, price, isSold, owner);
        PropertyManager.getInstance().addProperty(newProperty);
        return newProperty;
    }


    private User getOwnerIfExists(Integer ownerId) {
        if (ownerId == null) return null;
        if (!userService.getUsersCollection().containsKey(ownerId)) {
            throw new IllegalArgumentException("No user found with ID: " + ownerId);
        }
        User owner = userService.getUsersCollection().get(ownerId);
        if (owner instanceof Buyer) {
            throw new IllegalArgumentException("A buyer cannot own a property! ID: " + ownerId);
        }
        return owner;
    }
}
