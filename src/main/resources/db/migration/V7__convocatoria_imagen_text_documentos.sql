-- V7: imagen en base64 (TEXT) y tabla de documentos adjuntos

-- 1. imagen pasa a TEXT sin límite
ALTER TABLE convocatorias ALTER COLUMN imagen TYPE TEXT;

-- 2. Tabla de documentos adjuntos
CREATE TABLE IF NOT EXISTS documento_adjunto (
    id              BIGSERIAL PRIMARY KEY,
    convocatoria_id BIGINT       NOT NULL REFERENCES convocatorias(id) ON DELETE CASCADE,
    nombre          VARCHAR(255) NOT NULL,
    descripcion     VARCHAR(100),
    contenido       TEXT         NOT NULL,   -- base64 del archivo
    tipo_mime       VARCHAR(100),
    tamanio         BIGINT,                  -- bytes originales
    creado_en       TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_documento_adjunto_convocatoria
    ON documento_adjunto(convocatoria_id);
