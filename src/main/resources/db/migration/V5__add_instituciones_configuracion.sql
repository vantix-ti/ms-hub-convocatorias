-- ================================================================
-- V5: Instituciones, ConfiguracionPlataforma y asociación usuarios
-- ================================================================

-- Tabla de instituciones / empresas
CREATE TABLE IF NOT EXISTS instituciones (
    id              BIGSERIAL PRIMARY KEY,
    nombre          VARCHAR(255) NOT NULL,
    rut             VARCHAR(20),
    direccion       VARCHAR(500),
    telefono        VARCHAR(50),
    email           VARCHAR(255),
    logo_url        VARCHAR(500),
    activo          BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en       TIMESTAMP NOT NULL DEFAULT NOW(),
    actualizado_en  TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Tabla de configuración por institución (clave-valor)
CREATE TABLE IF NOT EXISTS configuracion_plataforma (
    id              BIGSERIAL PRIMARY KEY,
    institucion_id  BIGINT NOT NULL REFERENCES instituciones(id),
    clave           VARCHAR(100) NOT NULL,
    valor           TEXT,
    actualizado_en  TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_cfg_inst_clave UNIQUE (institucion_id, clave)
);

-- Agregar columna institucion_id a usuarios (nullable para no romper filas existentes)
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS institucion_id BIGINT REFERENCES instituciones(id);

-- Insertar Vantix SpA como institución por defecto
INSERT INTO instituciones (nombre, rut, direccion, telefono, email, activo)
VALUES ('Vantix SpA', '77.000.000-0', 'Los Militares N° 5620, Of. 905, Las Condes', '+56222000000', 'contacto@vantix.cl', TRUE)
ON CONFLICT DO NOTHING;

-- Insertar configuración por defecto para Vantix
INSERT INTO configuracion_plataforma (institucion_id, clave, valor)
SELECT id, 'nombrePlataforma',    'Hub Convocatorias'       FROM instituciones WHERE nombre = 'Vantix SpA'
ON CONFLICT ON CONSTRAINT uq_cfg_inst_clave DO NOTHING;

INSERT INTO configuracion_plataforma (institucion_id, clave, valor)
SELECT id, 'descripcionInicio',   'Plataforma de gestión integral de inscripción y convocatorias de emprendimiento.'
FROM instituciones WHERE nombre = 'Vantix SpA'
ON CONFLICT ON CONSTRAINT uq_cfg_inst_clave DO NOTHING;

INSERT INTO configuracion_plataforma (institucion_id, clave, valor)
SELECT id, 'registroAbierto',     'true'                    FROM instituciones WHERE nombre = 'Vantix SpA'
ON CONFLICT ON CONSTRAINT uq_cfg_inst_clave DO NOTHING;

INSERT INTO configuracion_plataforma (institucion_id, clave, valor)
SELECT id, 'requiereInvitacion',  'false'                   FROM instituciones WHERE nombre = 'Vantix SpA'
ON CONFLICT ON CONSTRAINT uq_cfg_inst_clave DO NOTHING;

INSERT INTO configuracion_plataforma (institucion_id, clave, valor)
SELECT id, 'limiteIntentos',      '5'                       FROM instituciones WHERE nombre = 'Vantix SpA'
ON CONFLICT ON CONSTRAINT uq_cfg_inst_clave DO NOTHING;

INSERT INTO configuracion_plataforma (institucion_id, clave, valor)
SELECT id, 'remitentEmail',       'no-reply@vantix.cl'      FROM instituciones WHERE nombre = 'Vantix SpA'
ON CONFLICT ON CONSTRAINT uq_cfg_inst_clave DO NOTHING;

INSERT INTO configuracion_plataforma (institucion_id, clave, valor)
SELECT id, 'mensajeBienvenida',   'Bienvenido/a a Hub Convocatorias. Aquí podrás postular a todas las convocatorias disponibles.'
FROM instituciones WHERE nombre = 'Vantix SpA'
ON CONFLICT ON CONSTRAINT uq_cfg_inst_clave DO NOTHING;

INSERT INTO configuracion_plataforma (institucion_id, clave, valor)
SELECT id, 'terminosCondiciones', ''                         FROM instituciones WHERE nombre = 'Vantix SpA'
ON CONFLICT ON CONSTRAINT uq_cfg_inst_clave DO NOTHING;

-- Asociar todos los usuarios existentes (sin institución) a Vantix SpA
UPDATE usuarios
SET institucion_id = (SELECT id FROM instituciones WHERE nombre = 'Vantix SpA')
WHERE institucion_id IS NULL;

-- Agregar entrada en seed de roles para GESTOR si no existe
INSERT INTO roles (nombre) VALUES ('GESTOR') ON CONFLICT (nombre) DO NOTHING;
