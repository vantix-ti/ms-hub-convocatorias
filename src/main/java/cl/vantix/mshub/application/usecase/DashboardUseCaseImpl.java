package cl.vantix.mshub.application.usecase;

import cl.vantix.mshub.domain.exception.ResourceNotFoundException;
import cl.vantix.mshub.domain.model.*;
import cl.vantix.mshub.domain.port.in.DashboardUseCase;
import cl.vantix.mshub.domain.port.out.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardUseCaseImpl implements DashboardUseCase {
    private final ConvocatoriaPersistencePort convocatoriaPort;
    private final PostulacionPersistencePort postulacionPort;
    private final EvaluacionPersistencePort evaluacionPort;
    private final UsuarioPersistencePort usuarioPort;

    @Override
    public Dashboard obtenerDashboard(Long convocatoriaId) {
        Convocatoria conv = convocatoriaPort.findById(convocatoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria no encontrada."));
        List<Postulacion> postulaciones = postulacionPort.findByConvocatoriaId(convocatoriaId);
        Map<String, Long> porEstado = postulaciones.stream()
                .collect(Collectors.groupingBy(p -> p.getEstado().name(), Collectors.counting()));
        List<Evaluacion> evaluaciones = evaluacionPort.findByEtapaId(
                conv.getEtapas().isEmpty() ? -1L : conv.getEtapas().get(0).getId());
        double promedio = evaluaciones.stream()
                .filter(e -> e.getPuntajeTotal() != null)
                .mapToDouble(Evaluacion::getPuntajeTotal).average().orElse(0.0);
        return Dashboard.builder()
                .convocatoriaId(convocatoriaId)
                .convocatoriaNombre(conv.getTitulo())
                .totalPostulaciones(postulaciones.size())
                .postulacionesEnviadas(porEstado.getOrDefault("ENVIADA", 0L))
                .postulacionesEnEvaluacion(porEstado.getOrDefault("EN_EVALUACION", 0L))
                .postulacionesSeleccionadas(porEstado.getOrDefault("SELECCIONADA", 0L))
                .postulacionesNoSeleccionadas(porEstado.getOrDefault("NO_SELECCIONADA", 0L))
                .promedioGeneral(promedio)
                .postulacionesPorEstado(porEstado)
                .build();
    }

    @Override
    public DashboardGlobal obtenerDashboardGlobal() {
        List<Convocatoria> todasConvocatorias = convocatoriaPort.findAll();
        long convocatoriasActivas = todasConvocatorias.stream()
                .filter(c -> c.getEstado() == EstadoConvocatoria.PUBLICADA)
                .count();

        List<Postulacion> todasPostulaciones = postulacionPort.findAll();
        long totalPostulaciones = todasPostulaciones.size();

        long seleccionados = todasPostulaciones.stream()
                .filter(p -> p.getEstado() == EstadoPostulacion.SELECCIONADA)
                .count();
        double seleccionadosPct = totalPostulaciones > 0
                ? Math.round((seleccionados * 100.0 / totalPostulaciones) * 10) / 10.0
                : 0.0;

        Map<String, Long> porEstado = todasPostulaciones.stream()
                .collect(Collectors.groupingBy(p -> p.getEstado().name(), Collectors.counting()));

        long revisores = usuarioPort.findByRol(Rol.REVISOR).size();

        // Últimas 5 postulaciones (más recientes primero)
        Map<Long, Convocatoria> convMap = todasConvocatorias.stream()
                .collect(Collectors.toMap(Convocatoria::getId, c -> c));

        List<DashboardGlobal.UltimaPostulacion> ultimas = todasPostulaciones.stream()
                .filter(p -> p.getCreadoEn() != null)
                .sorted(Comparator.comparing(Postulacion::getCreadoEn).reversed())
                .limit(5)
                .map(p -> {
                    String nombrePostulante = usuarioPort.findById(p.getPostulanteId())
                            .map(u -> u.getNombre() + " " + (u.getApellidoPaterno() != null ? u.getApellidoPaterno() : ""))
                            .orElse("Desconocido");
                    String titConv = Optional.ofNullable(convMap.get(p.getConvocatoriaId()))
                            .map(Convocatoria::getTitulo)
                            .orElse("Sin convocatoria");
                    return DashboardGlobal.UltimaPostulacion.builder()
                            .id(p.getId())
                            .postulanteNombre(nombrePostulante.trim())
                            .convocatoriaTitulo(titConv)
                            .fecha(p.getCreadoEn())
                            .estado(p.getEstado().name())
                            .build();
                })
                .collect(Collectors.toList());

        // Evolución temporal: últimos 14 días agrupados por fecha
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM");
        List<DashboardGlobal.PuntoTemporal> evolucion = new ArrayList<>();
        for (int i = 13; i >= 0; i--) {
            LocalDate dia = hoy.minusDays(i);
            long creadas = todasPostulaciones.stream()
                    .filter(p -> p.getCreadoEn() != null && p.getCreadoEn().toLocalDate().equals(dia))
                    .count();
            long enviadas = todasPostulaciones.stream()
                    .filter(p -> p.getEnviadaEn() != null && p.getEnviadaEn().toLocalDate().equals(dia))
                    .count();
            evolucion.add(DashboardGlobal.PuntoTemporal.builder()
                    .fecha(dia.format(fmt))
                    .creadas(creadas)
                    .enviadas(enviadas)
                    .build());
        }

        return DashboardGlobal.builder()
                .convocatoriasActivas(convocatoriasActivas)
                .totalPostulaciones(totalPostulaciones)
                .revisoresAsignados(revisores)
                .seleccionados(seleccionados)
                .seleccionadosPorcentaje(seleccionadosPct)
                .postulacionesPorEstado(porEstado)
                .ultimasPostulaciones(ultimas)
                .evolucionTemporal(evolucion)
                .build();
    }

    @Override
    public DashboardGlobal obtenerDashboardPorInstitucion(Long institucionId) {
        // IDs de usuarios que pertenecen a esta institución
        java.util.Set<Long> idsInstitucion = usuarioPort.findByInstitucionId(institucionId)
                .stream().map(Usuario::getId).collect(Collectors.toSet());

        // Convocatorias creadas por usuarios de la institución
        List<Convocatoria> convocatorias = convocatoriaPort.findAll().stream()
                .filter(c -> c.getCreadoPorId() != null && idsInstitucion.contains(c.getCreadoPorId()))
                .collect(Collectors.toList());

        long convocatoriasActivas = convocatorias.stream()
                .filter(c -> c.getEstado() == EstadoConvocatoria.PUBLICADA).count();

        java.util.Set<Long> convIds = convocatorias.stream()
                .map(Convocatoria::getId).collect(Collectors.toSet());

        // Postulaciones de esas convocatorias
        List<Postulacion> postulaciones = postulacionPort.findAll().stream()
                .filter(p -> convIds.contains(p.getConvocatoriaId()))
                .collect(Collectors.toList());

        long totalPostulaciones = postulaciones.size();
        long seleccionados = postulaciones.stream()
                .filter(p -> p.getEstado() == EstadoPostulacion.SELECCIONADA).count();
        double seleccionadosPct = totalPostulaciones > 0
                ? Math.round((seleccionados * 100.0 / totalPostulaciones) * 10) / 10.0 : 0.0;

        Map<String, Long> porEstado = postulaciones.stream()
                .collect(Collectors.groupingBy(p -> p.getEstado().name(), Collectors.counting()));

        // Revisores de la institución
        long revisores = usuarioPort.findByInstitucionId(institucionId).stream()
                .filter(u -> u.getRoles() != null && u.getRoles().contains(Rol.REVISOR)).count();

        // Últimas 5 postulaciones
        Map<Long, Convocatoria> convMap = convocatorias.stream()
                .collect(Collectors.toMap(Convocatoria::getId, cv -> cv));

        List<DashboardGlobal.UltimaPostulacion> ultimas = postulaciones.stream()
                .filter(p -> p.getCreadoEn() != null)
                .sorted(Comparator.comparing(Postulacion::getCreadoEn).reversed())
                .limit(5)
                .map(p -> {
                    String nombrePostulante = usuarioPort.findById(p.getPostulanteId())
                            .map(u -> u.getNombre() + " " + (u.getApellidoPaterno() != null ? u.getApellidoPaterno() : ""))
                            .orElse("Desconocido");
                    String titConv = Optional.ofNullable(convMap.get(p.getConvocatoriaId()))
                            .map(Convocatoria::getTitulo).orElse("Sin convocatoria");
                    return DashboardGlobal.UltimaPostulacion.builder()
                            .id(p.getId()).postulanteNombre(nombrePostulante.trim())
                            .convocatoriaTitulo(titConv).fecha(p.getCreadoEn())
                            .estado(p.getEstado().name()).build();
                }).collect(Collectors.toList());

        // Evolución temporal — últimos 14 días
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM");
        List<DashboardGlobal.PuntoTemporal> evolucion = new ArrayList<>();
        for (int i = 13; i >= 0; i--) {
            LocalDate dia = hoy.minusDays(i);
            long creadas = postulaciones.stream()
                    .filter(p -> p.getCreadoEn() != null && p.getCreadoEn().toLocalDate().equals(dia)).count();
            long enviadas = postulaciones.stream()
                    .filter(p -> p.getEnviadaEn() != null && p.getEnviadaEn().toLocalDate().equals(dia)).count();
            evolucion.add(DashboardGlobal.PuntoTemporal.builder()
                    .fecha(dia.format(fmt)).creadas(creadas).enviadas(enviadas).build());
        }

        return DashboardGlobal.builder()
                .convocatoriasActivas(convocatoriasActivas)
                .totalPostulaciones(totalPostulaciones)
                .revisoresAsignados(revisores)
                .seleccionados(seleccionados)
                .seleccionadosPorcentaje(seleccionadosPct)
                .postulacionesPorEstado(porEstado)
                .ultimasPostulaciones(ultimas)
                .evolucionTemporal(evolucion)
                .build();
    }

}
