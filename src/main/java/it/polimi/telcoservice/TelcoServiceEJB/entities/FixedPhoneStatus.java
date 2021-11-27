package it.polimi.telcoservice.TelcoServiceEJB.entities;

public enum FixedPhoneStatus {
    INCLUDED(0), EXCLUDED(1);

    private final int value;

    FixedPhoneStatus(int value){
        this.value = value;
    }

    public static FixedPhoneStatus getFixedPhoneStatusFromInt(int value){
        switch (value) {
            case 0:
                return FixedPhoneStatus.INCLUDED;
            case 1:
                return FixedPhoneStatus.EXCLUDED;
        }
        return null;
    }

    public int getStatus() { return value; }
}
