-- V6: Ampliar logo_url a TEXT para soportar imágenes en base64
ALTER TABLE instituciones ALTER COLUMN logo_url TYPE TEXT;
