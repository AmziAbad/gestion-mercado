DROP DATABASE IF EXISTS BD_ASOCIACION_MERCADO;
CREATE DATABASE BD_ASOCIACION_MERCADO;
USE BD_ASOCIACION_MERCADO;

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    dni CHAR(8) UNIQUE NOT NULL,
    correo VARCHAR(150) UNIQUE,
    telefono VARCHAR(20),
    rol ENUM('ADMIN', 'TESORERO', 'RECEPCIONISTA') NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE socios (
    id_socio INT AUTO_INCREMENT PRIMARY KEY,
    dni CHAR(8) NOT NULL UNIQUE,
    ruc CHAR(11) UNIQUE,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    telefono VARCHAR(15),
    correo VARCHAR(100),
    direccion VARCHAR(150),
    estado_solvencia BOOLEAN DEFAULT TRUE,
    activo BOOLEAN DEFAULT FALSE,
    es_asociacion BOOLEAN DEFAULT FALSE
);

CREATE TABLE puestos (
    id_puesto INT AUTO_INCREMENT PRIMARY KEY,
    numero_puesto VARCHAR(10) NOT NULL UNIQUE,
    pabellon VARCHAR(30) NOT NULL,
    medidas VARCHAR(20) DEFAULT '2x2m',
    precio DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    estado_puesto ENUM('OCUPADO', 'VACANTE', 'MANTENIMIENTO') DEFAULT 'VACANTE',
    id_socio_actual INT,
    id_giro INT
);

CREATE TABLE servicios (
    id_servicio INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50),
    nombre_servicio VARCHAR(50) NOT NULL,
    tipo_cobro ENUM('PRORRATEO', 'FIJO') NOT NULL,
    costo_total_externo DECIMAL(10,2),
    monto_fijo_puesto DECIMAL(10,2),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE cuotas_pago (
    id_cuota INT AUTO_INCREMENT PRIMARY KEY,
    id_puesto INT NOT NULL,
    id_servicio INT NOT NULL,
    mes INT NOT NULL,
    anio INT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    estado ENUM('PENDIENTE', 'PAGADO', 'EXONERADO', 'ANULADO') DEFAULT 'PENDIENTE',
    fecha_pago DATETIME,
    metodo_pago ENUM('EFECTIVO', 'TRANSFERENCIA', 'YAPE_PLIN', 'TARJETA'),
    numero_operacion VARCHAR(50),
    numero_comprobante VARCHAR(50),
    motivo_exoneracion VARCHAR(255),
    fecha_exoneracion DATETIME,
    motivo_anulacion VARCHAR(255),
    fecha_anulacion DATETIME,
    motivo_anulacion_pago VARCHAR(255),
    fecha_anulacion_pago DATETIME,
    id_cuota_origen INT,
    id_cuota_reemplazo INT
);

CREATE TABLE transferencias (
    id_transferencia INT AUTO_INCREMENT PRIMARY KEY,
    id_puesto INT NOT NULL,
    id_socio_saliente INT NULL,
    id_socio_entrante INT NOT NULL,
    id_usuario_tramite INT NOT NULL,
    costo_transferencia DECIMAL(10,2) DEFAULT 0.00,
    fecha_tramite TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    asume_deuda BOOLEAN DEFAULT FALSE,
    monto_deuda_asumida DECIMAL(10,2) DEFAULT 0.00,
    observacion TEXT
);

-- Passwords de prueba cifrados con BCrypt.
-- Claves originales para pruebas: admin123, teso123, recep123, bloq123.
INSERT INTO usuarios (username, password, nombre_completo, dni, correo, telefono, rol, activo) VALUES
('admin', '$2a$10$1x70xDMVmuQzhjaXpUeHROjpvxyvYLxaf7k0RgyqH8.73qrInEU7O', 'Administrador General', '70000001', 'admin@mercado.test', '999111001', 'ADMIN', TRUE),
('tesoreria', '$2a$10$uDJ2Djde4NMij33FIWk5GeY8eS2n5wsBzQRB8tMKbCaKwv6i/EpQq', 'Rosa Torres Vega', '70000002', 'tesoreria@mercado.test', '999111002', 'TESORERO', TRUE),
('recepcion', '$2a$10$zIVIlnpLd9AiBuSCIR03YOtAh8EfxRYNwwHKHRD5Y6OEKU/FYzkLu', 'Carlos Medina Ruiz', '70000003', 'recepcion@mercado.test', '999111003', 'RECEPCIONISTA', TRUE),
('bloqueado', '$2a$10$QkbQar1oqUwka2p5UM7ySuUsyDSb5WA9wAxyGotSD5JxsMdqjWpX2', 'Usuario Inactivo Demo', '70000004', 'bloqueado@mercado.test', '999111004', 'ADMIN', FALSE);

INSERT INTO socios (dni, ruc, nombre, apellido, telefono, correo, direccion, estado_solvencia, activo, es_asociacion) VALUES
('40606060', NULL, 'Asociacion', 'Mercado Central', '987111006', 'asociacion@test.com', 'Administracion del mercado', TRUE, TRUE, TRUE),
('40101010', '10401010101', 'Mariana', 'Quispe Rojas', '987111001', 'mariana.quispe@test.com', 'Av. Los Comerciantes 101', FALSE, TRUE, FALSE),
('40202020', '10402020202', 'Jorge', 'Salazar Mena', '987111002', 'jorge.salazar@test.com', 'Jr. Las Flores 220', TRUE, TRUE, FALSE),
('40303030', '10403030303', 'Lucia', 'Paredes Soto', '987111003', 'lucia.paredes@test.com', 'Calle Central 315', FALSE, TRUE, FALSE),
('40404040', '10404040404', 'Miguel', 'Huaman Diaz', '987111004', 'miguel.huaman@test.com', 'Pasaje Mercado 410', TRUE, TRUE, FALSE),
('40505050', '10405050505', 'Elena', 'Castillo Paz', '987111005', 'elena.castillo@test.com', 'Av. Grau 505', TRUE, FALSE, FALSE);

INSERT INTO puestos (numero_puesto, pabellon, medidas, precio, estado_puesto, id_socio_actual, id_giro) VALUES
('A-001', 'A', '2x2m', 12000.00, 'OCUPADO', 2, 1),
('A-002', 'A', '2x2m', 11800.00, 'OCUPADO', 2, 1),
('A-003', 'A', '2x2m', 11500.00, 'OCUPADO', 3, 2),
('A-004', 'A', '2x2m', 11500.00, 'VACANTE', 1, 2),
('B-001', 'B', '2x3m', 14000.00, 'OCUPADO', 4, 3),
('B-002', 'B', '2x3m', 13800.00, 'OCUPADO', 4, 3),
('B-003', 'B', '2x2m', 11000.00, 'MANTENIMIENTO', 1, 4),
('C-001', 'C', '3x3m', 18000.00, 'OCUPADO', 5, 5),
('C-002', 'C', '3x2m', 15000.00, 'VACANTE', 1, 5),
('C-003', 'C', '2x2m', 12500.00, 'VACANTE', 1, 6);

INSERT INTO servicios (nombre, nombre_servicio, tipo_cobro, costo_total_externo, monto_fijo_puesto, activo) VALUES
('Mantenimiento', 'Mantenimiento general', 'FIJO', NULL, 35.00, TRUE),
('Vigilancia', 'Servicio de vigilancia', 'PRORRATEO', 420.00, NULL, TRUE),
('Agua', 'Consumo de agua comun', 'PRORRATEO', 210.00, NULL, TRUE),
('Limpieza', 'Limpieza de pabellones', 'FIJO', NULL, 20.00, TRUE),
('Luz', 'Consumo electrico comun', 'PRORRATEO', 350.00, NULL, TRUE),
('Internet', 'Internet administrativo', 'FIJO', NULL, 15.00, FALSE);

INSERT INTO cuotas_pago (id_puesto, id_servicio, mes, anio, monto, estado, fecha_pago, metodo_pago, numero_operacion, numero_comprobante) VALUES
(1, 1, 5, 2026, 35.00, 'PAGADO', '2026-05-10 09:30:00', 'EFECTIVO', 'REC-202605-0001', 'CP-202605-000001'),
(1, 2, 5, 2026, 70.00, 'PAGADO', '2026-05-10 09:30:00', 'EFECTIVO', 'REC-202605-0002', 'CP-202605-000002'),
(1, 3, 6, 2026, 30.00, 'PENDIENTE', NULL, NULL, NULL, NULL),
(2, 1, 6, 2026, 35.00, 'PENDIENTE', NULL, NULL, NULL, NULL),
(2, 4, 6, 2026, 20.00, 'PENDIENTE', NULL, NULL, NULL, NULL),
(3, 1, 6, 2026, 35.00, 'PAGADO', '2026-06-12 14:20:00', 'YAPE_PLIN', 'YP-778899', 'CP-202606-000006'),
(3, 2, 6, 2026, 70.00, 'PAGADO', '2026-06-12 14:20:00', 'YAPE_PLIN', 'YP-778900', 'CP-202606-000007'),
(5, 1, 6, 2026, 35.00, 'PENDIENTE', NULL, NULL, NULL, NULL),
(5, 5, 6, 2026, 58.33, 'PENDIENTE', NULL, NULL, NULL, NULL),
(6, 1, 6, 2026, 35.00, 'PENDIENTE', NULL, NULL, NULL, NULL),
(8, 4, 6, 2026, 20.00, 'PAGADO', '2026-06-13 08:45:00', 'TRANSFERENCIA', 'TRX-20260613-001', 'CP-202606-000011'),
(10, 1, 6, 2026, 35.00, 'PENDIENTE', NULL, NULL, NULL, NULL);

INSERT INTO transferencias (id_puesto, id_socio_saliente, id_socio_entrante, id_usuario_tramite, costo_transferencia, fecha_tramite, observacion) VALUES
(10, NULL, 1, 1, 0.00, '2026-06-01 10:00:00', 'Asignacion inicial a la asociacion para pruebas.');

-- Consultas rapidas de verificacion
SELECT 'usuarios' AS tabla, COUNT(*) AS total FROM usuarios
UNION ALL SELECT 'socios', COUNT(*) FROM socios
UNION ALL SELECT 'puestos', COUNT(*) FROM puestos
UNION ALL SELECT 'servicios', COUNT(*) FROM servicios
UNION ALL SELECT 'cuotas_pago', COUNT(*) FROM cuotas_pago
UNION ALL SELECT 'transferencias', COUNT(*) FROM transferencias;

SELECT id_puesto, SUM(monto) AS deuda_pendiente
FROM cuotas_pago
WHERE estado = 'PENDIENTE'
GROUP BY id_puesto
ORDER BY id_puesto;
