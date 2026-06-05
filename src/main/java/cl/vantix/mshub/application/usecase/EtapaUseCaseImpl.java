package cl.vantix.mshub.application.usecase;

import cl.vantix.mshub.domain.exception.ResourceNotFoundException;
import cl.vantix.mshub.domain.model.*;
import cl.vantix.mshub.domain.port.in.EtapaUseCase;
import cl.vantix.mshub.domain.port.out.CampoFormularioPersistencePort;
import cl.vantix.mshub.domain.port.out.ConvocatoriaPersistencePort;
import cl.vantix.mshub.domain.port.out.CriterioEvaluacionPersistencePort;
import cl.vantix.mshub.domain.port.out.EtapaPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EtapaUseCaseImpl implements EtapaUseCase {

    private final EtapaPersistencePort etapaPort;
    private final CampoFormularioPersistencePort campoPort;
    private final CriterioEvaluacionPersistencePort criterioPort;
    private final ConvocatoriaPersistencePort convocatoriaPort;

    @Override public List<Etapa> listarPorConvocatoria(Long convocatoriaId) {
        return etapaPort.findByConvocatoriaId(convocatoriaId);
    }
    @Override public Etapa obtenerPorId(Long id) {
        return etapaPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etapa " + id + " no encontrada."));
    }
    @Override @Transactional
    public Etapa crear(Long convocatoriaId, String nombre, TipoEtapa tipo, int orden,
                       LocalDateTime fechaInicio, LocalDateTime fechaFin,
                       ModoEvaluacion modoEvaluacion, String instrPostulante, String instrRevisor) {
        if (!convocatoriaPort.existsById(convocatoriaId))
            throw new ResourceNotFoundException("Convocatoria " + convocatoriaId + " no encontrada.");
        return etapaPort.save(Etapa.builder()
                .convocatoriaId(convocatoriaId).nombre(nombre).tipoEtapa(tipo).orden(orden)
                .fechaInicio(fechaInicio).fechaFin(fechaFin).modoEvaluacion(modoEvaluacion)
                .instruccionesPostulante(instrPostulante).instruccionesRevisor(instrRevisor).build());
    }
    @Override @Transactional
    public Etapa actualizar(Long id, String nombre, TipoEtapa tipo, int orden,
                            LocalDateTime fechaInicio, LocalDateTime fechaFin,
                            ModoEvaluacion modoEvaluacion, String instrPostulante, String instrRevisor) {
        Etapa e = obtenerPorId(id);
        e.setNombre(nombre); e.setTipoEtapa(tipo); e.setOrden(orden);
        e.setFechaInicio(fechaInicio); e.setFechaFin(fechaFin);
        e.setModoEvaluacion(modoEvaluacion);
        e.setInstruccionesPostulante(instrPostulante);
        e.setInstruccionesRevisor(instrRevisor);
        return etapaPort.save(e);
    }
    @Override @Transactional public void eliminar(Long id) { etapaPort.deleteById(id); }

    @Override @Transactional
    public CampoFormulario agregarCampo(Long etapaId, String nombre, String mensajeAyuda,
                                         TipoCampo tipo, boolean obligatorio, int orden,
                                         List<String> opciones, int maxCaracteres,
                                         String formatosPermitidos, int maxArchivos) {
        obtenerPorId(etapaId);
        return campoPort.save(CampoFormulario.builder()
                .etapaId(etapaId).nombre(nombre).mensajeAyuda(mensajeAyuda).tipoCampo(tipo)
                .obligatorio(obligatorio).orden(orden).opciones(opciones != null ? opciones : List.of())
                .maxCaracteres(maxCaracteres).formatosPermitidos(formatosPermitidos)
                .maxArchivos(maxArchivos > 0 ? maxArchivos : 1).build());
    }
    @Override @Transactional
    public CampoFormulario actualizarCampo(Long campoId, String nombre, String mensajeAyuda,
                                            TipoCampo tipo, boolean obligatorio, int orden,
                                            List<String> opciones, int maxCaracteres,
                                            String formatosPermitidos, int maxArchivos) {
        CampoFormulario c = campoPort.findById(campoId)
                .orElseThrow(() -> new ResourceNotFoundException("Campo " + campoId + " no encontrado."));
        c.setNombre(nombre); c.setMensajeAyuda(mensajeAyuda); c.setTipoCampo(tipo);
        c.setObligatorio(obligatorio); c.setOrden(orden);
        if (opciones != null) c.setOpciones(opciones);
        c.setMaxCaracteres(maxCaracteres); c.setFormatosPermitidos(formatosPermitidos);
        c.setMaxArchivos(maxArchivos > 0 ? maxArchivos : 1);
        return campoPort.save(c);
    }
    @Override @Transactional public void eliminarCampo(Long campoId) { campoPort.deleteById(campoId); }

    @Override @Transactional
    public CriterioEvaluacion agregarCriterio(Long etapaId, String nombre, String descripcion,
                                               Double ponderador, int puntajeMin, int puntajeMax,
                                               ModoCriterio modo, String rubrica, int orden) {
        obtenerPorId(etapaId);
        return criterioPort.save(CriterioEvaluacion.builder()
                .etapaId(etapaId).nombre(nombre).descripcion(descripcion).ponderador(ponderador)
                .puntajeMinimo(puntajeMin).puntajeMaximo(puntajeMax).modoCriterio(modo)
                .rubrica(rubrica).orden(orden).build());
    }
    @Override @Transactional
    public CriterioEvaluacion actualizarCriterio(Long criterioId, String nombre, String descripcion,
                                                  Double ponderador, int puntajeMin, int puntajeMax,
                                                  ModoCriterio modo, String rubrica, int orden) {
        CriterioEvaluacion c = criterioPort.findById(criterioId)
                .orElseThrow(() -> new ResourceNotFoundException("Criterio " + criterioId + " no encontrado."));
        c.setNombre(nombre); c.setDescripcion(descripcion); c.setPonderador(ponderador);
        c.setPuntajeMinimo(puntajeMin); c.setPuntajeMaximo(puntajeMax);
        c.setModoCriterio(modo); c.setRubrica(rubrica); c.setOrden(orden);
        return criterioPort.save(c);
    }
    @Override @Transactional public void eliminarCriterio(Long criterioId) { criterioPort.deleteById(criterioId); }
}
