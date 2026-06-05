package cl.vantix.mshub.application.usecase;

import cl.vantix.mshub.domain.exception.BusinessException;
import cl.vantix.mshub.domain.exception.ResourceNotFoundException;
import cl.vantix.mshub.domain.exception.UnauthorizedException;
import cl.vantix.mshub.domain.model.*;
import cl.vantix.mshub.domain.port.in.PostulacionUseCase;
import cl.vantix.mshub.domain.port.out.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostulacionUseCaseImpl implements PostulacionUseCase {

    private final PostulacionPersistencePort postulacionPort;
    private final ConvocatoriaPersistencePort convocatoriaPort;
    private final RespuestaFormularioPersistencePort respuestaPort;

    @Override @Transactional
    public Postulacion crear(Long convocatoriaId, Long postulanteId) {
        Convocatoria conv = convocatoriaPort.findById(convocatoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria " + convocatoriaId + " no encontrada."));
        if (conv.getEstado() != EstadoConvocatoria.PUBLICADA)
            throw new BusinessException("La convocatoria no está disponible para postulaciones.");
        long yaPostuló = postulacionPort.countByConvocatoriaId(convocatoriaId);
        return postulacionPort.save(Postulacion.builder()
                .postulanteId(postulanteId).convocatoriaId(convocatoriaId)
                .estado(EstadoPostulacion.EN_CREACION).porcentajeAvance(0).build());
    }

    @Override
    public Postulacion obtenerPorId(Long id, Long usuarioId) {
        return postulacionPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Postulación " + id + " no encontrada."));
    }

    @Override
    public List<Postulacion> listarPorUsuario(Long usuarioId) {
        return postulacionPort.findByPostulanteId(usuarioId);
    }

    @Override
    public List<Postulacion> listarPorConvocatoria(Long convocatoriaId) {
        return postulacionPort.findByConvocatoriaId(convocatoriaId);
    }

    @Override @Transactional
    public Postulacion guardarRespuesta(Long postulacionId, Long campoId,
                                        String valorTexto, Double valorNumero,
                                        LocalDate valorFecha, Long usuarioId) {
        Postulacion p = postulacionPort.findById(postulacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Postulación no encontrada."));
        if (p.getEstado() != EstadoPostulacion.EN_CREACION)
            throw new BusinessException("La postulación ya fue enviada y no puede modificarse.");
        if (!p.getPostulanteId().equals(usuarioId))
            throw new UnauthorizedException("No tienes permiso para modificar esta postulación.");
        RespuestaFormulario respuesta = respuestaPort
                .findByPostulacionIdAndCampoId(postulacionId, campoId)
                .orElse(RespuestaFormulario.builder()
                        .postulacionId(postulacionId).campoId(campoId).build());
        respuesta.setValorTexto(valorTexto);
        respuesta.setValorNumero(valorNumero);
        respuesta.setValorFecha(valorFecha);
        respuestaPort.save(respuesta);
        return postulacionPort.findById(postulacionId).orElseThrow();
    }

    @Override @Transactional
    public Postulacion enviar(Long postulacionId, Long usuarioId) {
        Postulacion p = postulacionPort.findById(postulacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Postulación no encontrada."));
        if (p.getEstado() != EstadoPostulacion.EN_CREACION)
            throw new BusinessException("La postulación ya fue enviada.");
        p.setEstado(EstadoPostulacion.ENVIADA);
        p.setEnviadaEn(LocalDateTime.now());
        p.setPorcentajeAvance(100);
        return postulacionPort.save(p);
    }

    @Override
    public byte[] generarPdf(Long postulacionId) {
        // PDF generation stub — integrar iText7 según necesidad
        return new byte[0];
    }

    
}
