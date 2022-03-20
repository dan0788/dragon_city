package abstracts;

import interfaces.int_validations;

public abstract class abs_validations implements int_validations {
    private boolean bool=true;
    @Override
    public String letraCapitalCadaPalabra(String phrase) {
        char[] charArray = phrase.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (i == 0 || (!Character.isLetter(charArray[i - 1]) && Character.isLetter(charArray[i]))) {
                String txt = String.valueOf(charArray[i]).toUpperCase();
                charArray[i] = txt.charAt(0);
            }
        }
        return String.valueOf(charArray);
    }

    @Override
    public String letraCapitalFrase(String phrase) {
        char[] charArray = phrase.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (Character.isLetter(charArray[i])) {
                String txt = String.valueOf(charArray[i]).toUpperCase();
                charArray[i] = txt.charAt(0);
                break;
            }
        }
        return String.valueOf(charArray);
    }

    @Override
    public boolean textoVacio(String phrase) {
        char[] charArray = phrase.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if(!Character.isWhitespace(charArray[i])){
                bool= false;//false significa q no está vacío,está lleno
                return bool;
            }
            else if(Character.isWhitespace(charArray[i])){
                bool=true;
            }
        }
        return bool;
    }
    @Override
    public String eliminarUltimosCaracteres(String phrase, String caracteres_a_eliminar){
        if(phrase.endsWith(caracteres_a_eliminar)){
            int countCaracter =caracteres_a_eliminar.length();
            int countPhrase=phrase.length();
            phrase=phrase.substring(0, countPhrase-countCaracter);
        }
        return phrase;
    }
}
