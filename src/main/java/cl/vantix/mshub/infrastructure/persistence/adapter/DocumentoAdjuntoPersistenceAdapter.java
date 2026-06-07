package cl.vantix.mshub.infrastructure.persistence.adapter;

import cl.vantix.mshub.domain.model.DocumentoAdjunto;
import cl.vantix.mshub.domain.port.out.DocumentoAdjuntoPersistencePort;
import cl.vantix.mshub.infrastructure.persistence.entity.DocumentoAdjuntoJpa;
import cl.vantix.mshub.infrastructure.persistence.repository.DocumentoAdjuntoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component @RequiredArgsConstructor
public class DocumentoAdjuntoPersistenceAdapter implements DocumentoAdjuntoPersistencePort {

    private final DocumentoAdjuntoJpaRepository repo;

    @Override
    public List<DocumentoAdjunto> findByConvocatoriaId(Long id) {
        return repo.findByConvocatoriaId(id).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public DocumentoAdjunto save(DocumentoAdjunto doc) {
        return toDomain(repo.save(toJpa(doc)));
    }

    @Override
    public void deleteByConvocatoriaId(Long id) {
        repo.deleteByConvocatoriaId(id);
    }

    private DocumentoAdjunto toDomain(DocumentoAdjuntoJpa jpa) {
        return DocumentoAdjunto.builder()
                .id(jpa.getId()).convocatoriaId(jpa.getConvocatoriaId())
                .nombre(jpa.getNombre()).descripcion(jpa.getDescripcion())
                .contenido(jpa.getContenido()).tipoMime(jpa.getTipoMime())
                .tamanio(jpa.getTamanio()).creadoEn(jpa.getCreadoEn()).build();
    }

    private DocumentoAdjuntoJpa toJpa(DocumentoAdjunto doc) {
        return DocumentoAdjuntoJpa.builder()
                .id(doc.getId()).convocatoriaId(doc.getConvocatoriaId())
                .nombre(doc.getNombre()).descripcion(doc.getDescripcion())
                .contenido(doc.getContenido()).tipoMime(doc.getTipoMime())
                .tamanio(doc.getTamanio()).build();
    }
}
