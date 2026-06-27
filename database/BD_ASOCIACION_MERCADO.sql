DROP DATABASE IF EXISTS BD_ASOCIACION_MERCADO;
CREATE DATABASE BD_ASOCIACION_MERCADO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE BD_ASOCIACION_MERCADO;

-- ========================================================
-- MODULO 1: SEGURIDAD Y ACCESOS (Auth-Service)
-- ========================================================
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    dni CHAR(8) NOT NULL UNIQUE,
    correo VARCHAR(150) UNIQUE,
    telefono VARCHAR(20),
    rol ENUM('ADMIN', 'TESORERO', 'RECEPCIONISTA') NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ========================================================
-- MODULO 2: GOBERNANZA Y PATRIMONIO (Patrimonio-Service)
-- ========================================================
CREATE TABLE socios (
    id_socio INT AUTO_INCREMENT PRIMARY KEY,
    dni CHAR(8) NOT NULL UNIQUE,
    ruc CHAR(11) UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    correo VARCHAR(150) UNIQUE,
    direccion VARCHAR(180),
    estado ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    es_asociacion BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE puestos (
    id_puesto INT AUTO_INCREMENT PRIMARY KEY,
    codigo_puesto VARCHAR(20) NOT NULL UNIQUE,
    pabellon VARCHAR(50) NOT NULL,
    medidas VARCHAR(50),
    giro VARCHAR(80),
    estado_puesto ENUM('LIBRE', 'OCUPADO', 'MANTENIMIENTO') NOT NULL DEFAULT 'LIBRE',
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE contratos_ocupacion (
    id_contrato INT AUTO_INCREMENT PRIMARY KEY,
    id_puesto INT NOT NULL,
    id_socio INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    estado_contrato ENUM('ACTIVO', 'FINALIZADO', 'ANULADO') NOT NULL DEFAULT 'ACTIVO',
    motivo_cierre VARCHAR(255),
    id_usuario_registro INT NOT NULL,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transferencias_titularidad (
    id_transferencia INT AUTO_INCREMENT PRIMARY KEY,
    id_puesto INT NOT NULL,
    id_contrato_saliente INT,
    id_socio_saliente INT,
    id_socio_entrante INT NOT NULL,
    id_contrato_entrante INT,
    id_usuario_tramite INT NOT NULL,
    costo_transferencia DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    deuda_validada BOOLEAN NOT NULL DEFAULT FALSE,
    asume_deuda BOOLEAN NOT NULL DEFAULT FALSE,
    monto_deuda_asumida DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    estado_transferencia ENUM('REGISTRADA', 'BLOQUEADA', 'ANULADA') NOT NULL DEFAULT 'REGISTRADA',
    observacion TEXT,
    fecha_tramite DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ========================================================
-- MODULO 3: TESORERIA Y CAJA (Tesoreria-Service)
-- ========================================================
CREATE TABLE conceptos_cobro (
    id_concepto INT AUTO_INCREMENT PRIMARY KEY,
    nombre_concepto VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    tipo_cobro ENUM('FIJO', 'PRORRATEO') NOT NULL,
    periodicidad ENUM('MENSUAL', 'DIARIO', 'EXTRAORDINARIO') NOT NULL DEFAULT 'MENSUAL',
    monto_fijo DECIMAL(10,2),
    costo_total_prorrateo DECIMAL(10,2),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE turnos_caja (
    id_turno INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    fecha_apertura DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_cierre DATETIME,
    monto_inicial DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    monto_recaudado DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    monto_esperado DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    diferencia DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    estado_turno ENUM('ABIERTO', 'CERRADO', 'ANULADO') NOT NULL DEFAULT 'ABIERTO',
    observacion_apertura VARCHAR(255),
    observacion_cierre VARCHAR(255)
);

CREATE TABLE cuotas_deuda (
    id_cuota INT AUTO_INCREMENT PRIMARY KEY,
    id_puesto INT NOT NULL,
    id_contrato INT,
    id_concepto INT NOT NULL,
    periodo_mes INT,
    periodo_anio INT,
    monto_total DECIMAL(10,2) NOT NULL,
    estado_cuota ENUM('PENDIENTE', 'PAGADO', 'ANULADO', 'EXONERADO') NOT NULL DEFAULT 'PENDIENTE',
    fecha_generacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_vencimiento DATE,
    id_usuario_generacion INT NOT NULL,
    motivo_exoneracion VARCHAR(255),
    fecha_exoneracion DATETIME,
    id_usuario_exoneracion INT,
    motivo_anulacion VARCHAR(255),
    fecha_anulacion DATETIME,
    id_usuario_anulacion INT,
    id_cuota_origen INT,
    id_cuota_reemplazo INT
);

CREATE TABLE pagos (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_cuota INT NOT NULL,
    id_turno INT NOT NULL,
    id_usuario_cobro INT NOT NULL,
    metodo_pago ENUM('EFECTIVO', 'TRANSFERENCIA', 'YAPE_PLIN', 'TARJETA') NOT NULL,
    numero_operacion VARCHAR(80),
    monto_pagado DECIMAL(10,2) NOT NULL,
    fecha_pago DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado_pago ENUM('REGISTRADO', 'EXTORNADO') NOT NULL DEFAULT 'REGISTRADO',
    motivo_extorno VARCHAR(255),
    fecha_extorno DATETIME,
    id_usuario_extorno INT
);

CREATE TABLE comprobantes (
    id_comprobante INT AUTO_INCREMENT PRIMARY KEY,
    id_pago INT NOT NULL,
    id_cuota INT NOT NULL,
    numero_comprobante VARCHAR(60) NOT NULL UNIQUE,
    fecha_emision DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    monto_total DECIMAL(10,2) NOT NULL,
    metodo_pago ENUM('EFECTIVO', 'TRANSFERENCIA', 'YAPE_PLIN', 'TARJETA') NOT NULL,
    estado_comprobante ENUM('EMITIDO', 'ANULADO') NOT NULL DEFAULT 'EMITIDO'
);

-- ========================================================
-- MODULO 4: TRANSPARENCIA, AUDITORIA Y REPORTES
-- ========================================================
CREATE TABLE auditoria_eventos (
    id_evento INT AUTO_INCREMENT PRIMARY KEY,
    modulo VARCHAR(50) NOT NULL,
    tipo_evento VARCHAR(80) NOT NULL,
    entidad_afectada VARCHAR(80) NOT NULL,
    id_registro_afectado INT NOT NULL,
    id_usuario INT NOT NULL,
    descripcion TEXT NOT NULL,
    fecha_evento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE auditoria_anulaciones (
    id_auditoria INT AUTO_INCREMENT PRIMARY KEY,
    tipo_anulacion ENUM('CUOTA', 'PAGO', 'COMPROBANTE', 'TRANSFERENCIA') NOT NULL,
    id_registro_afectado INT NOT NULL,
    id_usuario INT NOT NULL,
    motivo_sustento TEXT NOT NULL,
    fecha_anulacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ========================================================
-- INDICES DE REFERENCIA ENTRE MICROSERVICIOS
-- No se crean llaves foraneas fisicas. La integridad se valida por servicios.
-- ========================================================
CREATE INDEX idx_socios_estado ON socios (estado);
CREATE INDEX idx_puestos_pabellon ON puestos (pabellon);
CREATE INDEX idx_puestos_estado ON puestos (estado_puesto);

CREATE INDEX idx_contratos_puesto ON contratos_ocupacion (id_puesto);
CREATE INDEX idx_contratos_socio ON contratos_ocupacion (id_socio);
CREATE INDEX idx_contratos_estado ON contratos_ocupacion (estado_contrato);

CREATE INDEX idx_transferencias_puesto ON transferencias_titularidad (id_puesto);
CREATE INDEX idx_transferencias_entrante ON transferencias_titularidad (id_socio_entrante);
CREATE INDEX idx_transferencias_estado ON transferencias_titularidad (estado_transferencia);

CREATE INDEX idx_conceptos_activo ON conceptos_cobro (activo);
CREATE INDEX idx_turnos_usuario_estado ON turnos_caja (id_usuario, estado_turno);
CREATE INDEX idx_turnos_fecha ON turnos_caja (fecha_apertura);

CREATE INDEX idx_cuotas_puesto ON cuotas_deuda (id_puesto);
CREATE INDEX idx_cuotas_contrato ON cuotas_deuda (id_contrato);
CREATE INDEX idx_cuotas_concepto ON cuotas_deuda (id_concepto);
CREATE INDEX idx_cuotas_estado ON cuotas_deuda (estado_cuota);
CREATE INDEX idx_cuotas_periodo ON cuotas_deuda (periodo_anio, periodo_mes);

CREATE INDEX idx_pagos_cuota ON pagos (id_cuota);
CREATE INDEX idx_pagos_turno ON pagos (id_turno);
CREATE INDEX idx_pagos_fecha ON pagos (fecha_pago);
CREATE INDEX idx_pagos_estado ON pagos (estado_pago);

CREATE INDEX idx_comprobantes_pago ON comprobantes (id_pago);
CREATE INDEX idx_comprobantes_cuota ON comprobantes (id_cuota);
CREATE INDEX idx_auditoria_eventos_usuario ON auditoria_eventos (id_usuario);
CREATE INDEX idx_auditoria_eventos_entidad ON auditoria_eventos (entidad_afectada, id_registro_afectado);
CREATE INDEX idx_auditoria_anulaciones_tipo ON auditoria_anulaciones (tipo_anulacion, id_registro_afectado);

-- ========================================================
-- DATOS DE PRUEBA
-- Passwords BCrypt de referencia.
-- Claves: admin123, teso123, recep123
-- ========================================================
INSERT INTO usuarios (username, password, nombre_completo, dni, correo, telefono, rol, activo) VALUES
('admin', '$2a$10$1x70xDMVmuQzhjaXpUeHROjpvxyvYLxaf7k0RgyqH8.73qrInEU7O', 'Lucia Perez Andrade', '70000001', 'admin@mercado.test', '999111001', 'ADMIN', TRUE),
('tesoreria', '$2a$10$uDJ2Djde4NMij33FIWk5GeY8eS2n5wsBzQRB8tMKbCaKwv6i/EpQq', 'Rosa Torres Vega', '70000002', 'tesoreria@mercado.test', '999111002', 'TESORERO', TRUE),
('recepcion', '$2a$10$zIVIlnpLd9AiBuSCIR03YOtAh8EfxRYNwwHKHRD5Y6OEKU/FYzkLu', 'Carlos Medina Ruiz', '70000003', 'recepcion@mercado.test', '999111003', 'RECEPCIONISTA', TRUE);

INSERT INTO socios (dni, ruc, nombres, apellidos, telefono, correo, direccion, estado, es_asociacion) VALUES
('40606060', NULL, 'Asociacion', 'Mercado Santa Rosa', '987111000', 'asociacion@mercado.test', 'Oficina administrativa del mercado', 'ACTIVO', TRUE),
('40101010', '10401010101', 'Ana Valeria', 'Ramos Paredes', '987111101', 'ana.ramos@mercado.test', 'Av. Los Comerciantes 101', 'ACTIVO', FALSE),
('40202020', '10402020202', 'Luis Alberto', 'Castillo Nunez', '987111102', 'luis.castillo@mercado.test', 'Jr. Union 220', 'ACTIVO', FALSE),
('40303030', '10403030303', 'Carmen Rosa', 'Huerta Salas', '987111103', 'carmen.huerta@mercado.test', 'Calle Central 315', 'ACTIVO', FALSE),
('40404040', '10404040404', 'Pedro Miguel', 'Torres Ibañez', '987111104', 'pedro.torres@mercado.test', 'Pasaje Mercado 410', 'ACTIVO', FALSE),
('40505050', '10405050505', 'Elena Marisol', 'Vargas Cueva', '987111105', 'elena.vargas@mercado.test', 'Av. Progreso 515', 'ACTIVO', FALSE),
('40707070', '10407070707', 'Ricardo', 'Flores Quiñones', '987111107', 'ricardo.flores@mercado.test', 'Jr. Comercio 700', 'INACTIVO', FALSE),
('40808080', '10408080808', 'Milagros', 'Seoane Rivero', '987111108', 'milagros.seoane@mercado.test', 'Av. Mercado Norte 808', 'ACTIVO', FALSE),
('40909090', '10409090909', 'Roberto', 'Salazar Poma', '987111109', 'roberto.salazar@mercado.test', 'Pasaje Las Flores 909', 'ACTIVO', FALSE);

INSERT INTO puestos (codigo_puesto, pabellon, medidas, giro, estado_puesto) VALUES
('A-001', 'A', '2x2m', 'Abarrotes', 'OCUPADO'),
('A-002', 'A', '2x2m', 'Jugueria', 'OCUPADO'),
('A-003', 'A', '2x2m', 'Ropa escolar', 'OCUPADO'),
('A-004', 'A', '2x2m', 'Bodega', 'LIBRE'),
('B-001', 'B', '2x3m', 'Carnes rojas', 'OCUPADO'),
('B-002', 'B', '2x3m', 'Polleria', 'OCUPADO'),
('B-003', 'B', '2x3m', 'Pescados', 'MANTENIMIENTO'),
('C-001', 'C', '3x3m', 'Verduras', 'OCUPADO'),
('C-002', 'C', '3x2m', 'Frutas', 'OCUPADO'),
('D-001', 'D', '2x2m', 'Deposito temporal', 'LIBRE'),
('D-002', 'D', '2x2m', 'Ferreteria', 'OCUPADO'),
('E-001', 'E', '3x2m', 'Comedor', 'MANTENIMIENTO');

INSERT INTO contratos_ocupacion (id_puesto, id_socio, fecha_inicio, fecha_fin, estado_contrato, motivo_cierre, id_usuario_registro, fecha_registro) VALUES
(1, 2, DATE_SUB(CURDATE(), INTERVAL 420 DAY), NULL, 'ACTIVO', NULL, 1, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 420 DAY), '09:00:00')),
(2, 3, DATE_SUB(CURDATE(), INTERVAL 390 DAY), DATE_SUB(CURDATE(), INTERVAL 21 DAY), 'FINALIZADO', 'Traspaso aprobado por administracion.', 1, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 390 DAY), '09:20:00')),
(2, 5, DATE_SUB(CURDATE(), INTERVAL 20 DAY), NULL, 'ACTIVO', NULL, 1, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 20 DAY), '10:10:00')),
(3, 4, DATE_SUB(CURDATE(), INTERVAL 360 DAY), NULL, 'ACTIVO', NULL, 1, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 360 DAY), '08:40:00')),
(5, 5, DATE_SUB(CURDATE(), INTERVAL 330 DAY), NULL, 'ACTIVO', NULL, 1, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 330 DAY), '08:50:00')),
(6, 6, DATE_SUB(CURDATE(), INTERVAL 300 DAY), NULL, 'ACTIVO', NULL, 1, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 300 DAY), '09:10:00')),
(8, 8, DATE_SUB(CURDATE(), INTERVAL 240 DAY), NULL, 'ACTIVO', NULL, 1, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 240 DAY), '09:30:00')),
(9, 9, DATE_SUB(CURDATE(), INTERVAL 210 DAY), NULL, 'ACTIVO', NULL, 1, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 210 DAY), '10:00:00')),
(11, 2, DATE_SUB(CURDATE(), INTERVAL 120 DAY), NULL, 'ACTIVO', NULL, 1, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 120 DAY), '11:00:00')),
(10, 1, DATE_SUB(CURDATE(), INTERVAL 500 DAY), DATE_SUB(CURDATE(), INTERVAL 60 DAY), 'FINALIZADO', 'Puesto liberado para nueva adjudicacion.', 1, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 500 DAY), '08:00:00'));

INSERT INTO conceptos_cobro (nombre_concepto, descripcion, tipo_cobro, periodicidad, monto_fijo, costo_total_prorrateo, activo, fecha_registro) VALUES
('Mantenimiento general', 'Cuota mensual para limpieza, administracion y mantenimiento menor.', 'FIJO', 'MENSUAL', 35.00, NULL, TRUE, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 180 DAY), '09:00:00')),
('Servicio de vigilancia', 'Prorrateo mensual del servicio de seguridad nocturna.', 'PRORRATEO', 'MENSUAL', NULL, 960.00, TRUE, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 180 DAY), '09:10:00')),
('Limpieza de pabellones', 'Cuota fija por limpieza comun de pasillos y puntos de acopio.', 'FIJO', 'MENSUAL', 18.00, NULL, TRUE, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 170 DAY), '09:20:00')),
('Agua comun', 'Consumo mensual de agua para zonas compartidas.', 'FIJO', 'MENSUAL', 22.50, NULL, TRUE, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 160 DAY), '09:30:00')),
('Multa por inasistencia', 'Cargo extraordinario por inasistencia a asamblea obligatoria.', 'FIJO', 'EXTRAORDINARIO', 50.00, NULL, TRUE, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 120 DAY), '10:00:00')),
('SISA diario', 'Cargo diario declarado por administracion municipal.', 'FIJO', 'DIARIO', 3.00, NULL, TRUE, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 100 DAY), '10:15:00')),
('Pintura de fachadas', 'Prorrateo para renovacion de fachadas del mercado.', 'PRORRATEO', 'MENSUAL', NULL, 480.00, FALSE, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 80 DAY), '10:30:00'));

INSERT INTO turnos_caja (id_usuario, fecha_apertura, fecha_cierre, monto_inicial, monto_recaudado, monto_esperado, diferencia, estado_turno, observacion_apertura, observacion_cierre) VALUES
(2, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY), '08:00:00'), TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY), '18:05:00'), 120.00, 138.00, 138.00, 0.00, 'CERRADO', 'Caja regular de dia laborable.', 'Cierre sin diferencia.'),
(2, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:00:00'), TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '18:00:00'), 100.00, 38.00, 38.00, 0.00, 'CERRADO', 'Caja con baja afluencia.', 'Cierre conforme.'),
(2, TIMESTAMP(CURDATE(), '08:00:00'), NULL, 150.00, 170.00, 170.00, 0.00, 'ABIERTO', 'Caja principal abierta.', NULL),
(2, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 6 DAY), '08:00:00'), TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 6 DAY), '08:35:00'), 80.00, 0.00, 0.00, 0.00, 'ANULADO', 'Turno abierto por error.', 'Anulado por duplicidad de apertura.');

INSERT INTO cuotas_deuda (id_puesto, id_contrato, id_concepto, periodo_mes, periodo_anio, monto_total, estado_cuota, fecha_generacion, fecha_vencimiento, id_usuario_generacion, motivo_exoneracion, fecha_exoneracion, id_usuario_exoneracion, motivo_anulacion, fecha_anulacion, id_usuario_anulacion, id_cuota_origen, id_cuota_reemplazo) VALUES
(1, 1, 1, MONTH(CURDATE()), YEAR(CURDATE()), 35.00, 'PENDIENTE', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 12 DAY), '09:00:00'), DATE_ADD(CURDATE(), INTERVAL 7 DAY), 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(1, 1, 2, MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)), YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)), 120.00, 'PAGADO', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 25 DAY), '09:00:00'), DATE_SUB(CURDATE(), INTERVAL 15 DAY), 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(2, 3, 1, MONTH(CURDATE()), YEAR(CURDATE()), 35.00, 'PENDIENTE', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 12 DAY), '09:05:00'), DATE_SUB(CURDATE(), INTERVAL 5 DAY), 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(2, 3, 5, MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)), YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)), 50.00, 'ANULADO', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 20 DAY), '10:00:00'), DATE_SUB(CURDATE(), INTERVAL 10 DAY), 1, NULL, NULL, NULL, 'Monto aplicado al puesto equivocado; se genero reemplazo corregido.', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 18 DAY), '16:20:00'), 1, NULL, 5),
(2, 3, 5, MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)), YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)), 40.00, 'PENDIENTE', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 18 DAY), '16:25:00'), DATE_ADD(CURDATE(), INTERVAL 3 DAY), 1, NULL, NULL, NULL, NULL, NULL, NULL, 4, NULL),
(3, 4, 3, MONTH(CURDATE()), YEAR(CURDATE()), 18.00, 'PAGADO', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 11 DAY), '09:20:00'), DATE_ADD(CURDATE(), INTERVAL 5 DAY), 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(5, 5, 2, MONTH(CURDATE()), YEAR(CURDATE()), 120.00, 'PENDIENTE', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 10 DAY), '09:30:00'), DATE_SUB(CURDATE(), INTERVAL 2 DAY), 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(6, 6, 4, MONTH(CURDATE()), YEAR(CURDATE()), 22.50, 'EXONERADO', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 10 DAY), '09:35:00'), DATE_ADD(CURDATE(), INTERVAL 4 DAY), 1, 'Exoneracion por cierre temporal autorizado por mantenimiento sanitario.', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 8 DAY), '12:00:00'), 1, NULL, NULL, NULL, NULL, NULL),
(8, 7, 1, MONTH(CURDATE()), YEAR(CURDATE()), 35.00, 'PAGADO', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 9 DAY), '09:40:00'), DATE_ADD(CURDATE(), INTERVAL 6 DAY), 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(9, 8, 6, MONTH(CURDATE()), YEAR(CURDATE()), 3.00, 'PAGADO', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 9 DAY), '09:45:00'), CURDATE(), 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(11, 9, 2, MONTH(CURDATE()), YEAR(CURDATE()), 120.00, 'PENDIENTE', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 8 DAY), '09:50:00'), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(5, 5, 1, MONTH(DATE_ADD(CURDATE(), INTERVAL 1 MONTH)), YEAR(DATE_ADD(CURDATE(), INTERVAL 1 MONTH)), 35.00, 'PENDIENTE', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 3 DAY), '11:00:00'), DATE_ADD(CURDATE(), INTERVAL 35 DAY), 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(3, 4, 5, MONTH(CURDATE()), YEAR(CURDATE()), 50.00, 'PAGADO', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 4 DAY), '10:00:00'), DATE_ADD(CURDATE(), INTERVAL 2 DAY), 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(6, 6, 1, MONTH(CURDATE()), YEAR(CURDATE()), 35.00, 'ANULADO', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 7 DAY), '10:00:00'), DATE_ADD(CURDATE(), INTERVAL 4 DAY), 1, NULL, NULL, NULL, 'Cuota anulada por duplicidad de generacion masiva.', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 6 DAY), '14:00:00'), 1, NULL, NULL),
(8, 7, 2, MONTH(CURDATE()), YEAR(CURDATE()), 120.00, 'PAGADO', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 5 DAY), '09:00:00'), DATE_ADD(CURDATE(), INTERVAL 8 DAY), 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(6, 6, 5, MONTH(CURDATE()), YEAR(CURDATE()), 50.00, 'PENDIENTE', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 3 DAY), '10:40:00'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO pagos (id_cuota, id_turno, id_usuario_cobro, metodo_pago, numero_operacion, monto_pagado, fecha_pago, estado_pago, motivo_extorno, fecha_extorno, id_usuario_extorno) VALUES
(2, 1, 2, 'EFECTIVO', 'REC-ANT-001', 120.00, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY), '10:30:00'), 'REGISTRADO', NULL, NULL, NULL),
(6, 1, 2, 'TRANSFERENCIA', 'TRF-BCP-88421', 18.00, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY), '12:10:00'), 'REGISTRADO', NULL, NULL, NULL),
(9, 2, 2, 'YAPE_PLIN', 'YP-778900', 35.00, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '11:10:00'), 'REGISTRADO', NULL, NULL, NULL),
(10, 2, 2, 'TARJETA', 'POS-002391', 3.00, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '15:40:00'), 'REGISTRADO', NULL, NULL, NULL),
(13, 3, 2, 'EFECTIVO', 'REC-HOY-001', 50.00, TIMESTAMP(CURDATE(), '09:45:00'), 'REGISTRADO', NULL, NULL, NULL),
(15, 3, 2, 'YAPE_PLIN', 'YP-991245', 120.00, TIMESTAMP(CURDATE(), '11:25:00'), 'REGISTRADO', NULL, NULL, NULL),
(16, 3, 2, 'TRANSFERENCIA', 'TRF-ERROR-557', 50.00, TIMESTAMP(CURDATE(), '12:05:00'), 'EXTORNADO', 'Pago registrado sobre cuota incorrecta; se extorna con autorizacion administrativa.', TIMESTAMP(CURDATE(), '12:30:00'), 1);

INSERT INTO comprobantes (id_pago, id_cuota, numero_comprobante, fecha_emision, monto_total, metodo_pago, estado_comprobante) VALUES
(1, 2, 'CP-ANT-000001', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY), '10:30:00'), 120.00, 'EFECTIVO', 'EMITIDO'),
(2, 6, 'CP-ANT-000002', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY), '12:10:00'), 18.00, 'TRANSFERENCIA', 'EMITIDO'),
(3, 9, 'CP-ANT-000003', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '11:10:00'), 35.00, 'YAPE_PLIN', 'EMITIDO'),
(4, 10, 'CP-ANT-000004', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '15:40:00'), 3.00, 'TARJETA', 'EMITIDO'),
(5, 13, 'CP-HOY-000001', TIMESTAMP(CURDATE(), '09:45:00'), 50.00, 'EFECTIVO', 'EMITIDO'),
(6, 15, 'CP-HOY-000002', TIMESTAMP(CURDATE(), '11:25:00'), 120.00, 'YAPE_PLIN', 'EMITIDO'),
(7, 16, 'CP-HOY-000003', TIMESTAMP(CURDATE(), '12:05:00'), 50.00, 'TRANSFERENCIA', 'ANULADO');

INSERT INTO transferencias_titularidad (id_puesto, id_contrato_saliente, id_socio_saliente, id_socio_entrante, id_contrato_entrante, id_usuario_tramite, costo_transferencia, deuda_validada, asume_deuda, monto_deuda_asumida, estado_transferencia, observacion, fecha_tramite) VALUES
(2, 2, 3, 5, 3, 1, 150.00, TRUE, FALSE, 0.00, 'REGISTRADA', 'Traspaso aprobado luego de validar deuda cero del puesto A-002.', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 20 DAY), '10:15:00')),
(5, 5, 5, 8, NULL, 1, 0.00, FALSE, FALSE, 0.00, 'BLOQUEADA', 'Solicitud bloqueada por deuda pendiente de vigilancia.', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 4 DAY), '16:20:00')),
(9, 8, 9, 6, NULL, 1, 120.00, TRUE, FALSE, 0.00, 'ANULADA', 'Tramite anulado por desistimiento del comprador antes de firmar contrato.', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 9 DAY), '12:00:00'));

INSERT INTO auditoria_eventos (modulo, tipo_evento, entidad_afectada, id_registro_afectado, id_usuario, descripcion, fecha_evento) VALUES
('PATRIMONIO', 'REGISTRO_SOCIO', 'socios', 2, 1, 'Registro de socio titular con documentacion completa.', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 420 DAY), '09:05:00')),
('PATRIMONIO', 'REGISTRO_CONTRATO', 'contratos_ocupacion', 1, 1, 'Contrato activo creado para puesto A-001.', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 420 DAY), '09:15:00')),
('PATRIMONIO', 'TRANSFERENCIA_REGISTRADA', 'transferencias_titularidad', 1, 1, 'Transferencia de A-002 aprobada sin deuda pendiente.', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 20 DAY), '10:20:00')),
('PATRIMONIO', 'TRANSFERENCIA_BLOQUEADA', 'transferencias_titularidad', 2, 1, 'Transferencia bloqueada por deuda activa del puesto B-001.', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 4 DAY), '16:25:00')),
('TESORERIA', 'GENERACION_CUOTAS', 'cuotas_deuda', 1, 1, 'Generacion mensual de cuotas para contratos activos.', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 12 DAY), '09:10:00')),
('TESORERIA', 'REGISTRO_PAGO', 'pagos', 5, 2, 'Pago registrado en caja del dia.', TIMESTAMP(CURDATE(), '09:46:00')),
('TESORERIA', 'EXTORNO_PAGO', 'pagos', 7, 1, 'Extorno autorizado por error de seleccion de cuota.', TIMESTAMP(CURDATE(), '12:31:00')),
('AUDITORIA', 'ANULACION_CUOTA', 'cuotas_deuda', 4, 1, 'Registro de anulacion por correccion de monto.', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 18 DAY), '16:21:00'));

INSERT INTO auditoria_anulaciones (tipo_anulacion, id_registro_afectado, id_usuario, motivo_sustento, fecha_anulacion) VALUES
('CUOTA', 4, 1, 'Cuota anulada por monto aplicado incorrectamente; se genero reemplazo por S/ 40.00.', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 18 DAY), '16:20:00')),
('CUOTA', 14, 1, 'Cuota anulada por duplicidad detectada en generacion masiva.', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 6 DAY), '14:00:00')),
('PAGO', 7, 1, 'Pago extornado por seleccion de cuota incorrecta en caja.', TIMESTAMP(CURDATE(), '12:30:00')),
('COMPROBANTE', 7, 1, 'Comprobante anulado por extorno del pago asociado.', TIMESTAMP(CURDATE(), '12:30:00')),
('TRANSFERENCIA', 3, 1, 'Transferencia anulada por desistimiento antes de apertura contractual.', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 9 DAY), '12:05:00'));

-- ========================================================
-- CONSULTAS RAPIDAS DE VERIFICACION
-- ========================================================
SELECT 'usuarios' AS tabla, COUNT(*) AS total FROM usuarios
UNION ALL SELECT 'socios', COUNT(*) FROM socios
UNION ALL SELECT 'puestos', COUNT(*) FROM puestos
UNION ALL SELECT 'contratos_ocupacion', COUNT(*) FROM contratos_ocupacion
UNION ALL SELECT 'conceptos_cobro', COUNT(*) FROM conceptos_cobro
UNION ALL SELECT 'turnos_caja', COUNT(*) FROM turnos_caja
UNION ALL SELECT 'cuotas_deuda', COUNT(*) FROM cuotas_deuda
UNION ALL SELECT 'pagos', COUNT(*) FROM pagos
UNION ALL SELECT 'comprobantes', COUNT(*) FROM comprobantes
UNION ALL SELECT 'transferencias_titularidad', COUNT(*) FROM transferencias_titularidad
UNION ALL SELECT 'auditoria_eventos', COUNT(*) FROM auditoria_eventos
UNION ALL SELECT 'auditoria_anulaciones', COUNT(*) FROM auditoria_anulaciones;

SELECT id_puesto, SUM(monto_total) AS deuda_pendiente
FROM cuotas_deuda
WHERE estado_cuota = 'PENDIENTE'
GROUP BY id_puesto
ORDER BY id_puesto;
