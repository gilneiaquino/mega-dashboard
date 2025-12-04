package br.com.megadashboard;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GerarHashSenha {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String raw = "admin123"; // senha que você quer usar
        String hash = encoder.encode(raw);

        System.out.println("Senha em texto  : " + raw);
        System.out.println("Hash gerado     : " + hash);
    }
}