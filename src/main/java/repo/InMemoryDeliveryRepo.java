package repo;

import model.Delivery;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryDeliveryRepo implements DeliveryRepo {
    private final Map<String, Delivery> storage = new HashMap<>();

    @Override
    public void save(Delivery delivery) {
        storage.put(delivery.getId(), delivery);
    }

    @Override
    public Optional<Delivery> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }
}
