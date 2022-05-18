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

    /**
     * Restituisce il codice fiscale.
     * @return Codice fiscale.
     */
    String getCodiceFiscale();

    /**
     * Imposta il codice fiscale.
     * @param codiceFiscale Codice fiscale
     */
    void setCodiceFiscale(String codiceFiscale);

    /**
     * Confronta due persone. Una persona viene prima in ordine
     * rispetto ad un altra in base all'ordine alfabetico dei loro codici fiscali
     * @param other Persona da confrontare con this
     * @return Numero negativo se this viene prima di other, 0 se sono uguali, positivo altrimenti se other viene prima di this
     */
    int compareTo(Persona other);
}
