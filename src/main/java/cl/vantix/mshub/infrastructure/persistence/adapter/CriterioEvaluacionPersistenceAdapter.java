package cl.vantix.mshub.infrastructure.persistence.adapter;

import cl.vantix.mshub.domain.model.*;
import cl.vantix.mshub.domain.port.out.CriterioEvaluacionPersistencePort;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import cl.vantix.mshub.infrastructure.persistence.mapper.EtapaPersistenceMapper;
import cl.vantix.mshub.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component @RequiredArgsConstructor
public class CriterioEvaluacionPersistenceAdapter implements CriterioEvaluacionPersistencePort {
    private final CriterioEvaluacionJpaRepository repo;
    private final EtapaJpaRepository etapaRepo;
    private final ModoCriterioJpaRepository modoCriterioRepo;
    private final EtapaPersistenceMapper mapper;

    @Override public Optional<CriterioEvaluacion> findById(Long id) {
        return repo.findById(id).map(mapper::criterioDomain);
    }
    @Override public void deleteById(Long id) { repo.deleteById(id); }
    @Override public CriterioEvaluacion save(CriterioEvaluacion domain) {
        ModoCriterioJpa modo = domain.getModoCriterio() != null
                ? modoCriterioRepo.findByNombre(domain.getModoCriterio().name()).orElse(null) : null;
        EtapaJpa etapa = etapaRepo.findById(domain.getEtapaId())
                .orElseThrow(() -> new RuntimeException("Etapa no encontrada"));
        CriterioEvaluacionJpa jpa = domain.getId() != null
                ? repo.findById(domain.getId()).orElse(CriterioEvaluacionJpa.builder().build())
                : CriterioEvaluacionJpa.builder().build();
        jpa.setEtapa(etapa); jpa.setNombre(domain.getNombre());
        jpa.setDescripcion(domain.getDescripcion()); jpa.setPonderador(domain.getPonderador());
        jpa.setPuntajeMinimo(domain.getPuntajeMinimo()); jpa.setPuntajeMaximo(domain.getPuntajeMaximo());
        jpa.setModoCriterio(modo); jpa.setRubrica(domain.getRubrica()); jpa.setOrden(domain.getOrden());
        return mapper.criterioDomain(repo.save(jpa));
    }
}
