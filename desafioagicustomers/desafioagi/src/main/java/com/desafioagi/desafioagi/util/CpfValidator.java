package com.desafioagi.desafioagi.util;
import java.util.regex.Pattern;

public class CpfValidator {

    private static final String CPF_REGEX = "^[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}\\-[0-9]{2}$";
    private static final Pattern CPF_PATTERN = Pattern.compile(CPF_REGEX);

    public static boolean isValidCPF(String cpf) {
        if (cpf == null || !CPF_PATTERN.matcher(cpf).matches()) {
            return false;
        }

        cpf = cpf.replace(".", "").replace("-", "");

        return cpf.matches("(\\d{9})(\\d{2})") &&
                cpf.substring(9).equals(calculateCpfCheckDigits(cpf.substring(0, 9)));

    }

    private static String calculateCpfCheckDigits(String cpf) {
        int sum1 = 0, sum2 = 0;

        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(cpf.charAt(i));
            sum1 += digit * (10 - i);
            sum2 += digit * (11 - i);
        }

        int firstCheckDigit = sum1 % 11 < 2 ? 0 : 11 - (sum1 % 11);
        sum2 += firstCheckDigit * 2;
        int secondCheckDigit = sum2 % 11 < 2 ? 0 : 11 - (sum2 % 11);

        return String.valueOf(firstCheckDigit) + secondCheckDigit;
    }
}
