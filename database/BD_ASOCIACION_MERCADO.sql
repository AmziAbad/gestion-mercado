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
-- Passwords BCrypt de referencia heredadas del avance anterior.
-- Claves: admin123, teso123, recep123
-- ========================================================
INSERT INTO usuarios (username, password, nombre_completo, dni, correo, telefono, rol, activo) VALUES
('admin', '$2a$10$1x70xDMVmuQzhjaXpUeHROjpvxyvYLxaf7k0RgyqH8.73qrInEU7O', 'Administrador General', '70000001', 'admin@mercado.test', '999111001', 'ADMIN', TRUE),
('tesoreria', '$2a$10$uDJ2Djde4NMij33FIWk5GeY8eS2n5wsBzQRB8tMKbCaKwv6i/EpQq', 'Rosa Torres Vega', '70000002', 'tesoreria@mercado.test', '999111002', 'TESORERO', TRUE),
('recepcion', '$2a$10$zIVIlnpLd9AiBuSCIR03YOtAh8EfxRYNwwHKHRD5Y6OEKU/FYzkLu', 'Carlos Medina Ruiz', '70000003', 'recepcion@mercado.test', '999111003', 'RECEPCIONISTA', TRUE);

INSERT INTO socios (dni, ruc, nombres, apellidos, telefono, correo, direccion, estado, es_asociacion) VALUES
('40606060', NULL, 'Asociacion', 'Mercado Central', '987111006', 'asociacion@test.com', 'Administracion del mercado', 'ACTIVO', TRUE),
('40101010', '10401010101', 'Mariana', 'Quispe Rojas', '987111001', 'mariana.quispe@test.com', 'Av. Los Comerciantes 101', 'ACTIVO', FALSE),
('40202020', '10402020202', 'Jorge', 'Salazar Mena', '987111002', 'jorge.salazar@test.com', 'Jr. Las Flores 220', 'ACTIVO', FALSE),
('40303030', '10403030303', 'Lucia', 'Paredes Soto', '987111003', 'lucia.paredes@test.com', 'Calle Central 315', 'ACTIVO', FALSE),
('40404040', '10404040404', 'Miguel', 'Huaman Diaz', '987111004', 'miguel.huaman@test.com', 'Pasaje Mercado 410', 'ACTIVO', FALSE);

INSERT INTO puestos (codigo_puesto, pabellon, medidas, giro, estado_puesto) VALUES
('A-001', 'A', '2x2m', 'Abarrotes', 'OCUPADO'),
('A-002', 'A', '2x2m', 'Abarrotes', 'OCUPADO'),
('A-003', 'A', '2x2m', 'Ropa', 'OCUPADO'),
('B-001', 'B', '2x3m', 'Carnes', 'OCUPADO'),
('B-002', 'B', '2x3m', 'Carnes', 'OCUPADO'),
('C-001', 'C', '3x3m', 'Verduras', 'LIBRE'),
('C-002', 'C', '3x2m', 'Frutas', 'MANTENIMIENTO');

INSERT INTO contratos_ocupacion (id_puesto, id_socio, fecha_inicio, fecha_fin, estado_contrato, id_usuario_registro) VALUES
(1, 2, '2025-01-01', NULL, 'ACTIVO', 1),
(2, 2, '2025-02-01', NULL, 'ACTIVO', 1),
(3, 3, '2025-03-01', NULL, 'ACTIVO', 1),
(4, 4, '2025-04-01', NULL, 'ACTIVO', 1),
(5, 5, '2025-05-01', NULL, 'ACTIVO', 1),
(6, 1, '2025-01-01', NULL, 'ACTIVO', 1);

INSERT INTO conceptos_cobro (nombre_concepto, descripcion, tipo_cobro, periodicidad, monto_fijo, costo_total_prorrateo, activo) VALUES
('Mantenimiento general', 'Cuota mensual de mantenimiento del mercado', 'FIJO', 'MENSUAL', 35.00, NULL, TRUE),
('Servicio de vigilancia', 'Prorrateo mensual del servicio de seguridad', 'PRORRATEO', 'MENSUAL', NULL, 420.00, TRUE),
('Limpieza de pabellones', 'Cuota fija por limpieza comun', 'FIJO', 'MENSUAL', 20.00, NULL, TRUE),
('Multa por inasistencia', 'Cargo extraordinario por inasistencia a asamblea', 'FIJO', 'EXTRAORDINARIO', 50.00, NULL, TRUE);

INSERT INTO turnos_caja (id_usuario, fecha_apertura, fecha_cierre, monto_inicial, monto_recaudado, monto_esperado, diferencia, estado_turno, observacion_apertura, observacion_cierre) VALUES
(2, '2026-06-24 08:00:00', NULL, 100.00, 0.00, 0.00, 0.00, 'ABIERTO', 'Inicio de caja diaria', NULL),
(2, '2026-06-23 08:00:00', '2026-06-23 18:00:00', 100.00, 90.00, 90.00, 0.00, 'CERRADO', 'Caja anterior', 'Cierre conforme');

INSERT INTO cuotas_deuda (id_puesto, id_contrato, id_concepto, periodo_mes, periodo_anio, monto_total, estado_cuota, fecha_generacion, fecha_vencimiento, id_usuario_generacion) VALUES
(1, 1, 1, 6, 2026, 35.00, 'PENDIENTE', '2026-06-01 09:00:00', '2026-06-30', 1),
(1, 1, 2, 6, 2026, 84.00, 'PAGADO', '2026-06-01 09:00:00', '2026-06-30', 1),
(2, 2, 1, 6, 2026, 35.00, 'PENDIENTE', '2026-06-01 09:00:00', '2026-06-30', 1),
(3, 3, 3, 6, 2026, 20.00, 'PAGADO', '2026-06-01 09:00:00', '2026-06-30', 1),
(4, 4, 4, 6, 2026, 50.00, 'PENDIENTE', '2026-06-10 10:00:00', NULL, 1);

INSERT INTO pagos (id_cuota, id_turno, id_usuario_cobro, metodo_pago, numero_operacion, monto_pagado, fecha_pago, estado_pago) VALUES
(2, 2, 2, 'EFECTIVO', 'REC-20260623-001', 84.00, '2026-06-23 10:30:00', 'REGISTRADO'),
(4, 2, 2, 'YAPE_PLIN', 'YP-778900', 20.00, '2026-06-23 11:10:00', 'REGISTRADO');

INSERT INTO comprobantes (id_pago, id_cuota, numero_comprobante, fecha_emision, monto_total, metodo_pago, estado_comprobante) VALUES
(1, 2, 'CP-202606-000001', '2026-06-23 10:30:00', 84.00, 'EFECTIVO', 'EMITIDO'),
(2, 4, 'CP-202606-000002', '2026-06-23 11:10:00', 20.00, 'YAPE_PLIN', 'EMITIDO');

INSERT INTO transferencias_titularidad (id_puesto, id_contrato_saliente, id_socio_saliente, id_socio_entrante, id_contrato_entrante, id_usuario_tramite, costo_transferencia, deuda_validada, asume_deuda, monto_deuda_asumida, estado_transferencia, observacion, fecha_tramite) VALUES
(6, NULL, NULL, 1, 6, 1, 0.00, TRUE, FALSE, 0.00, 'REGISTRADA', 'Asignacion inicial a la asociacion.', '2026-06-01 10:00:00');

INSERT INTO auditoria_eventos (modulo, tipo_evento, entidad_afectada, id_registro_afectado, id_usuario, descripcion) VALUES
('PATRIMONIO', 'REGISTRO_CONTRATO', 'contratos_ocupacion', 1, 1, 'Contrato inicial de ocupacion registrado.'),
('TESORERIA', 'APERTURA_CAJA', 'turnos_caja', 1, 2, 'Turno de caja abierto para recaudacion diaria.');

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
