public class PropertyCollectionFactory {
    public static PropertyCollectionStrategy createCollection(String type) {
        return switch (type.toLowerCase()) {
            case "hashmap" -> new HashMapPropertyCollectionStrategy();
            default -> throw new IllegalArgumentException("Unknown collection type: " + type);
        };
    }
}
