package feria;

/**
 * Contrato pequeño: todo objeto que pueda producir una ficha o reporte de una linea.
 * Los clientes (ImpresoraFichas) dependen de este contrato, no de los tipos concretos.
 */
public interface Ficha {

    String generarFicha();
}
