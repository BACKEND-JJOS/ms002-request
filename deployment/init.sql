-- podman machine start
-- podman start mariadb-ms002
-- podman run --name mariadb-ms002 -e MARIADB_ROOT_PASSWORD=root -e MARIADB_DATABASE=ms002_request_db -p 3307:3306 -d mariadb:10.11
-- podman exec -it mariadb-ms002 sh
-- mariadb -uroot -proot -h 127.0.0.1
-- USE ms002_request_db;

CREATE TABLE estado (
    id_estado INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE tipo_prestamo (
    id_tipo_prestamo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    monto_minimo DECIMAL(15,2) NOT NULL,
    monto_maximo DECIMAL(15,2) NOT NULL,
    tasa_interes DECIMAL(5,2) NOT NULL,
    validacion_automatica BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE solicitud (
    id_solicitud INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT(20) NOT NULL,
    monto DECIMAL(15,2) NOT NULL,
    plazo INT NOT NULL,
    email VARCHAR(100) NOT NULL,
    estado_id INT NOT NULL,
    tipo_prestamo_id INT NOT NULL,
    CONSTRAINT fk_estado FOREIGN KEY (estado_id) REFERENCES estado(id_estado),
    CONSTRAINT fk_tipo_prestamo FOREIGN KEY (tipo_prestamo_id) REFERENCES tipo_prestamo(id_tipo_prestamo)
);


INSERT INTO estado (nombre, descripcion) VALUES
('PENDIENTE', 'Solicitud en espera de revisión'),
('REVISADO', 'Solicitud revisada por un analista'),
('APROBADO', 'Solicitud aprobada y lista para desembolso'),
('RECHAZADO', 'Solicitud rechazada por criterios internos');

INSERT INTO tipo_prestamo (nombre, monto_minimo, monto_maximo, tasa_interes, validacion_automatica) VALUES
('Crédito de libre inversión', 500000, 50000000, 19.5, TRUE),
('Crédito de vehículo', 5000000, 80000000, 12.0, FALSE),
('Crédito hipotecario', 50000000, 500000000, 9.5, FALSE),
('Crédito educativo', 1000000, 20000000, 10.0, TRUE);