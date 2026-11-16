import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Property {
    private final int street;
    private final int avenue;
    private double area;
    private double price;
    private boolean isSold;
    private User owner;
    private List<InternalApartment> internalApartments = new ArrayList<>();

    public Property(int street, int avenue, double area, double price, boolean isSold, User owner){
        this.area = area;
        this.price = price;
        this.isSold = isSold;
        this.street = street;
        this.avenue = avenue;
        this.owner = owner;
    }

    public List<InternalApartment> getInternalApartments() {
        return internalApartments;
    }
    public void addInternalApartment (InternalApartment internalApartment) {
        double totalInternalArea = internalApartment.getArea();
        for (InternalApartment internalApt : internalApartments) {
            totalInternalArea += internalApt.getArea();
        }
        if (totalInternalArea > this.getArea()) {
            throw new IllegalArgumentException("Total internal apartment area exceeds the main property's area.");
        }
        internalApartments.add(internalApartment);
    }

    public int getStreet() {
        return street;
    }

    public int getAvenue() {
        return avenue;
    }

    public double getPrice() {
        return price;
    }

    public double getArea() {
        return area;
    }

    public boolean isSold () {
        return isSold;
    }

    public User getOwner() {
        return owner;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public abstract String getAddress ();
}

