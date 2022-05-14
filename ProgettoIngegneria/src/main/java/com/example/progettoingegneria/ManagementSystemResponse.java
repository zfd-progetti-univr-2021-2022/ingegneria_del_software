package com.example.progettoingegneria;

import java.util.Collection;

public class ManagementSystemResponse {

    /** Stato della richiesta */
    private final ManagementSystemStatus status;
    /** Dettagli sullo stato */
    private final Collection<String> details;

    /**
     * Crea la risposta ad una richiesta effettuata da un utente del Management System
     * @param status Stato della richiesta
     * @param details Dettagli sullo stato
     */
    public ManagementSystemResponse(ManagementSystemStatus status, Collection<String> details) {
        this.status = status;
        this.details = details;
    }

    /**
     * Restituisci stato del management system
     * @return Stato del management system
     */
    public ManagementSystemStatus getStatus() {
        return status;
    }

    /**
     * Restituisci lista di messaggi di errore relativi all'ultima richiesta
     * @return Lista di messaggi di errore
     */
    public Collection<String> getDetails() {
        return details;
    }

    /**
     * Restituisce lo stato della risposta alla richiesta seguito dalla lista dei dettagli
     * @return Stato della risposta con lista dei dettagli
     */
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("[" + this.status + "] ");

        for (int i = 0; i < details.size(); i++) {
            res.append(details.toArray()[i]);

            if (i < details.size()-1)
                res.append(", ");
        }
        return res.toString();
    }
}
