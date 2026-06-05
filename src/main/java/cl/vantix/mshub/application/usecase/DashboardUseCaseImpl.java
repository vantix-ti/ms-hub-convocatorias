package cl.vantix.mshub.application.usecase;

import cl.vantix.mshub.domain.exception.ResourceNotFoundException;
import cl.vantix.mshub.domain.model.*;
import cl.vantix.mshub.domain.port.in.DashboardUseCase;
import cl.vantix.mshub.domain.port.out.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardUseCaseImpl implements DashboardUseCase {
    private final ConvocatoriaPersistencePort convocatoriaPort;
    private final PostulacionPersistencePort postulacionPort;
    private final EvaluacionPersistencePort evaluacionPort;

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
}
