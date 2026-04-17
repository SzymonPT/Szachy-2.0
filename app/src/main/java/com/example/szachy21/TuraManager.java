package com.example.szachy21;

public class TuraManager {

    // true = białe, false = czarne
    private boolean biale = true;

    // zmiana tury
    public void zmienTure() {
        biale = !biale;
    }

    // kto ma teraz ruch
    public boolean czyBialeTeraz() {
        return biale;
    }

    // ================= NAJWAŻNIEJSZA METODA =================
    // sprawdza czy figura jest biała
    public boolean czyBiala(String figura) {
        return figura.equals("♙") ||
                figura.equals("♖") ||
                figura.equals("♘") ||
                figura.equals("♗") ||
                figura.equals("♕") ||
                figura.equals("♔");
    }

    // (opcjonalnie) tekst tury do debugowania
    public String napisTury() {
        return biale ? "Ruch białych" : "Ruch czarnych";
    }
}