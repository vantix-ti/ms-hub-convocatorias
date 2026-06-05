ALTER TABLE convocatorias RENAME COLUMN nombre TO titulo;
ALTER TABLE convocatorias RENAME COLUMN imagen_portada TO imagen;
ALTER TABLE convocatorias ADD COLUMN IF NOT EXISTS organizacion  VARCHAR(255);
ALTER TABLE convocatorias ADD COLUMN IF NOT EXISTS fecha_inicio  DATE;
ALTER TABLE convocatorias ADD COLUMN IF NOT EXISTS fecha_fin     DATE;
