
public class TransportServices implements PropertyService {
    @Override
    public double getServiceCost() {
        return 120;
    }

    @Override
    public String getDescription() {
        return "Transport services";
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
