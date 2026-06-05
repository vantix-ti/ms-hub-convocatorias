package cl.vantix.mshub.infrastructure.persistence.adapter;

import cl.vantix.mshub.domain.model.AsignacionRevisor;
import cl.vantix.mshub.domain.port.out.AsignacionRevisorPersistencePort;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import cl.vantix.mshub.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AsignacionRevisorPersistenceAdapter implements AsignacionRevisorPersistencePort {

    private final AsignacionRevisorJpaRepository asignRepo;
    private final UsuarioJpaRepository usuarioRepo;
    private final PostulacionJpaRepository postulacionRepo;
    private final EtapaJpaRepository etapaRepo;

    @Override
    public AsignacionRevisor save(AsignacionRevisor domain) {
        UsuarioJpa revisor = usuarioRepo.findById(domain.getRevisorId())
                .orElseThrow(() -> new RuntimeException("Revisor no encontrado"));
        PostulacionJpa postulacion = postulacionRepo.findById(domain.getPostulacionId())
                .orElseThrow(() -> new RuntimeException("Postulación no encontrada"));
        EtapaJpa etapa = etapaRepo.findById(domain.getEtapaId())
                .orElseThrow(() -> new RuntimeException("Etapa no encontrada"));
        AsignacionRevisorJpa jpa = AsignacionRevisorJpa.builder()
                .revisor(revisor).postulacion(postulacion).etapa(etapa).build();
        AsignacionRevisorJpa saved = asignRepo.save(jpa);
        return AsignacionRevisor.builder()
                .id(saved.getId())
                .revisorId(revisor.getId())
                .postulacionId(postulacion.getId())
                .etapaId(etapa.getId())
                .asignadoEn(saved.getAsignadoEn())
                .build();
    }

    @Override
    public List<AsignacionRevisor> findByPostulacionId(Long id) {
        return asignRepo.findByPostulacion_Id(id).stream()
                .map(a -> AsignacionRevisor.builder()
                        .id(a.getId())
                        .revisorId(a.getRevisor().getId())
                        .postulacionId(a.getPostulacion().getId())
                        .etapaId(a.getEtapa().getId())
                        .asignadoEn(a.getAsignadoEn())
                        .build())
                .toList();
    }

    @Override
    public boolean existsByPostulacionIdAndRevisorIdAndEtapaId(Long p, Long r, Long e) {
        return asignRepo.existsByPostulacion_IdAndRevisor_IdAndEtapa_Id(p, r, e);
    }
}
