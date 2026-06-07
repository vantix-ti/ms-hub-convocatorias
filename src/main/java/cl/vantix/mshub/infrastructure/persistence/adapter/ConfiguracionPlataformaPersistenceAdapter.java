package cl.vantix.mshub.infrastructure.persistence.adapter;

import cl.vantix.mshub.domain.model.ConfiguracionPlataforma;
import cl.vantix.mshub.domain.port.out.ConfiguracionPlataformaPersistencePort;
import cl.vantix.mshub.infrastructure.persistence.entity.ConfiguracionPlataformaJpa;
import cl.vantix.mshub.infrastructure.persistence.repository.ConfiguracionPlataformaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component @RequiredArgsConstructor
public class ConfiguracionPlataformaPersistenceAdapter implements ConfiguracionPlataformaPersistencePort {

    private final ConfiguracionPlataformaJpaRepository repo;

    @Override public ConfiguracionPlataforma save(ConfiguracionPlataforma cfg) {
        return toDomain(repo.save(toJpa(cfg)));
    }

    @Override public List<ConfiguracionPlataforma> saveAll(List<ConfiguracionPlataforma> cfgs) {
        return repo.saveAll(cfgs.stream().map(this::toJpa).collect(Collectors.toList()))
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override public List<ConfiguracionPlataforma> findByInstitucionId(Long institucionId) {
        return repo.findByInstitucionId(institucionId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override public Optional<ConfiguracionPlataforma> findByInstitucionIdAndClave(Long institucionId, String clave) {
        return repo.findByInstitucionIdAndClave(institucionId, clave).map(this::toDomain);
    }

    private ConfiguracionPlataformaJpa toJpa(ConfiguracionPlataforma cfg) {
        return ConfiguracionPlataformaJpa.builder()
                .id(cfg.getId()).institucionId(cfg.getInstitucionId())
                .clave(cfg.getClave()).valor(cfg.getValor()).build();
    }

    private ConfiguracionPlataforma toDomain(ConfiguracionPlataformaJpa jpa) {
        return ConfiguracionPlataforma.builder()
                .id(jpa.getId()).institucionId(jpa.getInstitucionId())
                .clave(jpa.getClave()).valor(jpa.getValor())
                .actualizadoEn(jpa.getActualizadoEn()).build();
    }
}
