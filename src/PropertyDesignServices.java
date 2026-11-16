public class PropertyDesignServices implements PropertyService {
    @Override
    public double getServiceCost() {
        return 100;
    }

    @Override
    public String getDescription() {
        return "Property design services";
    }
    @Override
    public String toString() {
        return getDescription();
    }
}
