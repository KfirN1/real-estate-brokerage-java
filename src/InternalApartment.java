import java.util.List;

public class InternalApartment extends Property{
    private Property mainApartment;
    private int internalNumber;
    public InternalApartment (Property mainApartment, int internalNumber, double area, double price, boolean isSold, User owner) {
        super(mainApartment.getStreet(), mainApartment.getAvenue(), area, price , isSold, owner);
        this.mainApartment = mainApartment;
        this.internalNumber = internalNumber;
        mainApartment.addInternalApartment(this);
    }

    public int getInternalNumber() {
        return internalNumber;
    }

    @Override
    public String getAddress() {
        return mainApartment.getAddress().replace(")", "") + "," + internalNumber + ")";
    }

    @Override
    public String toString() {
        return String.format("Internal Apartment #%d at %s | Area: %.2f m² | Price: $%.2f | Status: %s | Owner: %s",
                internalNumber, getAddress(), getArea(), getPrice(), isSold() ? "Sold" : "Available", (getOwner() != null ? getOwner().getName() : "none"));
    }

    public Property getMainApartment() {
        return mainApartment;
    }
}
