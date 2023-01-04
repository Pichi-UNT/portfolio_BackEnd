package argentinaprograma.cvdigital.utils;

import java.util.Random;

public class ConfirmationCodeGenerator {
    private static final int CODE_LENGTH = 6; // Longitud del código
    private static final String CHAR_SET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Conjunto de caracteres permitidos

    public static String generateCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHAR_SET.length());
            sb.append(CHAR_SET.charAt(index));
        }
        return sb.toString();
    }
}