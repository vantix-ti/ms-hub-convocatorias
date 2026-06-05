-- ============================================================
-- V1 — Creación de tablas
-- ms-hub-convocatorias | Modelo Normalizado v2.0
-- Vantix SpA | PostgreSQL 16
-- ============================================================

-- ── Tablas de referencia (lookup) ──────────────────────────

CREATE TABLE permisos (
    id          BIGSERIAL    PRIMARY KEY,
    codigo      VARCHAR(80)  NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    modulo      VARCHAR(60)
);

CREATE TABLE roles (
    id          BIGSERIAL    PRIMARY KEY,
    nombre      VARCHAR(60)  NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    activo      BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE rol_permisos (
    rol_id     BIGINT NOT NULL REFERENCES roles(id)    ON DELETE CASCADE,
    permiso_id BIGINT NOT NULL REFERENCES permisos(id) ON DELETE CASCADE,
    PRIMARY KEY (rol_id, permiso_id)
);

CREATE TABLE estados_convocatoria (
    id          BIGSERIAL   PRIMARY KEY,
    nombre      VARCHAR(60) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    orden       INT         NOT NULL DEFAULT 0
);

CREATE TABLE tipos_registro (
    id          BIGSERIAL   PRIMARY KEY,
    nombre      VARCHAR(60) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE tipos_etapa (
    id          BIGSERIAL   PRIMARY KEY,
    nombre      VARCHAR(60) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE modos_evaluacion (
    id          BIGSERIAL   PRIMARY KEY,
    nombre      VARCHAR(60) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE tipos_campo (
    id                BIGSERIAL   PRIMARY KEY,
    nombre            VARCHAR(60) NOT NULL UNIQUE,
    descripcion       VARCHAR(255),
    requiere_opciones BOOLEAN     NOT NULL DEFAULT FALSE,
    requiere_archivos BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE TABLE modos_criterio (
    id          BIGSERIAL   PRIMARY KEY,
    nombre      VARCHAR(60) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE estados_postulacion (
    id          BIGSERIAL   PRIMARY KEY,
    nombre      VARCHAR(60) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    orden       INT         NOT NULL DEFAULT 0,
    es_terminal BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE TABLE estados_evaluacion (
    id          BIGSERIAL   PRIMARY KEY,
    nombre      VARCHAR(60) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    orden       INT         NOT NULL DEFAULT 0
);

CREATE TABLE tipos_notificacion (
    id          BIGSERIAL   PRIMARY KEY,
    nombre      VARCHAR(60) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    activo      BOOLEAN     NOT NULL DEFAULT TRUE
);

-- ── Tablas principales ──────────────────────────────────────

CREATE TABLE usuarios (
    id                      BIGSERIAL    PRIMARY KEY,
    nombre                  VARCHAR(100) NOT NULL,
    apellido_paterno        VARCHAR(100) NOT NULL,
    apellido_materno        VARCHAR(100),
    email                   VARCHAR(150) NOT NULL UNIQUE,
    password                VARCHAR      NOT NULL,
    confirmado              BOOLEAN      NOT NULL DEFAULT FALSE,
    activo                  BOOLEAN      NOT NULL DEFAULT TRUE,
    telefono                VARCHAR(20),
    avatar_url              VARCHAR(500),
    token_confirmacion      VARCHAR,
    token_expiracion        TIMESTAMP,
    token_reset             VARCHAR,
    token_reset_expiracion  TIMESTAMP,
    two_factor_enabled      BOOLEAN      NOT NULL DEFAULT FALSE,
    intentos_fallidos       INT          NOT NULL DEFAULT 0,
    bloqueado_hasta         TIMESTAMP,
    creado_en               TIMESTAMP    NOT NULL DEFAULT NOW(),
    actualizado_en          TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE usuario_roles (
    usuario_id   BIGINT    NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    rol_id       BIGINT    NOT NULL REFERENCES roles(id)    ON DELETE RESTRICT,
    asignado_en  TIMESTAMP NOT NULL DEFAULT NOW(),
    asignado_por BIGINT    REFERENCES usuarios(id),
    PRIMARY KEY (usuario_id, rol_id)
);

CREATE TABLE etiquetas (
    id     BIGSERIAL   PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    color  VARCHAR(20)
);

CREATE TABLE convocatorias (
    id                            BIGSERIAL PRIMARY KEY,
    nombre                        VARCHAR   NOT NULL,
    descripcion                   TEXT,
    imagen_portada                VARCHAR,
    estado_id                     BIGINT    NOT NULL REFERENCES estados_convocatoria(id),
    tipo_registro_id              BIGINT    NOT NULL REFERENCES tipos_registro(id),
    max_postulaciones_por_usuario INT       NOT NULL DEFAULT 1,
    creado_por_id                 BIGINT    REFERENCES usuarios(id),
    creado_en                     TIMESTAMP NOT NULL DEFAULT NOW(),
    actualizado_en                TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE convocatoria_etiquetas (
    convocatoria_id BIGINT NOT NULL REFERENCES convocatorias(id) ON DELETE CASCADE,
    etiqueta_id     BIGINT NOT NULL REFERENCES etiquetas(id)     ON DELETE CASCADE,
    PRIMARY KEY (convocatoria_id, etiqueta_id)
);

CREATE TABLE etapas (
    id                            BIGSERIAL PRIMARY KEY,
    convocatoria_id               BIGINT    NOT NULL REFERENCES convocatorias(id) ON DELETE CASCADE,
    nombre                        VARCHAR   NOT NULL,
    tipo_etapa_id                 BIGINT    NOT NULL REFERENCES tipos_etapa(id),
    orden                         INT       NOT NULL,
    fecha_inicio                  TIMESTAMP,
    fecha_fin                     TIMESTAMP,
    modo_evaluacion_id            BIGINT    REFERENCES modos_evaluacion(id),
    instrucciones_postulante      TEXT,
    instrucciones_revisor         TEXT,
    mensaje_envio_email           TEXT,
    mensaje_seleccionado_email    TEXT,
    mensaje_no_seleccionado_email TEXT
);

CREATE TABLE campos_formulario (
    id                  BIGSERIAL PRIMARY KEY,
    etapa_id            BIGINT    NOT NULL REFERENCES etapas(id) ON DELETE CASCADE,
    nombre              VARCHAR   NOT NULL,
    mensaje_ayuda       VARCHAR,
    tipo_campo_id       BIGINT    NOT NULL REFERENCES tipos_campo(id),
    obligatorio         BOOLEAN   NOT NULL DEFAULT FALSE,
    orden               INT       NOT NULL,
    condicional         BOOLEAN   NOT NULL DEFAULT FALSE,
    campo_condicion_id  BIGINT    REFERENCES campos_formulario(id),
    valor_condicion     VARCHAR,
    max_archivos        INT       NOT NULL DEFAULT 1,
    formatos_permitidos VARCHAR,
    max_caracteres      INT       NOT NULL DEFAULT 0
);

CREATE TABLE campo_opciones (
    campo_id BIGINT       NOT NULL REFERENCES campos_formulario(id) ON DELETE CASCADE,
    opcion   VARCHAR(255) NOT NULL,
    orden    INT          NOT NULL DEFAULT 0,
    PRIMARY KEY (campo_id, opcion)
);

CREATE TABLE criterios_evaluacion (
    id               BIGSERIAL PRIMARY KEY,
    etapa_id         BIGINT    NOT NULL REFERENCES etapas(id) ON DELETE CASCADE,
    nombre           VARCHAR   NOT NULL,
    descripcion      TEXT,
    ponderador       DOUBLE PRECISION,
    puntaje_minimo   INT       NOT NULL DEFAULT 0,
    puntaje_maximo   INT       NOT NULL DEFAULT 100,
    modo_criterio_id BIGINT    REFERENCES modos_criterio(id),
    rubrica          TEXT,
    orden            INT       NOT NULL DEFAULT 0
);

CREATE TABLE postulaciones (
    id                    BIGSERIAL PRIMARY KEY,
    postulante_id         BIGINT    NOT NULL REFERENCES usuarios(id),
    convocatoria_id       BIGINT    NOT NULL REFERENCES convocatorias(id),
    etapa_actual_id       BIGINT    REFERENCES etapas(id),
    estado_postulacion_id BIGINT    NOT NULL REFERENCES estados_postulacion(id),
    porcentaje_avance     INT       NOT NULL DEFAULT 0,
    enviada_en            TIMESTAMP,
    creado_en             TIMESTAMP NOT NULL DEFAULT NOW(),
    actualizado_en        TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE respuestas_formulario (
    id             BIGSERIAL PRIMARY KEY,
    postulacion_id BIGINT    NOT NULL REFERENCES postulaciones(id) ON DELETE CASCADE,
    campo_id       BIGINT    NOT NULL REFERENCES campos_formulario(id),
    valor_texto    TEXT,
    valor_numero   DOUBLE PRECISION,
    valor_fecha    DATE
);

CREATE TABLE archivos_adjuntos (
    id                  BIGSERIAL PRIMARY KEY,
    respuesta_id        BIGINT    NOT NULL REFERENCES respuestas_formulario(id) ON DELETE CASCADE,
    nombre_original     VARCHAR   NOT NULL,
    nombre_almacenado   VARCHAR   NOT NULL,
    mime_type           VARCHAR(120),
    tamanio             BIGINT,
    url                 VARCHAR,
    creado_en           TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE evaluaciones (
    id                   BIGSERIAL PRIMARY KEY,
    postulacion_id       BIGINT    NOT NULL REFERENCES postulaciones(id),
    revisor_id           BIGINT    NOT NULL REFERENCES usuarios(id),
    etapa_id             BIGINT    NOT NULL REFERENCES etapas(id),
    estado_evaluacion_id BIGINT    NOT NULL REFERENCES estados_evaluacion(id),
    puntaje_total        DOUBLE PRECISION,
    enviada              BOOLEAN   NOT NULL DEFAULT FALSE,
    enviada_en           TIMESTAMP,
    creado_en            TIMESTAMP NOT NULL DEFAULT NOW(),
    actualizado_en       TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE respuestas_evaluador (
    id            BIGSERIAL PRIMARY KEY,
    evaluacion_id BIGINT    NOT NULL REFERENCES evaluaciones(id) ON DELETE CASCADE,
    criterio_id   BIGINT    NOT NULL REFERENCES criterios_evaluacion(id),
    puntaje       DOUBLE PRECISION,
    comentario    TEXT
);

CREATE TABLE asignaciones_revisores (
    id             BIGSERIAL PRIMARY KEY,
    revisor_id     BIGINT    NOT NULL REFERENCES usuarios(id),
    etapa_id       BIGINT    NOT NULL REFERENCES etapas(id),
    postulacion_id BIGINT    NOT NULL REFERENCES postulaciones(id),
    asignado_en    TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE notificaciones (
    id                   BIGSERIAL PRIMARY KEY,
    destinatario_id      BIGINT    NOT NULL REFERENCES usuarios(id),
    tipo_notificacion_id BIGINT    NOT NULL REFERENCES tipos_notificacion(id),
    titulo               VARCHAR   NOT NULL,
    mensaje              TEXT,
    leida                BOOLEAN   NOT NULL DEFAULT FALSE,
    creado_en            TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ── Índices ─────────────────────────────────────────────────

CREATE INDEX idx_usuario_roles_usuario    ON usuario_roles(usuario_id);
CREATE INDEX idx_usuario_roles_rol        ON usuario_roles(rol_id);
CREATE INDEX idx_convocatorias_estado     ON convocatorias(estado_id);
CREATE INDEX idx_etapas_convocatoria      ON etapas(convocatoria_id);
CREATE INDEX idx_etapas_orden             ON etapas(convocatoria_id, orden);
CREATE INDEX idx_campos_etapa             ON campos_formulario(etapa_id);
CREATE INDEX idx_criterios_etapa          ON criterios_evaluacion(etapa_id);
CREATE INDEX idx_postulaciones_postulante ON postulaciones(postulante_id);
CREATE INDEX idx_postulaciones_conv       ON postulaciones(convocatoria_id);
CREATE INDEX idx_postulaciones_estado     ON postulaciones(estado_postulacion_id);
CREATE INDEX idx_respuestas_postulacion   ON respuestas_formulario(postulacion_id);
CREATE INDEX idx_respuestas_campo         ON respuestas_formulario(campo_id);
CREATE INDEX idx_evaluaciones_revisor     ON evaluaciones(revisor_id);
CREATE INDEX idx_evaluaciones_postulacion ON evaluaciones(postulacion_id);
CREATE INDEX idx_notif_destinatario       ON notificaciones(destinatario_id);
CREATE INDEX idx_notif_leida              ON notificaciones(destinatario_id, leida);
