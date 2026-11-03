import model.Customer;
import model.Courier;
import model.Delivery;
import model.DeliveryStatus;
import model.Package;

import repo.DeliveryRepo;
import repo.InMemoryDeliveryRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DeliveryService;

import static org.junit.jupiter.api.Assertions.*;

public class DeliveryServiceTest {

    private DeliveryService deliveryService;

    @BeforeEach
    void setUp() {
        DeliveryRepo repo = new InMemoryDeliveryRepo();
        deliveryService = new DeliveryService(repo);
    }

    @Test
    void test1() {
        // Тест 1: Успешный сценарий корректной последовательности статусов
        Package pkg = new Package("P01", "Электроника", 1.5);
        Customer customer = new Customer("C01", "Иван Иванов", "ул. Ленина, д. 10");
        Courier courier = new Courier("CR01", "Петр Петров");

        Delivery delivery = deliveryService.createDelivery("D01", pkg, customer);
        assertEquals(DeliveryStatus.CREATED, delivery.getStatus());

        deliveryService.assignCourier("D01", courier);
        assertEquals(DeliveryStatus.ASSIGNED, delivery.getStatus());

        deliveryService.changeStatus("D01", DeliveryStatus.IN_TRANSIT);
        assertEquals(DeliveryStatus.IN_TRANSIT, delivery.getStatus());

        deliveryService.changeStatus("D01", DeliveryStatus.DELIVERED);
        assertEquals(DeliveryStatus.DELIVERED, delivery.getStatus());
    }

    @Test
    void test2() {
        Package pkg = new Package("P02", "Книги", 0.8);
        Customer customer = new Customer("C02", "Анна Сидорова", "пр. Независимости, д. 50");

        deliveryService.createDelivery("D02", pkg, customer);

        assertThrows(IllegalStateException.class, () -> {
            deliveryService.changeStatus("D02", DeliveryStatus.DELIVERED);
        });
    }

    @Test
    void test3() {
        Package pkg = new Package("P03", "Одежда", 0.5);
        Customer customer = new Customer("C03", "Алексей Смирнов", "ул. Победителей, д. 15");

        deliveryService.createDelivery("D03", pkg, customer);
        deliveryService.changeStatus("D03", DeliveryStatus.CANCELLED);

        // Попытка сменить статус у уже отмененной доставки должна вызвать исключение
        assertThrows(IllegalStateException.class, () -> {
            deliveryService.changeStatus("D03", DeliveryStatus.IN_TRANSIT);
        });
    }

    @Test
    void test4() {
        Package pkg = new Package("P04", "Велосипед", 5);
        Customer customer = new Customer("C04", "Александр Шмат", "пр. Держинского, д. 11");
        Courier courier = new Courier("CR01", "Петр Петров");

        Delivery delivery = deliveryService.createDelivery("D04", pkg, customer);

        assertThrows(IllegalStateException.class, () -> {
            deliveryService.changeStatus("D03", DeliveryStatus.ASSIGNED);
        });
    }
}