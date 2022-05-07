/**
 * Definisce interfaccia usata dalle classi
 * che rappresentano persone all'interno del sistema.
 */
package com.example.progettoingegneria;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

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
    List<String> validate();

    /**
     * Restituisce vero se una persona e' admin, falso altrimenti
     * @return vero se una persona e' admin, falso altrimenti
     */
    boolean isAdmin();

    /**
     * Restituisce vero se una persona e' dipendente (o admin), falso altrimenti
     * @return vero se una persona e' dipendente (o admin), falso altrimenti
     */
    boolean isDipendente();
}
