package it.polimi.telcoservice.TelcoServiceEJB.entities;

public enum UserStatus  {
    SOLVENT(0), INSOLVENT(1);

    private final int value;

    UserStatus(int value){
        this.value = value;
    }

    public static UserStatus getUserStatusFromInt(int value){
        switch (value) {
            case 0:
                return UserStatus.SOLVENT;
            case 1:
                return UserStatus.INSOLVENT;
        }
        return null;
    }

    public int getStatus() { return value; }

}
