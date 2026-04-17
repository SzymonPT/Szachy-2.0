package com.example.szachy21;

public class TuraManager {

    // ta zmienna jest JEDYNYM źródłem informacji o turze
    // true = białe, false = czarne
    // cały system gry opiera się tylko na tym jednym booleanie
    private boolean biale = true;

    // ================= ZMIANA STANU =================
    // ta metoda NIE sprawdza niczego
    // tylko przełącza stan true/false
    public void zmienTure() {

        // operator ! odwraca wartość logiczną
        // true → false
        // false → true
        biale = !biale;
    }

    // ================= ODCZYT STANU =================
    // UI i logika gry pytają tę metodę:
    // „kto teraz ma ruch?”
    public boolean czyBialeTeraz() {

        // zwraca aktualny stan bez modyfikacji
        return biale;
    }

    // ================= IDENTYFIKACJA FIGUR =================
    // ta metoda NIE sprawdza planszy
    // tylko analizuje pojedynczy String (symbol figury)
    public boolean czyBiala(String figura) {

        // porównujemy symbol tekstowy (Unicode chess pieces)
        // jeśli pasuje do zestawu → figura biała
        return figura.equals("♙") ||
                figura.equals("♖") ||
                figura.equals("♘") ||
                figura.equals("♗") ||
                figura.equals("♕") ||
                figura.equals("♔");
    }

    // ================= DEBUG / INFO =================
    // ta metoda nie wpływa na grę
    // tylko zwraca tekst do Toast/logów
    public String napisTury() {

        // operator ternary:
        // jeśli biale == true → "Ruch białych"
        // jeśli false → "Ruch czarnych"
        return biale ? "Ruch białych" : "Ruch czarnych";
    }
}