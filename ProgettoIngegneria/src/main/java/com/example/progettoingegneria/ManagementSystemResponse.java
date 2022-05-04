package com.example.progettoingegneria;

import java.util.Collection;

public class ManagementSystemResponse {

    private ManagementSystemStatus status;
    private Collection<String> details;

    public ManagementSystemResponse(ManagementSystemStatus status, Collection<String> details) {
        this.status = status;
        this.details = details;
    }

    @Override
    public String toString() {
        String res = "[" + this.status + "]";

        for (int i = 0; i < details.size(); i++) {
            res += details.toArray()[i];

            if (i < details.size()-1)
                res += ", ";
        }
        return res;
    }
}
