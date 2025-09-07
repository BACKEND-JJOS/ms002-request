package co.com.bancolombia.model.status;

public enum StatusType {

    PENDING(1L, "PENDIENTE", "Solicitud en espera de revisión"),
    REVIEWED(2L,"REVISADO", "Solicitud revisada por un analista"),
    APPROVED(3L,"APROBADO", "Solicitud aprobada y lista para desembolso"),
    REJECTED(4L, "RECHAZADO", "Solicitud rechazada por criterios internos");

    private final Long dbId;
    private final String dbName;
    private final String description;

    StatusType(Long dbId,String dbName, String description) {
        this.dbId = dbId;
        this.dbName = dbName;
        this.description = description;
    }

    public  Long getDbId (){
        return dbId;
    }
    public String getDbName() {
        return dbName;
    }

    public String getDescription() {
        return description;
    }

}
