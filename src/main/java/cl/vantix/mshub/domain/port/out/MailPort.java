package cl.vantix.mshub.domain.port.out;

public interface MailPort {
    void sendConfirmacionEmail(String to, String nombreCompleto, String token);
    void sendResetPasswordEmail(String to, String nombreCompleto, String token);
    void sendNotificacion(String to, String nombreCompleto, String titulo, String mensaje);
    /** Correo al usuario creado por un admin para que active su cuenta y establezca su contraseña */
    void sendBienvenidaAdminEmail(String to, String nombreCompleto, String token);
}

