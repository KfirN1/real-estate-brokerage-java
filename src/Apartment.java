import java.util.ArrayList;
import java.util.List;

public class Apartment extends Property{
    public Apartment (int street, int avenue, double area, double price, boolean isSold, User owner){
        super(street, avenue, area, price, isSold, owner);
    }

    @Override
    public String getAddress() {
            return "(" + getStreet() + "," + getAvenue() + ")";
    }

    @Override
    public String toString() {
        return String.format("Apartment at %s | Area: %.2f m² | Price: $%.2f | Status: %s | Owner: %s ",
                getAddress(), getArea(), getPrice(), isSold() ? "Sold" : "Available", (getOwner()!= null ? getOwner().getName() : "none"));
    }
}
