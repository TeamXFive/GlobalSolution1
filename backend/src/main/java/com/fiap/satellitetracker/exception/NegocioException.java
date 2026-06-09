package com.fiap.satellitetracker.exception;

/** Lancada para erros de regra de negocio (ex.: email duplicado, login invalido). */
public class NegocioException extends RuntimeException {
    public NegocioException(String mensagem) {
        super(mensagem);
    }
}
