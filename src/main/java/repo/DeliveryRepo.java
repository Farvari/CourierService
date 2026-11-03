package repo;

import model.Delivery;
import java.util.Optional;

public interface DeliveryRepo {
    void save(Delivery delivery);
    Optional<Delivery> findById(String id);
}
