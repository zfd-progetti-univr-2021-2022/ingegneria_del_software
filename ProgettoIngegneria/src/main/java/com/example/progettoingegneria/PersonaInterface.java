/**
 * Definisce interfaccia usata dalle classi
 * che rappresentano persone all'interno del sistema.
 */
package com.example.progettoingegneria;

import am.ik.yavi.core.ConstraintViolations;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Tnterfaccia usata dalle classi
 * che rappresentano persone all'interno del sistema.
 *
 * Tutte gli oggetti che rappresentano persone devono poter essere convertibili in formato JSON
 * e devono avere un metodo usato per validare i dati al proprio interno.
 */
public interface PersonaInterface {
    /**
     * Restituisce stringa JSON con le proprieta' dell'oggetto
     * @return Stringa contenente JSON con le proprieta' dell'oggetto
     * @throws JsonProcessingException Jackson non riesce a convertire l'oggetto in JSON
     */
    String asJSON() throws JsonProcessingException;

    /**
     * Restituisce i problemi di validazione dell'oggetto
     * @return Problemi validazione
     */
    ConstraintViolations validate();
}
