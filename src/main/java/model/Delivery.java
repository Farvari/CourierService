package model;

public class Delivery {
    private final String id;
    private final Package pkg;
    private final Customer customer;
    private Courier courier;
    private DeliveryStatus status;

    public Delivery(String id, Package pkg, Customer customer) {
        this.id = id;
        this.pkg = pkg;
        this.customer = customer;
        this.status = DeliveryStatus.CREATED;
    }

    public String getId() { return id; }
    public Package getPkg() { return pkg; }
    public Customer getCustomer() { return customer; }
    public Courier getCourier() { return courier; }
    public void setCourier(Courier courier) { this.courier = courier; }
    public DeliveryStatus getStatus() { return status; }

    public void updateStatus(DeliveryStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                    String.format("Недопустимый переход статуса с %s на %s", this.status, newStatus)
            );
        }
        this.status = newStatus;
    }
}
