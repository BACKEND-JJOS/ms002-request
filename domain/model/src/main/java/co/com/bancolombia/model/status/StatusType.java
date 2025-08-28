package co.com.bancolombia.model.status;

public enum StatusType {

    PENDING("PENDIENTE", "Solicitud en espera de revisión"),
    REVIEWED("REVISADO", "Solicitud revisada por un analista"),
    APPROVED("APROBADO", "Solicitud aprobada y lista para desembolso"),
    REJECTED("RECHAZADO", "Solicitud rechazada por criterios internos");

    private final String dbName;
    private final String description;

    StatusType(String dbName, String description) {
        this.dbName = dbName;
        this.description = description;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDescription() {
        return description;
    }

}
