-- ============================================================
-- V2 — Seed de tablas de referencia
-- ms-hub-convocatorias | Vantix SpA
-- ============================================================

-- ── Permisos ────────────────────────────────────────────────
INSERT INTO permisos (codigo, descripcion, modulo) VALUES
  ('convocatoria:ver',               'Ver convocatorias publicadas',          'convocatorias'),
  ('convocatoria:crear',             'Crear convocatoria',                    'convocatorias'),
  ('convocatoria:editar',            'Editar convocatoria',                   'convocatorias'),
  ('convocatoria:eliminar',          'Eliminar convocatoria',                 'convocatorias'),
  ('convocatoria:cambiar_estado',    'Cambiar estado de convocatoria',        'convocatorias'),
  ('etapa:gestionar',                'Crear/editar/eliminar etapas y campos', 'etapas'),
  ('postulacion:crear',              'Crear postulación propia',              'postulaciones'),
  ('postulacion:ver_propia',         'Ver sus propias postulaciones',         'postulaciones'),
  ('postulacion:ver_todas',          'Ver todas las postulaciones',           'postulaciones'),
  ('evaluacion:ver_propia',          'Ver evaluaciones asignadas al revisor', 'evaluaciones'),
  ('evaluacion:gestionar',           'Asignar revisores y gestionar eval.',   'evaluaciones'),
  ('evaluacion:notificar',           'Enviar notificaciones de resultados',   'evaluaciones'),
  ('usuario:ver_revisores',          'Ver listado de revisores',              'usuarios'),
  ('usuario:cambiar_rol',            'Cambiar rol de un usuario',             'usuarios'),
  ('dashboard:ver',                  'Ver dashboard de convocatoria',         'dashboard');

-- ── Roles ────────────────────────────────────────────────────
INSERT INTO roles (nombre, descripcion) VALUES
  ('ADMIN',      'Administrador de la plataforma — acceso total'),
  ('POSTULANTE', 'Postulante — puede crear y enviar postulaciones'),
  ('REVISOR',    'Revisor — evalúa postulaciones asignadas');

-- ── Rol → Permisos ──────────────────────────────────────────

-- ADMIN: todos los permisos
INSERT INTO rol_permisos (rol_id, permiso_id)
  SELECT r.id, p.id FROM roles r, permisos p WHERE r.nombre = 'ADMIN';

-- POSTULANTE
INSERT INTO rol_permisos (rol_id, permiso_id)
  SELECT r.id, p.id FROM roles r, permisos p
  WHERE r.nombre = 'POSTULANTE'
    AND p.codigo IN (
      'convocatoria:ver',
      'postulacion:crear',
      'postulacion:ver_propia'
    );

-- REVISOR
INSERT INTO rol_permisos (rol_id, permiso_id)
  SELECT r.id, p.id FROM roles r, permisos p
  WHERE r.nombre = 'REVISOR'
    AND p.codigo IN (
      'convocatoria:ver',
      'postulacion:ver_todas',
      'evaluacion:ver_propia',
      'dashboard:ver'
    );

-- ── Estados de convocatoria ─────────────────────────────────
INSERT INTO estados_convocatoria (nombre, descripcion, orden) VALUES
  ('BORRADOR',   'En construcción, no visible para postulantes',  1),
  ('PUBLICADA',  'Abierta y visible, recibiendo postulaciones',   2),
  ('EVALUACION', 'Cerrada al público, en proceso de evaluación',  3),
  ('FINALIZADA', 'Proceso completado, resultados publicados',     4);

-- ── Tipos de registro ───────────────────────────────────────
INSERT INTO tipos_registro (nombre, descripcion) VALUES
  ('ABIERTO',        'Cualquier usuario registrado puede postular'),
  ('POR_INVITACION', 'Solo usuarios invitados pueden postular');

-- ── Tipos de etapa ──────────────────────────────────────────
INSERT INTO tipos_etapa (nombre, descripcion) VALUES
  ('RECEPCION',  'Etapa de recepción de formularios y archivos'),
  ('EVALUACION', 'Etapa de evaluación por revisores');

-- ── Modos de evaluación ─────────────────────────────────────
INSERT INTO modos_evaluacion (nombre, descripcion) VALUES
  ('VENTANILLA_ABIERTA', 'Los revisores pueden ver los puntajes de otros revisores'),
  ('VENTANILLA_CERRADA', 'Cada revisor evalúa de forma independiente y ciega');

-- ── Tipos de campo ──────────────────────────────────────────
INSERT INTO tipos_campo (nombre, descripcion, requiere_opciones, requiere_archivos) VALUES
  ('TEXTO',              'Campo de texto corto (una línea)',                FALSE, FALSE),
  ('TEXTO_LARGO',        'Área de texto multilínea',                       FALSE, FALSE),
  ('NUMERO',             'Campo numérico entero o decimal',                FALSE, FALSE),
  ('FECHA',              'Selector de fecha',                              FALSE, FALSE),
  ('ARCHIVO',            'Carga de archivos adjuntos',                     FALSE, TRUE),
  ('LISTA',              'Lista desplegable de opciones',                  TRUE,  FALSE),
  ('SELECCION_UNICA',    'Radio buttons — una sola opción',                TRUE,  FALSE),
  ('SELECCION_MULTIPLE', 'Checkboxes — múltiples opciones',                TRUE,  FALSE),
  ('RUT',                'Campo RUT chileno con validación de dígito verificador', FALSE, FALSE),
  ('TABLA',              'Tabla dinámica con filas agregables',            FALSE, FALSE),
  ('TABLA_FIJA',         'Tabla con filas predefinidas',                   FALSE, FALSE),
  ('SECCION_TEXTO',      'Bloque de texto informativo (no es una respuesta)', FALSE, FALSE);

-- ── Modos de criterio ───────────────────────────────────────
INSERT INTO modos_criterio (nombre, descripcion) VALUES
  ('PUNTAJE', 'El revisor asigna un puntaje numérico al criterio'),
  ('RANKING', 'Los criterios se ordenan por importancia relativa');

-- ── Estados de postulación ──────────────────────────────────
INSERT INTO estados_postulacion (nombre, descripcion, orden, es_terminal) VALUES
  ('EN_CREACION',    'La postulación está siendo completada por el postulante', 1, FALSE),
  ('ENVIADA',        'El postulante envió la postulación correctamente',        2, FALSE),
  ('EN_EVALUACION',  'La postulación está siendo evaluada por revisores',       3, FALSE),
  ('SELECCIONADA',   'La postulación fue seleccionada como ganadora',           4, TRUE),
  ('NO_SELECCIONADA','La postulación no fue seleccionada',                      4, TRUE);

-- ── Estados de evaluación ───────────────────────────────────
INSERT INTO estados_evaluacion (nombre, descripcion, orden) VALUES
  ('PENDIENTE',   'Asignada pero no iniciada por el revisor',      1),
  ('EN_PROGRESO', 'El revisor ha comenzado a completar la rúbrica',2),
  ('FINALIZADA',  'El revisor completó y envió la evaluación',     3);

-- ── Tipos de notificación ───────────────────────────────────
INSERT INTO tipos_notificacion (nombre, descripcion, activo) VALUES
  ('EMAIL',   'Notificación enviada por correo electrónico',  TRUE),
  ('SISTEMA', 'Notificación interna visible en la plataforma',TRUE),
  ('PUSH',    'Notificación push (para futura app móvil)',    FALSE);
