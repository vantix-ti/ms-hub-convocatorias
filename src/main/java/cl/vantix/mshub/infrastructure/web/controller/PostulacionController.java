package cl.vantix.mshub.infrastructure.web.controller;

import cl.vantix.mshub.domain.port.in.*;
import cl.vantix.mshub.infrastructure.web.dto.request.*;
import cl.vantix.mshub.infrastructure.web.dto.response.*;
import cl.vantix.mshub.infrastructure.web.mapper.WebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController @RequestMapping("/postulaciones") @RequiredArgsConstructor
public class PostulacionController {
    private final PostulacionUseCase useCase;
    private final AuthUseCase authUseCase;
    private final WebMapper mapper;

    @PostMapping @PreAuthorize("hasRole('POSTULANTE')")
    public ResponseEntity<PostulacionResponse> crear(@Valid @RequestBody PostulacionRequest req,
                                                      @AuthenticationPrincipal UserDetails ud) {
        Long uid = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.toPostulacionResponse(useCase.crear(req.getConvocatoriaId(), uid)));
    }

    @GetMapping("/mis-postulaciones") @PreAuthorize("hasAnyRole('POSTULANTE','ADMIN')")
    public ResponseEntity<List<PostulacionResponse>> misPostulaciones(@AuthenticationPrincipal UserDetails ud) {
        Long uid = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        return ResponseEntity.ok(useCase.listarPorUsuario(uid).stream()
                .map(mapper::toPostulacionResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostulacionResponse> obtener(@PathVariable Long id,
                                                        @AuthenticationPrincipal UserDetails ud) {
        Long uid = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        return ResponseEntity.ok(mapper.toPostulacionResponse(useCase.obtenerPorId(id, uid)));
    }

    @PutMapping("/{id}/respuestas") @PreAuthorize("hasRole('POSTULANTE')")
    public ResponseEntity<PostulacionResponse> guardarRespuesta(@PathVariable Long id,
                                                                 @Valid @RequestBody RespuestaRequest req,
                                                                 @AuthenticationPrincipal UserDetails ud) {
        Long uid = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        return ResponseEntity.ok(mapper.toPostulacionResponse(
                useCase.guardarRespuesta(id, req.getCampoId(), req.getValorTexto(),
                                         req.getValorNumero(), req.getValorFecha(), uid)));
    }

    @PostMapping("/{id}/enviar") @PreAuthorize("hasRole('POSTULANTE')")
    public ResponseEntity<PostulacionResponse> enviar(@PathVariable Long id,
                                                       @AuthenticationPrincipal UserDetails ud) {
        Long uid = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        return ResponseEntity.ok(mapper.toPostulacionResponse(useCase.enviar(id, uid)));
    }

    @GetMapping("/convocatoria/{convocatoriaId}") @PreAuthorize("hasAnyRole('ADMIN','REVISOR')")
    public ResponseEntity<List<PostulacionResponse>> listarPorConvocatoria(@PathVariable Long convocatoriaId) {
        return ResponseEntity.ok(useCase.listarPorConvocatoria(convocatoriaId).stream()
                .map(mapper::toPostulacionResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Long id) {
        byte[] pdf = useCase.generarPdf(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=postulacion-" + id + ".pdf")
                .body(pdf);
    }
}
