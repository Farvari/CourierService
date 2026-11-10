package service;

import model.Courier;
import model.Customer;
import model.Delivery;
import model.DeliveryStatus;
import repo.DeliveryRepo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeliveryService {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);

    private final DeliveryRepo repo;

    public DeliveryService(DeliveryRepo repo) {
        this.repo = repo;
    }

    public Delivery createDelivery(String deliveryId, model.Package pkg, Customer customer) {
        logger.info("Delivery request received for ID: {}", deliveryId);

        // Использование метода isBlank() из библиотеки commons-lang3
        if (StringUtils.isBlank(deliveryId)) {
            logger.error("Delivery operation failed: delivery ID must not be blank");
            throw new IllegalArgumentException("Идентификатор доставки не может быть пустым");
        }
        if (pkg == null || customer == null) {
            logger.error("Delivery operation failed: package and customer must be provided for ID: {}", deliveryId);
            throw new IllegalArgumentException("Посылка и клиент должны быть указаны");
        }

        Delivery delivery = new Delivery(deliveryId, pkg, customer);
        repo.save(delivery);
        logger.info("Delivery created for ID: {} with status: {}", deliveryId, delivery.getStatus());
        return delivery;
    }

    public void assignCourier(String deliveryId, Courier courier) {
        if (StringUtils.isBlank(deliveryId)) {
            logger.error("Delivery operation failed: delivery ID must not be blank");
            throw new IllegalArgumentException("Идентификатор доставки не может быть пустым");
        }
        Delivery delivery = getDeliveryOrThrow(deliveryId);
        delivery.setCourier(courier);
        delivery.updateStatus(DeliveryStatus.ASSIGNED);
        repo.save(delivery);
    }

    public void changeStatus(String deliveryId, DeliveryStatus newStatus) {
        if (StringUtils.isBlank(deliveryId)) {
            logger.error("Delivery operation failed: delivery ID must not be blank");
            throw new IllegalArgumentException("Идентификатор доставки не может быть пустым");
        }
        Delivery delivery = getDeliveryOrThrow(deliveryId);
        try {
            delivery.updateStatus(newStatus);
        } catch (IllegalStateException e) {
            logger.warn("Invalid status transition attempted for delivery ID: {} from {} to {}",
                    deliveryId, delivery.getStatus(), newStatus);
            throw e;
        }
        repo.save(delivery);
        logger.info("Delivery status updated for ID: {} to status: {}", deliveryId, newStatus);
    }

    public Delivery getDeliveryOrThrow(String deliveryId) {
        return repo.findById(deliveryId)
                .orElseThrow(() -> {
                    logger.error("Delivery operation failed: delivery with ID {} not found in repository", deliveryId);
                    return new IllegalArgumentException("Доставка с ID " + deliveryId + " не найдена");
                });
    }
}