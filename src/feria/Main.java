package feria;

import java.util.ArrayList;
import java.util.List;

/**
 * Prepara los escenarios con datos fijos y muestra resultados esperados vs obtenidos.
 * La logica vive en las clases del modelo; aqui solo se ejecuta y se compara.
 */
public class Main {

    private static int totalPruebas = 0;
    private static int coinciden = 0;

    public static void main(String[] args) {
        escenarioBase();
        datosInvalidosYLimites();
        inscripcionesYCancelaciones();
        tallerLlenoExacto();
        listaVacia();
        busquedaInexistente();
        asistenciaPosicionesYLimites();

        System.out.println();
        System.out.println("RESULTADO GLOBAL: " + coinciden + " de " + totalPruebas + " pruebas coinciden");
    }

    // ---------- Escenarios ----------

    private static void escenarioBase() {
        titulo("ESCENARIO BASE");
        GestorTalleres gestor = new GestorTalleres();
        ImpresoraFichas impresora = new ImpresoraFichas();

        verificar("Registrar T-01 presencial 'Pintura' (cap 3, 60 min)", "Aceptada",
                texto(gestor.registrarPresencial("T-01", "Pintura", 3, 60)));
        verificar("Registrar T-02 virtual 'Fotografia' (cap 2, 40 min)", "Aceptada",
                texto(gestor.registrarVirtual("T-02", "Fotografia", 2, 40)));

        verificar("Inscribir 2 en T-01", "Aceptada", texto(gestor.inscribir("T-01", 2)));
        verificar("  inscritos T-01", "2", String.valueOf(inscritosDe(gestor, "T-01")));
        verificar("Inscribir otros 2 en T-01", "Rechazada", texto(gestor.inscribir("T-01", 2)));
        verificar("  inscritos T-01", "2", String.valueOf(inscritosDe(gestor, "T-01")));
        verificar("Inscribir 1 en T-02", "Aceptada", texto(gestor.inscribir("T-02", 1)));
        verificar("  inscritos T-02", "1", String.valueOf(inscritosDe(gestor, "T-02")));
        verificar("Cancelar 1 en T-01", "Aceptada", texto(gestor.cancelar("T-01", 1)));
        verificar("  inscritos T-01", "1", String.valueOf(inscritosDe(gestor, "T-01")));

        System.out.println("  Fichas en orden de registro:");
        impresora.mostrarTodas(gestor.listarFichas());

        verificar("Cantidad de talleres", "2", String.valueOf(gestor.cantidadTalleres()));
        verificar("Total de inscritos", "2", String.valueOf(gestor.totalInscritos()));
        verificar("Inscritos por taller (promedio)", "1.0", gestor.promedioInscritos());
        verificar("Duracion T-01 (60 + 15)", "75", String.valueOf(gestor.buscar("T-01").duracionTotal()));
        verificar("Duracion T-02 (40 + 5)", "45", String.valueOf(gestor.buscar("T-02").duracionTotal()));

        AsistenciaSemanal asistencia = new AsistenciaSemanal();
        int[] datos = {12, 8, 15, 0, 10};
        int aceptadas = 0;
        for (int i = 0; i < datos.length; i++) {
            if (asistencia.registrarDia(i + 1, datos[i])) {
                aceptadas++;
            }
        }
        verificar("Registrar asistencias [12, 8, 15, 0, 10]", "5", String.valueOf(aceptadas));
        verificar("Total semanal", "45", String.valueOf(asistencia.total()));
        verificar("Promedio", "9.0", String.valueOf(asistencia.promedio()));
        verificar("Maximo", "15", String.valueOf(asistencia.maximo()));
        verificar("Primer dia del maximo", "3", String.valueOf(asistencia.primerDiaMaximo()));

        Evidencia<Integer> evTotal = asistencia.crearEvidenciaTotal();
        Evidencia<String> evConclusion = asistencia.crearEvidenciaConclusion();
        verificar("Evidencia<Integer> contiene 45", "45", String.valueOf(evTotal.getContenido()));
        System.out.println("  " + evTotal.mostrar());
        System.out.println("  " + evConclusion.mostrar());

        System.out.println("  Cliente del contrato Ficha (talleres + asistencia):");
        List<Ficha> reportes = new ArrayList<>();
        reportes.addAll(gestor.listarFichas());
        reportes.add(asistencia);
        impresora.mostrarTodas(reportes);
    }

    private static void datosInvalidosYLimites() {
        titulo("DATOS INVALIDOS Y LIMITES (objetos nuevos)");
        GestorTalleres gestor = new GestorTalleres();
        gestor.registrarPresencial("T-01", "Pintura", 3, 60);

        verificar("ID duplicado ' t-01 ' (tras normalizar)", "Rechazada",
                texto(gestor.registrarVirtual(" t-01 ", "Otro", 5, 30)));
        verificar("  cantidad de talleres sigue en 1", "1", String.valueOf(gestor.cantidadTalleres()));
        verificar("ID en blanco", "Rechazada", texto(gestor.registrarVirtual("   ", "Sin id", 5, 30)));
        verificar("ID nulo", "Rechazada", texto(gestor.registrarVirtual(null, "Sin id", 5, 30)));
        verificar("Nombre en blanco", "Rechazada", texto(gestor.registrarVirtual("T-09", "  ", 5, 30)));
        verificar("Nombre repetido con ID distinto", "Aceptada",
                texto(gestor.registrarVirtual("T-03", "Pintura", 5, 30)));
        verificar("Capacidad 0", "Rechazada", texto(gestor.registrarVirtual("C-0", "Cap0", 0, 30)));
        verificar("Capacidad 31", "Rechazada", texto(gestor.registrarVirtual("C-31", "Cap31", 31, 30)));
        verificar("Minutos 0", "Rechazada", texto(gestor.registrarVirtual("M-0", "Min0", 5, 0)));
        verificar("Minutos 181", "Rechazada", texto(gestor.registrarVirtual("M-181", "Min181", 5, 181)));
        verificar("  cantidad de talleres (T-01 y T-03)", "2", String.valueOf(gestor.cantidadTalleres()));

        GestorTalleres limites = new GestorTalleres();
        verificar("Limite minimo: capacidad 1, 1 min (presencial)", "Aceptada",
                texto(limites.registrarPresencial("B-1", "Minimo", 1, 1)));
        verificar("Limite maximo: capacidad 30, 180 min (virtual)", "Aceptada",
                texto(limites.registrarVirtual("B-2", "Maximo", 30, 180)));
        verificar("  duracion B-1 (1 + 15)", "16", String.valueOf(limites.buscar("B-1").duracionTotal()));
        verificar("  duracion B-2 (180 + 5)", "185", String.valueOf(limites.buscar("B-2").duracionTotal()));
    }

    private static void inscripcionesYCancelaciones() {
        titulo("INSCRIPCION CERO Y CANCELACION EXCESIVA (objetos nuevos)");
        GestorTalleres gestor = new GestorTalleres();
        gestor.registrarPresencial("T-01", "Pintura", 3, 60);

        verificar("Inscribir 0", "Rechazada", texto(gestor.inscribir("T-01", 0)));
        verificar("Inscribir -1", "Rechazada", texto(gestor.inscribir("T-01", -1)));
        verificar("  inscritos", "0", String.valueOf(inscritosDe(gestor, "T-01")));
        verificar("Cancelar 1 con 0 inscritos", "Rechazada", texto(gestor.cancelar("T-01", 1)));
        verificar("Inscribir 1", "Aceptada", texto(gestor.inscribir(" t-01 ", 1)));
        verificar("Cancelar 2 con 1 inscrito", "Rechazada", texto(gestor.cancelar("T-01", 2)));
        verificar("Cancelar 0", "Rechazada", texto(gestor.cancelar("T-01", 0)));
        verificar("  inscritos conserva 1", "1", String.valueOf(inscritosDe(gestor, "T-01")));
        verificar("Cancelar exactamente 1 (limite)", "Aceptada", texto(gestor.cancelar("T-01", 1)));
        verificar("  inscritos queda en 0", "0", String.valueOf(inscritosDe(gestor, "T-01")));
    }

    private static void tallerLlenoExacto() {
        titulo("TALLER LLENO EXACTO (ejecucion nueva)");
        GestorTalleres gestor = new GestorTalleres();
        gestor.registrarVirtual("T-02", "Fotografia", 2, 40);

        verificar("Inscribir 2 (llena exactamente la capacidad 2)", "Aceptada", texto(gestor.inscribir("T-02", 2)));
        verificar("Inscribir 1 adicional", "Rechazada", texto(gestor.inscribir("T-02", 1)));
        verificar("  inscritos conserva 2", "2", String.valueOf(inscritosDe(gestor, "T-02")));
        verificar("  cupos restantes", "0", String.valueOf(gestor.buscar("T-02").cuposRestantes()));
    }

    private static void listaVacia() {
        titulo("LISTA VACIA (objeto nuevo)");
        GestorTalleres gestor = new GestorTalleres();
        verificar("Cantidad de talleres", "0", String.valueOf(gestor.cantidadTalleres()));
        verificar("Total de inscritos", "0", String.valueOf(gestor.totalInscritos()));
        verificar("Promedio de inscritos", "Sin datos", gestor.promedioInscritos());
        verificar("Lider de inscritos", "Sin datos", gestor.liderInscritos());
        verificar("Fichas listadas", "0", String.valueOf(gestor.listarFichas().size()));
    }

    private static void busquedaInexistente() {
        titulo("BUSQUEDA DE ID INEXISTENTE (objeto nuevo)");
        GestorTalleres gestor = new GestorTalleres();
        gestor.registrarPresencial("T-01", "Pintura", 3, 60);

        verificar("Consultar ficha de Z-99", "No encontrado", gestor.consultarFicha("Z-99"));
        verificar("Inscribir 1 en Z-99", "Rechazada", texto(gestor.inscribir("Z-99", 1)));
        verificar("Cancelar 1 en Z-99", "Rechazada", texto(gestor.cancelar("Z-99", 1)));
        verificar("Buscar con ID nulo", "No encontrado", gestor.consultarFicha(null));
        verificar("Buscar ' t-01 ' (normaliza al buscar)", "Encontrado",
                gestor.buscar(" t-01 ") != null ? "Encontrado" : "No encontrado");
    }

    private static void asistenciaPosicionesYLimites() {
        titulo("ASISTENCIA: POSICIONES, LIMITES Y EMPATES (objetos nuevos)");
        AsistenciaSemanal a = new AsistenciaSemanal();
        a.registrarDia(1, 12);
        a.registrarDia(2, 8);
        a.registrarDia(3, 15);
        a.registrarDia(4, 0);
        a.registrarDia(5, 10);

        verificar("Consultar posicion 0", "Posicion inexistente", a.consultarDia(0));
        verificar("Consultar posicion 6", "Posicion inexistente", a.consultarDia(6));
        verificar("Consultar dia 3", "Dia 3: 15", a.consultarDia(3));
        verificar("Corregir dia 6 = 5", "Rechazada", texto(a.corregirDia(6, 5)));
        verificar("Corregir dia 0 = 5", "Rechazada", texto(a.corregirDia(0, 5)));
        verificar("Corregir dia 3 = 201", "Rechazada", texto(a.corregirDia(3, 201)));
        verificar("Corregir dia 3 = -1", "Rechazada", texto(a.corregirDia(3, -1)));
        verificar("  total no cambia", "45", String.valueOf(a.total()));
        verificar("Corregir dia 4 = 0 (cero es valido)", "Aceptada", texto(a.corregirDia(4, 0)));
        verificar("Corregir dia 4 = 200 (limite superior)", "Aceptada", texto(a.corregirDia(4, 200)));
        verificar("  nuevo total", "245", String.valueOf(a.total()));
        verificar("  nuevo maximo y dia", "200 / 4", a.maximo() + " / " + a.primerDiaMaximo());

        AsistenciaSemanal empate = new AsistenciaSemanal();
        int[] datos = {5, 200, 3, 200, 0};
        for (int i = 0; i < datos.length; i++) {
            empate.registrarDia(i + 1, datos[i]);
        }
        verificar("Empate de maximos [5, 200, 3, 200, 0]: primer dia", "2", String.valueOf(empate.primerDiaMaximo()));

        AsistenciaSemanal ceros = new AsistenciaSemanal();
        verificar("Todo en cero: total / promedio / maximo / dia", "0 / 0.0 / 0 / 1",
                ceros.total() + " / " + ceros.promedio() + " / " + ceros.maximo() + " / " + ceros.primerDiaMaximo());
    }

    // ---------- Apoyo de presentacion ----------

    private static int inscritosDe(GestorTalleres gestor, String id) {
        Taller taller = gestor.buscar(id);
        if (taller == null) {
            return -1;
        }
        return taller.getInscritos();
    }

    private static String texto(boolean aceptada) {
        if (aceptada) {
            return "Aceptada";
        }
        return "Rechazada";
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }

    private static void verificar(String prueba, String esperado, String obtenido) {
        totalPruebas++;
        boolean coincide = esperado.equals(obtenido);
        if (coincide) {
            coinciden++;
        }
        String marca = coincide ? "[OK]    " : "[FALLA] ";
        System.out.println(marca + prueba + " | esperado: " + esperado + " | obtenido: " + obtenido);
    }
}
