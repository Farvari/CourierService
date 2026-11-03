package service;

import model.*;
import repo.DeliveryRepo;
import org.apache.commons.lang3.StringUtils; // Импорт из подключенной библиотеки

public class DeliveryService {
    private final DeliveryRepo repo;

    public DeliveryService(DeliveryRepo repo) {
        this.repo = repo;
    }

    public Delivery createDelivery(String deliveryId, model.Package pkg, Customer customer) {
        // Использование метода isBlank() из библиотеки commons-lang3
        if (StringUtils.isBlank(deliveryId)) {
            throw new IllegalArgumentException("Идентификатор доставки не может быть пустым");
        }
        if (pkg == null || customer == null) {
            throw new IllegalArgumentException("Посылка и клиент должны быть указаны");
        }

        Delivery delivery = new Delivery(deliveryId, pkg, customer);
        repo.save(delivery);
        return delivery;
    }

    public void assignCourier(String deliveryId, Courier courier) {
        if (StringUtils.isBlank(deliveryId)) {
            throw new IllegalArgumentException("Идентификатор доставки не может быть пустым");
        }
        Delivery delivery = getDeliveryOrThrow(deliveryId);
        delivery.setCourier(courier);
        delivery.updateStatus(DeliveryStatus.ASSIGNED);
        repo.save(delivery);
    }

    public void changeStatus(String deliveryId, DeliveryStatus newStatus) {
        if (StringUtils.isBlank(deliveryId)) {
            throw new IllegalArgumentException("Идентификатор доставки не может быть пустым");
        }
        Delivery delivery = getDeliveryOrThrow(deliveryId);
        delivery.updateStatus(newStatus);
        repo.save(delivery);
    }

    public Delivery getDeliveryOrThrow(String deliveryId) {
        return repo.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("Доставка с ID " + deliveryId + " не найдена"));
    }
}