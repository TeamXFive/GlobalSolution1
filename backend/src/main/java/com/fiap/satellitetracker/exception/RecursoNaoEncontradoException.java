package com.fiap.satellitetracker.exception;

/** Lancada quando um recurso (usuario, alerta, etc.) nao e encontrado. */
public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
