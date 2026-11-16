public class ContractGuarantorServices implements PropertyService {
    @Override
    public double getServiceCost() {
        return 500;
    }

    @Override
    public String getDescription() {
        return "Contract guarantor services";
    }
    @Override
    public String toString() {
        return getDescription();
    }
}
