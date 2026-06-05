package cl.vantix.mshub.infrastructure.web.mapper;

import cl.vantix.mshub.domain.model.*;
import cl.vantix.mshub.infrastructure.web.dto.response.*;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class WebMapper {

    public UsuarioResponse toUsuarioResponse(Usuario u) {
        return UsuarioResponse.builder()
                .id(u.getId()).nombre(u.getNombre())
                .apellidoPaterno(u.getApellidoPaterno())
                .apellidoMaterno(u.getApellidoMaterno())
                .email(u.getEmail())
                .roles(u.getRoles().stream().map(Enum::name).collect(Collectors.toSet()))
                .telefono(u.getTelefono()).activo(u.isActivo())
                .confirmado(u.isConfirmado()).creadoEn(u.getCreadoEn()).build();
    }

    public ConvocatoriaResponse toConvocatoriaResponse(Convocatoria c) {
        return ConvocatoriaResponse.builder()
                .id(c.getId())
                .titulo(c.getTitulo())
                .descripcion(c.getDescripcion())
                .organizacion(c.getOrganizacion())
                .fechaInicio(c.getFechaInicio())
                .fechaFin(c.getFechaFin())
                .imagen(c.getImagen())
                .estado(c.getEstado().name())
                .tipoRegistro(c.getTipoRegistro().name())
                .maxPostulacionesPorUsuario(c.getMaxPostulacionesPorUsuario())
                .etiquetas(c.getEtiquetas().stream()
                        .map(e -> EtiquetaResponse.builder().id(e.getId())
                                .nombre(e.getNombre()).color(e.getColor()).build())
                        .collect(Collectors.toSet()))
                .creadoEn(c.getCreadoEn())
                .actualizadoEn(c.getActualizadoEn())
                .build();
    }


    public EtapaResponse toEtapaResponse(Etapa e) {
        return EtapaResponse.builder()
                .id(e.getId()).convocatoriaId(e.getConvocatoriaId()).nombre(e.getNombre())
                .tipoEtapa(e.getTipoEtapa().name()).orden(e.getOrden())
                .fechaInicio(e.getFechaInicio()).fechaFin(e.getFechaFin())
                .modoEvaluacion(e.getModoEvaluacion() != null ? e.getModoEvaluacion().name() : null)
                .instruccionesPostulante(e.getInstruccionesPostulante())
                .instruccionesRevisor(e.getInstruccionesRevisor())
                .campos(e.getCampos().stream().map(this::toCampoResponse).collect(Collectors.toList()))
                .criterios(e.getCriterios().stream().map(this::toCriterioResponse).collect(Collectors.toList()))
                .build();
    }

    public CampoFormularioResponse toCampoResponse(CampoFormulario c) {
        return CampoFormularioResponse.builder()
                .id(c.getId()).etapaId(c.getEtapaId()).nombre(c.getNombre())
                .mensajeAyuda(c.getMensajeAyuda()).tipoCampo(c.getTipoCampo().name())
                .obligatorio(c.isObligatorio()).orden(c.getOrden()).opciones(c.getOpciones())
                .maxCaracteres(c.getMaxCaracteres()).formatosPermitidos(c.getFormatosPermitidos())
                .maxArchivos(c.getMaxArchivos()).build();
    }

    public CriterioEvaluacionResponse toCriterioResponse(CriterioEvaluacion c) {
        return CriterioEvaluacionResponse.builder()
                .id(c.getId()).etapaId(c.getEtapaId()).nombre(c.getNombre())
                .descripcion(c.getDescripcion()).ponderador(c.getPonderador())
                .puntajeMinimo(c.getPuntajeMinimo()).puntajeMaximo(c.getPuntajeMaximo())
                .modoCriterio(c.getModoCriterio() != null ? c.getModoCriterio().name() : null)
                .rubrica(c.getRubrica()).orden(c.getOrden()).build();
    }

    public PostulacionResponse toPostulacionResponse(Postulacion p) {
        return PostulacionResponse.builder()
                .id(p.getId()).postulanteId(p.getPostulanteId())
                .convocatoriaId(p.getConvocatoriaId())
                .estado(p.getEstado().name()).porcentajeAvance(p.getPorcentajeAvance())
                .enviadaEn(p.getEnviadaEn()).creadoEn(p.getCreadoEn()).build();
    }

    public EvaluacionResponse toEvaluacionResponse(Evaluacion e) {
        return EvaluacionResponse.builder()
                .id(e.getId()).postulacionId(e.getPostulacionId())
                .revisorId(e.getRevisorId()).etapaId(e.getEtapaId())
                .estado(e.getEstado().name()).puntajeTotal(e.getPuntajeTotal())
                .respuestasEvaluador(e.getRespuestasEvaluador().stream()
                        .map(r -> RespuestaEvaluadorResponse.builder()
                                .id(r.getId()).criterioId(r.getCriterioId())
                                .puntaje(r.getPuntaje()).comentario(r.getComentario()).build())
                        .collect(Collectors.toList()))
                .enviada(e.isEnviada()).enviadaEn(e.getEnviadaEn()).creadoEn(e.getCreadoEn()).build();
    }

    public NotificacionResponse toNotificacionResponse(Notificacion n) {
        return NotificacionResponse.builder()
                .id(n.getId()).titulo(n.getTitulo()).mensaje(n.getMensaje())
                .tipo(n.getTipo().name()).leida(n.isLeida()).creadoEn(n.getCreadoEn()).build();
    }

    public DashboardResponse toDashboardResponse(Dashboard d) {
        return DashboardResponse.builder()
                .convocatoriaId(d.getConvocatoriaId()).convocatoriaNombre(d.getConvocatoriaNombre())
                .totalPostulaciones(d.getTotalPostulaciones())
                .postulacionesEnviadas(d.getPostulacionesEnviadas())
                .postulacionesEnEvaluacion(d.getPostulacionesEnEvaluacion())
                .postulacionesSeleccionadas(d.getPostulacionesSeleccionadas())
                .postulacionesNoSeleccionadas(d.getPostulacionesNoSeleccionadas())
                .promedioGeneral(d.getPromedioGeneral())
                .postulacionesPorEstado(d.getPostulacionesPorEstado()).build();
    }
}
