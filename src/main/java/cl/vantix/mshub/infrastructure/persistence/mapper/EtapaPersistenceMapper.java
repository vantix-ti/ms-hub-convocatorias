package cl.vantix.mshub.infrastructure.persistence.mapper;

import cl.vantix.mshub.domain.model.*;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import org.springframework.stereotype.Component;

@Component
public class EtapaPersistenceMapper {

    public Etapa toDomain(EtapaJpa jpa) {
        if (jpa == null) return null;
        return Etapa.builder()
                .id(jpa.getId())
                .convocatoriaId(jpa.getConvocatoria().getId())
                .nombre(jpa.getNombre())
                .tipoEtapa(TipoEtapa.valueOf(jpa.getTipoEtapa().getNombre()))
                .orden(jpa.getOrden())
                .fechaInicio(jpa.getFechaInicio()).fechaFin(jpa.getFechaFin())
                .modoEvaluacion(jpa.getModoEvaluacion() != null
                        ? ModoEvaluacion.valueOf(jpa.getModoEvaluacion().getNombre()) : null)
                .instruccionesPostulante(jpa.getInstruccionesPostulante())
                .instruccionesRevisor(jpa.getInstruccionesRevisor())
                .mensajeEnvioEmail(jpa.getMensajeEnvioEmail())
                .build();
    }

    public CampoFormulario campoDomain(CampoFormularioJpa jpa) {
        if (jpa == null) return null;
        return CampoFormulario.builder()
                .id(jpa.getId()).etapaId(jpa.getEtapa().getId())
                .nombre(jpa.getNombre()).mensajeAyuda(jpa.getMensajeAyuda())
                .tipoCampo(TipoCampo.valueOf(jpa.getTipoCampo().getNombre()))
                .obligatorio(jpa.isObligatorio()).orden(jpa.getOrden())
                .opciones(jpa.getOpciones()).condicional(jpa.isCondicional())
                .campoCondicionId(jpa.getCampoCondicionId())
                .maxArchivos(jpa.getMaxArchivos())
                .formatosPermitidos(jpa.getFormatosPermitidos())
                .maxCaracteres(jpa.getMaxCaracteres()).build();
    }

    public CriterioEvaluacion criterioDomain(CriterioEvaluacionJpa jpa) {
        if (jpa == null) return null;
        return CriterioEvaluacion.builder()
                .id(jpa.getId()).etapaId(jpa.getEtapa().getId())
                .nombre(jpa.getNombre()).descripcion(jpa.getDescripcion())
                .ponderador(jpa.getPonderador())
                .puntajeMinimo(jpa.getPuntajeMinimo()).puntajeMaximo(jpa.getPuntajeMaximo())
                .modoCriterio(jpa.getModoCriterio() != null
                        ? ModoCriterio.valueOf(jpa.getModoCriterio().getNombre()) : null)
                .rubrica(jpa.getRubrica()).orden(jpa.getOrden()).build();
    }
}
