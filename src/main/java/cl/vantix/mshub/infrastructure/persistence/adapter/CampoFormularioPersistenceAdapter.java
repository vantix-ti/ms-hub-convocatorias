package cl.vantix.mshub.infrastructure.persistence.adapter;

import cl.vantix.mshub.domain.model.CampoFormulario;
import cl.vantix.mshub.domain.port.out.CampoFormularioPersistencePort;
import cl.vantix.mshub.infrastructure.persistence.entity.CampoFormularioJpa;
import cl.vantix.mshub.infrastructure.persistence.entity.EtapaJpa;
import cl.vantix.mshub.infrastructure.persistence.entity.TipoCampoJpa;
import cl.vantix.mshub.infrastructure.persistence.mapper.EtapaPersistenceMapper;
import cl.vantix.mshub.infrastructure.persistence.repository.CampoFormularioJpaRepository;
import cl.vantix.mshub.infrastructure.persistence.repository.EtapaJpaRepository;
import cl.vantix.mshub.infrastructure.persistence.repository.TipoCampoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CampoFormularioPersistenceAdapter implements CampoFormularioPersistencePort {

    private final CampoFormularioJpaRepository campoRepo;
    private final TipoCampoJpaRepository tipoCampoRepo;
    private final EtapaJpaRepository etapaRepo;
    private final EtapaPersistenceMapper mapper;

    @Override
    public Optional<CampoFormulario> findById(Long id) {
        return campoRepo.findById(id).map(mapper::campoDomain);
    }

    @Override
    public void deleteById(Long id) {
        campoRepo.deleteById(id);
    }

    @Override
    public CampoFormulario save(CampoFormulario domain) {
        TipoCampoJpa tipoCampo = tipoCampoRepo.findByNombre(domain.getTipoCampo().name())
                .orElseThrow(() -> new RuntimeException("TipoCampo no encontrado: " + domain.getTipoCampo()));
        EtapaJpa etapa = etapaRepo.findById(domain.getEtapaId())
                .orElseThrow(() -> new RuntimeException("Etapa no encontrada: " + domain.getEtapaId()));
        CampoFormularioJpa jpa = domain.getId() != null
                ? campoRepo.findById(domain.getId()).orElse(CampoFormularioJpa.builder().build())
                : CampoFormularioJpa.builder().build();
        jpa.setEtapa(etapa);
        jpa.setNombre(domain.getNombre());
        jpa.setMensajeAyuda(domain.getMensajeAyuda());
        jpa.setTipoCampo(tipoCampo);
        jpa.setObligatorio(domain.isObligatorio());
        jpa.setOrden(domain.getOrden());
        jpa.setOpciones(domain.getOpciones());
        jpa.setCondicional(domain.isCondicional());
        jpa.setCampoCondicionId(domain.getCampoCondicionId());
        jpa.setMaxArchivos(domain.getMaxArchivos());
        jpa.setFormatosPermitidos(domain.getFormatosPermitidos());
        jpa.setMaxCaracteres(domain.getMaxCaracteres());
        return mapper.campoDomain(campoRepo.save(jpa));
    }
}
