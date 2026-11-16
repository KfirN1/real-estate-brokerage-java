;

public class CleaningService implements PropertyService {
    @Override
    public double getServiceCost() {
        return 80;
    }

    @Override
    public String getDescription() {
        return "Cleaning service";
    }
    @Override
    public String toString() {
        return getDescription();
    }
}
