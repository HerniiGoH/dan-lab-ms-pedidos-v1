CREATE TABLE estado_pedido
(
    id     INTEGER UNSIGNED   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    estado VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE pedido
(
    id               INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fecha_pedido     DATETIME         NOT NULL,
    estado_pedido_id INTEGER UNSIGNED NOT NULL,
    obra_id          INTEGER UNSIGNED NOT NULL,
    CONSTRAINT fk_estado_pedido_pedido FOREIGN KEY (estado_pedido_id) REFERENCES estado_pedido (id)

);

CREATE TABLE detalle_pedido
(
    id          INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cantidad    INTEGER UNSIGNED NOT NULL,
    precio      DOUBLE           NOT NULL,
    producto_id INTEGER UNSIGNED NOT NULL,
    pedido_id   INTEGER UNSIGNED NOT NULL,
    CONSTRAINT fk_pedido_detalle_pedido FOREIGN KEY (pedido_id) REFERENCES pedido (id)
);

INSERT INTO estado_pedido (estado)
VALUES ('NUEVO'),
       ('CONFIRMADO'),
       ('PENDIENTE'),
       ('CANCELADO'),
       ('ACEPTADO'),
       ('RECHAZADO'),
       ('EN PREPARACION'),
       ('ENTREGADO');