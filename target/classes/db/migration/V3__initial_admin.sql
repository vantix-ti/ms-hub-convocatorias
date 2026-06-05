-- ============================================================
-- V3 — Usuario administrador inicial
-- ms-hub-convocatorias | Vantix SpA
-- Password: Admin123! (BCrypt)
-- ⚠️  Cambiar la contraseña en el primer login en producción
-- ============================================================

INSERT INTO usuarios (
    nombre, apellido_paterno, email, password,
    confirmado, activo, creado_en, actualizado_en
) VALUES (
    'Administrador', 'Sistema',
    'admin@vantix.cl',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lkHq',
    TRUE, TRUE, NOW(), NOW()
);

INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u, roles r
WHERE u.email = 'admin@vantix.cl'
  AND r.nombre = 'ADMIN';
