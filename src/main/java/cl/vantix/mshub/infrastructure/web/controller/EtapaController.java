package cl.vantix.mshub.infrastructure.web.controller;

import cl.vantix.mshub.domain.port.in.EtapaUseCase;
import cl.vantix.mshub.infrastructure.web.dto.request.*;
import cl.vantix.mshub.infrastructure.web.dto.response.*;
import cl.vantix.mshub.infrastructure.web.mapper.WebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController @RequiredArgsConstructor
public class EtapaController {
    private final EtapaUseCase useCase;
    private final WebMapper mapper;

    @GetMapping("/convocatorias/{id}/etapas")
    public ResponseEntity<List<EtapaResponse>> listar(@PathVariable Long id) {
        return ResponseEntity.ok(useCase.listarPorConvocatoria(id).stream()
                .map(mapper::toEtapaResponse).collect(Collectors.toList()));
    }

    @GetMapping("/etapas/{id}")
    public ResponseEntity<EtapaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toEtapaResponse(useCase.obtenerPorId(id)));
    }

    @PostMapping("/convocatorias/{id}/etapas") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EtapaResponse> crear(@PathVariable Long id,
                                                @Valid @RequestBody EtapaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toEtapaResponse(
                useCase.crear(id, req.getNombre(), req.getTipoEtapa(), req.getOrden(),
                              req.getFechaInicio(), req.getFechaFin(), req.getModoEvaluacion(),
                              req.getInstruccionesPostulante(), req.getInstruccionesRevisor())));
    }

    @PutMapping("/etapas/{id}") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EtapaResponse> actualizar(@PathVariable Long id,
                                                     @Valid @RequestBody EtapaRequest req) {
        return ResponseEntity.ok(mapper.toEtapaResponse(
                useCase.actualizar(id, req.getNombre(), req.getTipoEtapa(), req.getOrden(),
                                   req.getFechaInicio(), req.getFechaFin(), req.getModoEvaluacion(),
                                   req.getInstruccionesPostulante(), req.getInstruccionesRevisor())));
    }

    @DeleteMapping("/etapas/{id}") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        useCase.eliminar(id); return ResponseEntity.noContent().build();
    }

    @PostMapping("/etapas/{id}/campos") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CampoFormularioResponse> agregarCampo(@PathVariable Long id,
                                                                  @Valid @RequestBody CampoFormularioRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toCampoResponse(
                useCase.agregarCampo(id, req.getNombre(), req.getMensajeAyuda(), req.getTipoCampo(),
                                     req.isObligatorio(), req.getOrden(), req.getOpciones(),
                                     req.getMaxCaracteres(), req.getFormatosPermitidos(), req.getMaxArchivos())));
    }

    @PutMapping("/campos/{id}") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CampoFormularioResponse> actualizarCampo(@PathVariable Long id,
                                                                     @Valid @RequestBody CampoFormularioRequest req) {
        return ResponseEntity.ok(mapper.toCampoResponse(
                useCase.actualizarCampo(id, req.getNombre(), req.getMensajeAyuda(), req.getTipoCampo(),
                                        req.isObligatorio(), req.getOrden(), req.getOpciones(),
                                        req.getMaxCaracteres(), req.getFormatosPermitidos(), req.getMaxArchivos())));
    }

    @DeleteMapping("/campos/{id}") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarCampo(@PathVariable Long id) {
        useCase.eliminarCampo(id); return ResponseEntity.noContent().build();
    }

    @PostMapping("/etapas/{id}/criterios") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CriterioEvaluacionResponse> agregarCriterio(@PathVariable Long id,
                                                                        @Valid @RequestBody CriterioEvaluacionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toCriterioResponse(
                useCase.agregarCriterio(id, req.getNombre(), req.getDescripcion(), req.getPonderador(),
                                        req.getPuntajeMinimo(), req.getPuntajeMaximo(), req.getModoCriterio(),
                                        req.getRubrica(), req.getOrden())));
    }

    @PutMapping("/criterios/{id}") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CriterioEvaluacionResponse> actualizarCriterio(@PathVariable Long id,
                                                                           @Valid @RequestBody CriterioEvaluacionRequest req) {
        return ResponseEntity.ok(mapper.toCriterioResponse(
                useCase.actualizarCriterio(id, req.getNombre(), req.getDescripcion(), req.getPonderador(),
                                           req.getPuntajeMinimo(), req.getPuntajeMaximo(), req.getModoCriterio(),
                                           req.getRubrica(), req.getOrden())));
    }

    @DeleteMapping("/criterios/{id}") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarCriterio(@PathVariable Long id) {
        useCase.eliminarCriterio(id); return ResponseEntity.noContent().build();
    }
}
