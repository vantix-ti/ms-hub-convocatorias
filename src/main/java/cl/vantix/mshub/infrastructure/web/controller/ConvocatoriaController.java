package cl.vantix.mshub.infrastructure.web.controller;

import cl.vantix.mshub.domain.model.*;
import cl.vantix.mshub.domain.port.in.*;
import cl.vantix.mshub.infrastructure.web.dto.request.ConvocatoriaRequest;
import cl.vantix.mshub.infrastructure.web.dto.response.ConvocatoriaResponse;
import cl.vantix.mshub.infrastructure.web.mapper.WebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController @RequestMapping("/convocatorias") @RequiredArgsConstructor
public class ConvocatoriaController {

    private final ConvocatoriaUseCase useCase;
    private final AuthUseCase authUseCase;
    private final UsuarioUseCase usuarioUseCase;
    private final InstitucionUseCase institucionUseCase;
    private final WebMapper mapper;

    @GetMapping
    public ResponseEntity<List<ConvocatoriaResponse>> listarPublicas() {
        return ResponseEntity.ok(useCase.listarPublicas().stream()
                .map(c -> mapper.toConvocatoriaResponseWithDocs(c)).collect(Collectors.toList()));
    }

    /** ADMIN ve todas; GESTOR ve solo las de su institución */
    @GetMapping("/todas") @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<List<ConvocatoriaResponse>> listarTodas(
            @AuthenticationPrincipal UserDetails ud) {
        boolean isGestor = ud.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_GESTOR"));
        List<Convocatoria> lista;
        if (isGestor) {
            Long instId = usuarioUseCase.obtenerPorEmail(ud.getUsername()).getInstitucionId();
            lista = instId != null ? useCase.listarPorInstitucion(instId) : List.of();
        } else {
            lista = useCase.listarTodas();
        }
        return ResponseEntity.ok(lista.stream()
                .map(c -> mapper.toConvocatoriaResponseWithDocs(c)).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConvocatoriaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toConvocatoriaResponseWithDocs(useCase.obtenerPorId(id)));
    }

    @PostMapping @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<ConvocatoriaResponse> crear(
            @Valid @RequestBody ConvocatoriaRequest req,
            @AuthenticationPrincipal UserDetails ud) {
        Long uid  = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        String org = resolveOrganizacion(req, ud);
        List<DocumentoAdjunto> docs = mapDocumentos(req, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.toConvocatoriaResponseWithDocs(useCase.crear(
                        req.getTitulo(), req.getDescripcion(), org,
                        req.getFechaInicio(), req.getFechaFin(), req.getImagen(), uid, docs)));
    }

    @PutMapping("/{id}") @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<ConvocatoriaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ConvocatoriaRequest req,
            @AuthenticationPrincipal UserDetails ud) {
        Long uid  = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        String org = resolveOrganizacion(req, ud);
        List<DocumentoAdjunto> docs = mapDocumentos(req, id);
        return ResponseEntity.ok(mapper.toConvocatoriaResponseWithDocs(
                useCase.actualizar(id, req.getTitulo(), req.getDescripcion(), org,
                        req.getFechaInicio(), req.getFechaFin(), req.getImagen(), uid, docs)));
    }

    @PutMapping("/{id}/estado") @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<ConvocatoriaResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails ud) {
        Long uid = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        return ResponseEntity.ok(mapper.toConvocatoriaResponseWithDocs(
                useCase.cambiarEstado(id, EstadoConvocatoria.valueOf(body.get("estado")), uid)));
    }

    @DeleteMapping("/{id}") @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails ud) {
        Long uid = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        useCase.eliminar(id, uid);
        return ResponseEntity.noContent().build();
    }

    // ── Helpers ──────────────────────────────────────────────

    /**
     * Si el caller es GESTOR, la organización se auto-asigna desde el nombre de su institución.
     * Si el campo viene relleno del request y el caller es ADMIN, se respeta.
     */
    private String resolveOrganizacion(ConvocatoriaRequest req, UserDetails ud) {
        boolean isGestor = ud.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_GESTOR"));
        if (isGestor) {
            Long instId = usuarioUseCase.obtenerPorEmail(ud.getUsername()).getInstitucionId();
            if (instId != null) {
                try { return institucionUseCase.obtenerPorId(instId).getNombre(); }
                catch (Exception ignored) {}
            }
        }
        return req.getOrganizacion();
    }

    private List<DocumentoAdjunto> mapDocumentos(ConvocatoriaRequest req, Long convocatoriaId) {
        if (req.getDocumentos() == null) return List.of();
        return req.getDocumentos().stream().map(d -> DocumentoAdjunto.builder()
                .convocatoriaId(convocatoriaId)
                .nombre(d.getNombre()).descripcion(d.getDescripcion())
                .contenido(d.getContenido()).tipoMime(d.getTipoMime())
                .tamanio(d.getTamanio()).build())
                .collect(Collectors.toList());
    }
}
