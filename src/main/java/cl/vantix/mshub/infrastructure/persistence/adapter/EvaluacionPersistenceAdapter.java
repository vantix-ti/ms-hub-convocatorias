package cl.vantix.mshub.infrastructure.persistence.adapter;

import cl.vantix.mshub.domain.model.Evaluacion;
import cl.vantix.mshub.domain.model.RespuestaEvaluador;
import cl.vantix.mshub.domain.port.out.EvaluacionPersistencePort;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import cl.vantix.mshub.infrastructure.persistence.mapper.EvaluacionPersistenceMapper;
import cl.vantix.mshub.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EvaluacionPersistenceAdapter implements EvaluacionPersistencePort {

    private final EvaluacionJpaRepository evalRepo;
    private final EstadoEvaluacionJpaRepository estadoEvalRepo;
    private final UsuarioJpaRepository usuarioRepo;
    private final PostulacionJpaRepository postulacionRepo;
    private final EtapaJpaRepository etapaRepo;
    private final CriterioEvaluacionJpaRepository criterioRepo;
    private final EvaluacionPersistenceMapper mapper;

    @Override
    public Optional<Evaluacion> findById(Long id) {
        return evalRepo.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Evaluacion> findByRevisorId(Long id) {
        return evalRepo.findByRevisor_Id(id).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Evaluacion> findByPostulacionId(Long id) {
        return evalRepo.findByPostulacion_Id(id).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Evaluacion> findByEtapaId(Long id) {
        return evalRepo.findByEtapa_Id(id).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public boolean existsByPostulacionIdAndRevisorIdAndEtapaId(Long p, Long r, Long e) {
        return evalRepo.existsByPostulacion_IdAndRevisor_IdAndEtapa_Id(p, r, e);
    }

    @Override
    public Evaluacion save(Evaluacion domain) {
        EstadoEvaluacionJpa estado = estadoEvalRepo.findByNombre(domain.getEstado().name())
                .orElseThrow(() -> new RuntimeException("EstadoEvaluacion no encontrado"));
        UsuarioJpa revisor = usuarioRepo.findById(domain.getRevisorId())
                .orElseThrow(() -> new RuntimeException("Revisor no encontrado"));
        PostulacionJpa postulacion = postulacionRepo.findById(domain.getPostulacionId())
                .orElseThrow(() -> new RuntimeException("Postulación no encontrada"));
        EtapaJpa etapa = etapaRepo.findById(domain.getEtapaId())
                .orElseThrow(() -> new RuntimeException("Etapa no encontrada"));
        EvaluacionJpa jpa = domain.getId() != null
                ? evalRepo.findById(domain.getId()).orElse(EvaluacionJpa.builder().build())
                : EvaluacionJpa.builder().build();
        jpa.setPostulacion(postulacion);
        jpa.setRevisor(revisor);
        jpa.setEtapa(etapa);
        jpa.setEstado(estado);
        jpa.setPuntajeTotal(domain.getPuntajeTotal());
        jpa.setEnviada(domain.isEnviada());
        jpa.setEnviadaEn(domain.getEnviadaEn());
        if (domain.getRespuestasEvaluador() != null && !domain.getRespuestasEvaluador().isEmpty()) {
            EvaluacionJpa savedFirst = evalRepo.save(jpa);
            List<RespuestaEvaluadorJpa> respJpa = domain.getRespuestasEvaluador().stream().map(r -> {
                CriterioEvaluacionJpa criterio = criterioRepo.findById(r.getCriterioId())
                        .orElseThrow(() -> new RuntimeException("Criterio no encontrado"));
                return RespuestaEvaluadorJpa.builder()
                        .evaluacion(savedFirst).criterio(criterio)
                        .puntaje(r.getPuntaje()).comentario(r.getComentario()).build();
            }).collect(Collectors.toList());
            savedFirst.getRespuestasEvaluador().clear();
            savedFirst.getRespuestasEvaluador().addAll(respJpa);
            return mapper.toDomain(evalRepo.save(savedFirst));
        }
        return mapper.toDomain(evalRepo.save(jpa));
    }
}
