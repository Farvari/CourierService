package model;

import java.util.EnumSet;
import java.util.Set;

public enum DeliveryStatus {
    CREATED,
    ASSIGNED,
    IN_TRANSIT,
    DELIVERED,
    CANCELLED;

    public boolean canTransitionTo(DeliveryStatus next) {
        switch (this) {
            case CREATED:
                return EnumSet.of(ASSIGNED, CANCELLED).contains(next);
            case ASSIGNED:
                return EnumSet.of(IN_TRANSIT, CANCELLED).contains(next);
            case IN_TRANSIT:
                return EnumSet.of(DELIVERED, CANCELLED).contains(next);
            case DELIVERED:
            case CANCELLED:
            default:
                return false;
        }
    }
}