CREATE TABLE tipo_usuario
(
    id   INTEGER UNSIGNED   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE usuario
(
    id              INTEGER UNSIGNED   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user            VARCHAR(20) UNIQUE NOT NULL,
    password        VARCHAR(50)        NOT NULL,
    tipo_usuario_id INTEGER UNSIGNED,
    CONSTRAINT fk_tipo_usuario_usuario FOREIGN KEY (tipo_usuario_id) REFERENCES tipo_usuario (id)
);

CREATE TABLE empleado
(
    id         INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    mail       VARCHAR(50)      NOT NULL,
    razon_social VARCHAR(50)    NOT NULL,
    usuario_id INTEGER UNSIGNED NOT NULL,
    CONSTRAINT fk_usuario_empleado FOREIGN KEY (usuario_id) REFERENCES usuario (id) ON UPDATE CASCADE
);

CREATE TABLE cliente
(
    id                      INTEGER UNSIGNED   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    razon_social            VARCHAR(100)       NOT NULL,
    cuit                    VARCHAR(11) UNIQUE NOT NULL,
    mail                    VARCHAR(50) UNIQUE NOT NULL,
    max_cuenta_corriente    DOUBLE(19, 2),
    habilitado_online       BOOLEAN            NOT NULL DEFAULT FALSE,
    usuario_id              INTEGER UNSIGNED   NOT NULL,
    fecha_baja              DATE DEFAULT NULL,
    CONSTRAINT fk_usuario_cliente FOREIGN KEY (usuario_id) REFERENCES usuario (id) ON UPDATE CASCADE
);

CREATE TABLE tipo_obra
(
    id   INTEGER UNSIGNED    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(254) NOT NULL,
    tipo VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE obra
(
    id           INTEGER UNSIGNED   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    descripcion  VARCHAR(255)       NOT NULL,
    direccion    VARCHAR(100)       NOT NULL UNIQUE,
    latitud      REAL               NOT NULL,
    longitud     REAL               NOT NULL,
    superficie   INTEGER            NOT NULL,
    tipo_obra_id INTEGER UNSIGNED   NOT NULL,
    cliente_id   INTEGER UNSIGNED   NOT NULL,
    CONSTRAINT fk_cliente_obra FOREIGN KEY (cliente_id) REFERENCES cliente (id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_tipo_obra_obra FOREIGN KEY (tipo_obra_id) REFERENCES tipo_obra (id)
);

INSERT INTO tipo_obra (descripcion, tipo)
VALUES ('Obra para trabajar en una reforma','Reforma'),
       ('Obra para trabajar en una casa','Casa'),
       ('Obra para trabajar en un edificio','Edificio'),
       ('Obra para trabajar en vialidad','Vial');

INSERT INTO tipo_usuario (tipo)
VALUES ('Cliente'),
       ('Vendedor');