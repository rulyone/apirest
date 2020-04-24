/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.people.apirest.validation;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 
 * @author rulyone
 */
public class RutValidator implements ConstraintValidator<Rut, String> {

    @Override
    public boolean isValid(String rut, ConstraintValidatorContext cvc) {
        return isValidRut(rut);
    }
    
    //from https://es.stackoverflow.com/questions/118104/validacion-de-rut-en-java
    private boolean isValidRut(String rut) {
        boolean ret = false;
        if (rut != null && rut.trim().length() > 0) {
            try {
                rut = rut.replaceAll("[.]", "").replaceAll("-", "").trim().toUpperCase();
                char dv = rut.charAt(rut.length() - 1);
                String mantisa = rut.substring(0, rut.length() - 1);
                if (isInteger(mantisa)) {
                    int mantisaInt = Integer.parseInt(mantisa);
                    ret = validarRut(mantisaInt, dv);
                }
            } catch (Throwable e) {
                Logger.getLogger(RutValidator.class.getName()).log(Level.FINE, "isValidRut[" + rut + "]", e);
            }
        }
        return ret;
    }

    private boolean validarRut(int rut, char dv) {
        int m = 0, s = 1;
        for (; rut != 0; rut /= 10) {
            s = (s + rut % 10 * (9 - m++ % 6)) % 11;
        }
        return Character.toUpperCase(dv) == (char) (s != 0 ? s + 47 : 75);
    }

    public boolean isInteger(String cad) {
        for (int i = 0; i < cad.length(); i++) {
            if (!Character.isDigit(cad.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
}
