package it.polimi.telcoservice.TelcoServiceEJB.entities;

import it.polimi.telcoservice.TelcoServiceEJB.services.OrderService;

public enum OrderStatus {
    VALID(0), INVALID(1);

    private final int value;

    OrderStatus(int value){
        this.value = value;
    }

    public static OrderStatus getOrderStatusFromInt(int value){
        switch (value) {
            case 0:
                return OrderStatus.VALID;
            case 1:
                return OrderStatus.INVALID;
        }
        return null;
    }

    public int getStatus() { return value; }

}
