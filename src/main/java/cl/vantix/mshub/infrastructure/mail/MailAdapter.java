package cl.vantix.mshub.infrastructure.mail;

import cl.vantix.mshub.domain.port.out.MailPort;
import jakarta.mail.internet.MimeMessage;           // ← agregar esta línea
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Component;


@Component @RequiredArgsConstructor @Slf4j
public class MailAdapter implements MailPort {

    private final JavaMailSender mailSender;
    @Value("${app.url}") private String appUrl;
    @Value("${spring.mail.username}") private String from;

    @Override
    public void sendConfirmacionEmail(String to, String nombre, String token) {
        String link = appUrl + "/confirmar-email?token=" + token;
        send(to, "Confirma tu cuenta — Hub Convocatorias",
             "<h2>Hola, " + nombre + "</h2><p>Confirma tu cuenta: <a href='" + link + "'>Confirmar</a></p>");
    }

    @Override
    public void sendResetPasswordEmail(String to, String nombre, String token) {
        String link = appUrl + "/reset-password?token=" + token;
        send(to, "Restablecer contraseña — Hub Convocatorias",
             "<h2>Hola, " + nombre + "</h2><p>Restablecer: <a href='" + link + "'>Clic aquí</a></p>");
    }

    @Override
    public void sendNotificacion(String to, String nombre, String titulo, String mensaje) {
        send(to, titulo, "<h2>" + titulo + "</h2><p>" + mensaje + "</p>");
    }

    @Override
    public void sendBienvenidaAdminEmail(String to, String nombre, String token) {
        String link = appUrl + "/reset-password?token=" + token;
        String html =
                "<div style='font-family:sans-serif;max-width:520px;margin:0 auto;'>" +
                        "<h2 style='color:#337BD9;'>Bienvenido/a a Hub Convocatorias</h2>" +
                        "<p>Hola, <strong>" + nombre + "</strong>.</p>" +
                        "<p>Un administrador ha creado una cuenta para ti. " +
                        "Para activarla debes establecer tu contraseña haciendo clic en el siguiente enlace:</p>" +
                        "<p style='margin:24px 0;'>" +
                        "<a href='" + link + "' style='background:#337BD9;color:#fff;padding:12px 24px;" +
                        "text-decoration:none;border-radius:6px;font-weight:bold;'>Activar mi cuenta</a></p>" +
                        "<p style='color:#888;font-size:12px;'>Este enlace expira en 72 horas.<br>" +
                        "Si no esperabas este correo, ignóralo.</p>" +
                        "</div>";
        send(to, "Activa tu cuenta — Hub Convocatorias", html);
    }


    private void send(String to, String subject, String html) {
        try {
            String sender = (from != null && !from.isBlank()) ? from : "noreply@hub-convocatorias.cl";
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(msg, true, "UTF-8");
            h.setFrom(sender); h.setTo(to); h.setSubject(subject); h.setText(html, true);
            mailSender.send(msg);
        } catch (Exception e) { log.warn("Error al enviar email a {}: {}", to, e.getMessage()); }
    }
}
