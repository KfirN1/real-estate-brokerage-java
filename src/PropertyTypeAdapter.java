import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class PropertyTypeAdapter extends TypeAdapter<Property> {
    private final Gson gson;
    private final UserService userService;

    public PropertyTypeAdapter(UserService userService) {
        this.gson = new GsonBuilder().create();
        this.userService = userService;
    }

    @Override
    public void write(JsonWriter out, Property value) throws IOException {
        out.beginObject();
        out.name("street").value(value.getStreet());
        out.name("avenue").value(value.getAvenue());
        out.name("area").value(value.getArea());
        out.name("price").value(value.getPrice());
        out.name("isSold").value(value.isSold());

        if (value.getOwner() != null) {
            out.name("owner ID").value(value.getOwner().getId());
        }

        if (value instanceof InternalApartment) {
            InternalApartment internal = (InternalApartment) value;
            out.name("internalNumber").value(internal.getInternalNumber());
            out.name("mainApartment").beginArray().value(internal.getMainApartment().getAddress()).endArray();
            out.endObject();
        }

        out.endObject();
    }

    @Override
    public Property read(JsonReader in) throws IOException {
        JsonObject jsonObject = JsonParser.parseReader(in).getAsJsonObject();

        int street = jsonObject.get("street").getAsInt();
        int avenue = jsonObject.get("avenue").getAsInt();
        double area = jsonObject.get("area").getAsDouble();
        double price = jsonObject.get("price").getAsDouble();
        boolean isSold = jsonObject.get("isSold").getAsBoolean();

        User owner = null;
        if (jsonObject.has("owner ID")) {
            int ownerId = jsonObject.get("owner ID").getAsInt();
            owner = userService.getUsersCollection().get(ownerId);
            if (owner == null) {
                throw new IllegalArgumentException("No user found with ID: " + ownerId);
            }
        }

        if (jsonObject.has("internalNumber")) {
            int internalNumber = jsonObject.get("internalNumber").getAsInt();
            JsonElement mainApartmentElement = jsonObject.get("mainApartment");
            Property mainApartment;
            if (mainApartmentElement.isJsonObject()) {
                JsonObject mainApartmentObj = mainApartmentElement.getAsJsonObject();
                if (mainApartmentObj.has("address")) {
                    String address = mainApartmentObj.get("address").getAsString();
                    mainApartment = PropertyManager.getInstance().getPropertyByAddress(address);
                    if (mainApartment == null) {
                        throw new IllegalArgumentException("No property found with address: " + address);
                    }
                } else {
                    mainApartment = gson.fromJson(mainApartmentObj, Property.class);
                }
            } else {
                throw new JsonParseException("Expected mainApartment to be an object");
            }
            return new InternalApartment(mainApartment, internalNumber, area, price, isSold, owner);
        }

        return new Apartment(street, avenue, area, price, isSold, owner);
    }
}
