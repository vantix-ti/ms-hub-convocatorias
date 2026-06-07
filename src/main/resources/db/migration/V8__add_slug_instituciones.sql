-- V8: slug único por institución (usado como URL de entrada pública)
ALTER TABLE instituciones ADD COLUMN IF NOT EXISTS slug VARCHAR(100);
CREATE UNIQUE INDEX IF NOT EXISTS idx_instituciones_slug ON instituciones(slug) WHERE slug IS NOT NULL;
