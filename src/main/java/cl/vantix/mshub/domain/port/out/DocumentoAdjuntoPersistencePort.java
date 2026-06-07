package cl.vantix.mshub.domain.port.out;

import cl.vantix.mshub.domain.model.DocumentoAdjunto;
import java.util.List;

public interface DocumentoAdjuntoPersistencePort {
    List<DocumentoAdjunto> findByConvocatoriaId(Long convocatoriaId);
    DocumentoAdjunto save(DocumentoAdjunto doc);
    void deleteByConvocatoriaId(Long convocatoriaId);
}
