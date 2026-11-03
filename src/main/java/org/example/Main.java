package org.example;
import model.*;
import repo.*;
import service.*;

public class Main {
    public static void main(String[] args) {
        DeliveryRepo repo = new InMemoryDeliveryRepo();
        DeliveryService deliveryService = new DeliveryService(repo);

        model.Package pkg = new model.Package("P-101", "Игровой ноутбук", 2.5);
        Customer customer = new Customer("C-202", "Иван Иванов", "г. Минск, ул. Свердлова, д. 13a");
        Courier courier = new Courier("CR-303", "Дмитрий");

        System.out.println("\n1. Создание заказа");
        Delivery delivery = deliveryService.createDelivery("DEL-001", pkg, customer);
        printInfo(delivery);

        System.out.println("\n2. Назначение курьера");
        deliveryService.assignCourier("DEL-001", courier);
        delivery = deliveryService.getDeliveryOrThrow("DEL-001");
        printInfo(delivery);

        System.out.println("\n3. Изменение статуса на 'В пути'");
        deliveryService.changeStatus("DEL-001", DeliveryStatus.IN_TRANSIT);
        delivery = deliveryService.getDeliveryOrThrow("DEL-001");
        printInfo(delivery);

        System.out.println("\n4. Изменение статуса на 'Доставлено'");
        deliveryService.changeStatus("DEL-001", DeliveryStatus.DELIVERED);
        delivery = deliveryService.getDeliveryOrThrow("DEL-001");
        printInfo(delivery);

        System.out.println("\n5. Перехват ошибок");
        try {
            System.out.println("Попытка сменить статус доставленного заказа обратно на IN_TRANSIT:");
            deliveryService.changeStatus("DEL-001", DeliveryStatus.IN_TRANSIT);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("Попытка создать заказ с пустым ID:");
            deliveryService.createDelivery("   ", pkg, customer);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printInfo(Delivery delivery) {
        System.out.println("ID Доставки  : " + delivery.getId());
        System.out.println("Посылка      : " + delivery.getPkg().getDescription() + " (" + delivery.getPkg().getWeightKg() + " кг)");
        System.out.println("Получатель   : " + delivery.getCustomer().getName() + " (" + delivery.getCustomer().getAddress() + ")");
        System.out.println("Курьер       : " + (delivery.getCourier() != null ? delivery.getCourier().getName() : "Не назначен"));
        System.out.println("Текущий статус: " + delivery.getStatus());
    }
}
