package cl.vantix.mshub.domain.port.in;
import cl.vantix.mshub.domain.model.Usuario;

public interface AuthUseCase {
    void register(String nombre, String apellidoPaterno, String apellidoMaterno,
                  String email, String password, String telefono);
    String login(String email, String password);
    void confirmarEmail(String token);
    void solicitarResetPassword(String email);
    void resetPassword(String token, String nuevaPassword);
    Usuario obtenerPorEmail(String email);
}
