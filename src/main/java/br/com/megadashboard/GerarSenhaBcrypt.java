package br.com.megadashboard;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class GerarSenhaBcrypt {
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin123"; // coloque a senha que quer usar
        String hash = encoder.encode(rawPassword);
        System.out.println("Hash BCrypt: " + hash);
    }
}
