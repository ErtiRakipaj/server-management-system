package al.sda.servermanagementsystem.enums;

import lombok.Data;

public enum Status {

    ACTIVE ("ACTIVE"),
    INACTIVE("INACTIVE");
    private final  String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
