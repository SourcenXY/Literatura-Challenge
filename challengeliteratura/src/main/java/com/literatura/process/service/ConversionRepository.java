package com.literatura.process.service;

public interface ConversionRepository {
    <T> T convertirDatos(String json , Class<T> clase);
}
