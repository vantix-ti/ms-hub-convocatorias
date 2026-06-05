package cl.vantix.mshub.infrastructure.persistence.adapter;

import cl.vantix.mshub.domain.model.Etapa;
import cl.vantix.mshub.domain.port.out.EtapaPersistencePort;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import cl.vantix.mshub.infrastructure.persistence.mapper.EtapaPersistenceMapper;
import cl.vantix.mshub.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component @RequiredArgsConstructor
public class EtapaPersistenceAdapter implements EtapaPersistencePort {

    private final EtapaJpaRepository etapaRepo;
    private final TipoEtapaJpaRepository tipoEtapaRepo;
    private final ModoEvaluacionJpaRepository modoEvalRepo;
    private final ConvocatoriaJpaRepository convRepo;
    private final EtapaPersistenceMapper mapper;

    @Override public List<Etapa> findByConvocatoriaId(Long id) {
        return etapaRepo.findByConvocatoria_IdOrderByOrdenAsc(id)
                .stream().map(mapper::toDomain).collect(Collectors.toList());
    }
    @Override public Optional<Etapa> findById(Long id) {
        return etapaRepo.findById(id).map(mapper::toDomain);
    }
    @Override public void deleteById(Long id) { etapaRepo.deleteById(id); }
    @Override public Etapa save(Etapa domain) {
        TipoEtapaJpa tipoEtapa = tipoEtapaRepo.findByNombre(domain.getTipoEtapa().name())
                .orElseThrow(() -> new RuntimeException("TipoEtapa no encontrado"));
        ModoEvaluacionJpa modoEval = domain.getModoEvaluacion() != null
                ? modoEvalRepo.findByNombre(domain.getModoEvaluacion().name()).orElse(null) : null;
        ConvocatoriaJpa conv = convRepo.findById(domain.getConvocatoriaId())
                .orElseThrow(() -> new RuntimeException("Convocatoria no encontrada"));
        EtapaJpa jpa = domain.getId() != null
                ? etapaRepo.findById(domain.getId()).orElse(EtapaJpa.builder().build())
                : EtapaJpa.builder().build();
        jpa.setConvocatoria(conv); jpa.setNombre(domain.getNombre());
        jpa.setTipoEtapa(tipoEtapa); jpa.setOrden(domain.getOrden());
        jpa.setFechaInicio(domain.getFechaInicio()); jpa.setFechaFin(domain.getFechaFin());
        jpa.setModoEvaluacion(modoEval);
        jpa.setInstruccionesPostulante(domain.getInstruccionesPostulante());
        jpa.setInstruccionesRevisor(domain.getInstruccionesRevisor());
        return mapper.toDomain(etapaRepo.save(jpa));
    }
}
