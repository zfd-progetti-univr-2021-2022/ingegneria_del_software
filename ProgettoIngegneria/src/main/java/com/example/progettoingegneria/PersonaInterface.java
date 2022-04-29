package com.example.progettoingegneria;

import am.ik.yavi.core.ConstraintViolations;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PersonaInterface {
    String asJSON() throws JsonProcessingException;
    ConstraintViolations validate();
}
