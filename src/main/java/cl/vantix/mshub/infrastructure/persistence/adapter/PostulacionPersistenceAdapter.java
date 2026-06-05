package cl.vantix.mshub.infrastructure.persistence.adapter;

import cl.vantix.mshub.domain.model.*;
import cl.vantix.mshub.domain.port.out.*;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import cl.vantix.mshub.infrastructure.persistence.mapper.PostulacionPersistenceMapper;
import cl.vantix.mshub.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component @RequiredArgsConstructor
public class PostulacionPersistenceAdapter implements PostulacionPersistencePort,
        RespuestaFormularioPersistencePort {

    private final PostulacionJpaRepository postulacionRepo;
    private final RespuestaFormularioJpaRepository respuestaRepo;
    private final EstadoPostulacionJpaRepository estadoRepo;
    private final UsuarioJpaRepository usuarioRepo;
    private final ConvocatoriaJpaRepository convRepo;
    private final CampoFormularioJpaRepository campoRepo;
    private final PostulacionPersistenceMapper mapper;

    @Override public Optional<Postulacion> findById(Long id) {
        return postulacionRepo.findById(id).map(mapper::toDomain);
    }
    @Override public List<Postulacion> findByPostulanteId(Long id) {
        return postulacionRepo.findByPostulante_Id(id).stream().map(mapper::toDomain).collect(Collectors.toList());
    }
    @Override public List<Postulacion> findByConvocatoriaId(Long id) {
        return postulacionRepo.findByConvocatoria_Id(id).stream().map(mapper::toDomain).collect(Collectors.toList());
    }
    @Override public long countByConvocatoriaId(Long id) {
        return postulacionRepo.countByConvocatoria_Id(id);
    }
    @Override public Postulacion save(Postulacion domain) {
        EstadoPostulacionJpa estado = estadoRepo.findByNombre(domain.getEstado().name())
                .orElseThrow(() -> new RuntimeException("EstadoPostulacion no encontrado: " + domain.getEstado()));
        UsuarioJpa postulante = usuarioRepo.findById(domain.getPostulanteId())
                .orElseThrow(() -> new RuntimeException("Postulante no encontrado"));
        ConvocatoriaJpa conv = convRepo.findById(domain.getConvocatoriaId())
                .orElseThrow(() -> new RuntimeException("Convocatoria no encontrada"));
        PostulacionJpa jpa = domain.getId() != null
                ? postulacionRepo.findById(domain.getId()).orElse(PostulacionJpa.builder().build())
                : PostulacionJpa.builder().build();
        jpa.setPostulante(postulante); jpa.setConvocatoria(conv);
        jpa.setEstado(estado); jpa.setPorcentajeAvance(domain.getPorcentajeAvance());
        jpa.setEnviadaEn(domain.getEnviadaEn());
        return mapper.toDomain(postulacionRepo.save(jpa));
    }
    @Override public Optional<RespuestaFormulario> findByPostulacionIdAndCampoId(Long postulacionId, Long campoId) {
        return respuestaRepo.findByPostulacion_IdAndCampo_Id(postulacionId, campoId)
                .map(r -> RespuestaFormulario.builder().id(r.getId())
                        .postulacionId(r.getPostulacion().getId())
                        .campoId(r.getCampo().getId())
                        .valorTexto(r.getValorTexto()).valorNumero(r.getValorNumero())
                        .valorFecha(r.getValorFecha()).build());
    }
    @Override public List<RespuestaFormulario> findByPostulacionId(Long postulacionId) {
        return respuestaRepo.findByPostulacion_Id(postulacionId).stream()
                .map(r -> RespuestaFormulario.builder().id(r.getId())
                        .postulacionId(r.getPostulacion().getId())
                        .campoId(r.getCampo().getId())
                        .valorTexto(r.getValorTexto()).valorNumero(r.getValorNumero())
                        .valorFecha(r.getValorFecha()).build())
                .collect(Collectors.toList());
    }
    @Override public RespuestaFormulario save(RespuestaFormulario domain) {
        PostulacionJpa postulacion = postulacionRepo.findById(domain.getPostulacionId())
                .orElseThrow(() -> new RuntimeException("Postulación no encontrada"));
        CampoFormularioJpa campo = campoRepo.findById(domain.getCampoId())
                .orElseThrow(() -> new RuntimeException("Campo no encontrado"));
        RespuestaFormularioJpa jpa = domain.getId() != null
                ? respuestaRepo.findById(domain.getId()).orElse(RespuestaFormularioJpa.builder().build())
                : RespuestaFormularioJpa.builder().build();
        jpa.setPostulacion(postulacion); jpa.setCampo(campo);
        jpa.setValorTexto(domain.getValorTexto()); jpa.setValorNumero(domain.getValorNumero());
        jpa.setValorFecha(domain.getValorFecha());
        RespuestaFormularioJpa saved = respuestaRepo.save(jpa);
        return RespuestaFormulario.builder().id(saved.getId())
                .postulacionId(saved.getPostulacion().getId())
                .campoId(saved.getCampo().getId())
                .valorTexto(saved.getValorTexto()).valorNumero(saved.getValorNumero())
                .valorFecha(saved.getValorFecha()).build();
    }
}
