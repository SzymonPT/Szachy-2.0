package com.example.szachy21;

public class SzachyLogika {

    // tablica jest PRZEKAZANA z MainActivity i NIE jest kopiowana
    // to znaczy: zmiany w MainActivity = zmiany tutaj
    private String[][] figury;

    public SzachyLogika(String[][] figury) {
        this.figury = figury;
    }

    // ta metoda jest wywoływana zawsze gdy próbujesz wykonać ruch
    // dostaje 4 współrzędne i informację czy ruch jest teraz dla białych
    public boolean czyRuchDozwolony(int ws, int ks, int wc, int kc, boolean bialeTeraz) {

        // pobieramy wartość z tablicy (referencja do String)
        String figura = figury[ws][ks];

        // sprawdzamy pole docelowe (czy coś tam stoi)
        String cel = figury[wc][kc];

        // jeśli pole startowe jest puste, od razu kończymy
        if (figura.equals("")) return false;

        // sprawdzamy czy figura należy do aktualnego gracza
        // jeśli warunek nie przejdzie, ruch jest natychmiast odrzucony
        if (bialeTeraz && !czyBiala(figura)) return false;
        if (!bialeTeraz && czyBiala(figura)) return false;

        // sprawdzamy czy na polu docelowym stoi figura tego samego koloru
        // jeśli tak → nie można nadpisać wartości w tablicy
        if (!cel.equals("") && czyTenSamKolor(figura, cel)) return false;

        // switch działa jak „rozgałęzienie sterowania”
        // Java sprawdza STRING i wybiera odpowiednią metodę
        switch (figura) {

            case "♟": return pionCzarny(ws, ks, wc, kc);
            case "♙": return pionBialy(ws, ks, wc, kc);

            case "♜":
            case "♖": return wieza(ws, ks, wc, kc);

            case "♝":
            case "♗": return goniec(ws, ks, wc, kc);

            case "♛":
            case "♕": return hetman(ws, ks, wc, kc);

            case "♞":
            case "♘": return skoczek(ws, ks, wc, kc);

            case "♚":
            case "♔": return krol(ws, ks, wc, kc);
        }

        // jeśli znak nie pasuje do żadnej figury → ruch odrzucony
        return false;
    }

    // ta funkcja NIE sprawdza ruchu — tylko analizuje stan planszy
    // szuka króla i sprawdza czy ktoś ma do niego dostęp
    public boolean czySzach(boolean bialeTeraz) {

        // wybieramy symbol króla zależnie od koloru
        String krol = bialeTeraz ? "♔" : "♚";

        int kw = -1;
        int kk = -1;

        // pełne przejście przez tablicę 8x8
        // szukamy pozycji króla
        for (int w = 0; w < 8; w++) {
            for (int k = 0; k < 8; k++) {
                if (figury[w][k].equals(krol)) {
                    kw = w;
                    kk = k;
                }
            }
        }

        // jeśli króla nie ma → coś jest nie tak w stanie gry
        if (kw == -1) return false;

        // teraz sprawdzamy KAŻDĄ figurę przeciwnika
        for (int w = 0; w < 8; w++) {
            for (int k = 0; k < 8; k++) {

                String figura = figury[w][k];

                // puste pole → pomijamy
                if (figura.equals("")) continue;

                // pomijamy własne figury
                if (bialeTeraz && czyBiala(figura)) continue;
                if (!bialeTeraz && !czyBiala(figura)) continue;

                // sprawdzamy czy ta figura może „trafić” króla
                if (czyRuchBezTury(w, k, kw, kk)) {
                    return true;
                }
            }
        }

        // jeśli żadna figura nie atakuje króla → brak szacha
        return false;
    }

    // ta metoda wykonuje bardzo kosztowną analizę:
    // sprawdza czy ISTNIEJE JAKIKOLWIEK legalny ruch
    public boolean czyMat(boolean bialeTeraz) {

        // jeśli nie ma szacha → nie ma sensu sprawdzać dalej
        if (!czySzach(bialeTeraz)) return false;

        // brute-force: sprawdzamy każdą figurę
        for (int ws = 0; ws < 8; ws++) {
            for (int ks = 0; ks < 8; ks++) {

                String figura = figury[ws][ks];
                if (figura.equals("")) continue;

                // filtrujemy kolor
                if (bialeTeraz && !czyBiala(figura)) continue;
                if (!bialeTeraz && czyBiala(figura)) continue;

                // próbujemy każdy możliwy cel
                for (int wc = 0; wc < 8; wc++) {
                    for (int kc = 0; kc < 8; kc++) {

                        // jeśli znajdzie choć jeden ruch → natychmiast kończy
                        if (czyRuchDozwolony(ws, ks, wc, kc, bialeTeraz)) {
                            return false;
                        }
                    }
                }
            }
        }

        // jeśli NIC nie działa → mat
        return true;
    }

    // poniżej logika pionków:
    // porównujemy współrzędne i stan pól na tablicy

    private boolean pionBialy(int ws, int ks, int wc, int kc) {

        // sprawdzamy ruch w górę tablicy (ws-1)
        if (ks == kc &&
                wc == ws - 1 &&
                figury[wc][kc].equals("")) return true;

        // bicie działa tylko po skosie i tylko jeśli coś stoi
        if (Math.abs(kc - ks) == 1 &&
                wc == ws - 1 &&
                !figury[wc][kc].equals("") &&
                !czyBiala(figury[wc][kc])) return true;

        // start pionka (sprawdzamy dokładnie stan pola pośredniego)
        if (ws == 6 &&
                ks == kc &&
                wc == ws - 2 &&
                figury[ws - 1][ks].equals("") &&
                figury[wc][kc].equals("")) return true;

        return false;
    }

    private boolean pionCzarny(int ws, int ks, int wc, int kc) {

        // analogicznie, ale kierunek odwrotny (ws+1)
        if (ks == kc &&
                wc == ws + 1 &&
                figury[wc][kc].equals("")) return true;

        if (Math.abs(kc - ks) == 1 &&
                wc == ws + 1 &&
                !figury[wc][kc].equals("") &&
                czyBiala(figury[wc][kc])) return true;

        if (ws == 1 &&
                ks == kc &&
                wc == ws + 2 &&
                figury[ws + 1][ks].equals("") &&
                figury[wc][kc].equals("")) return true;

        return false;
    }

    private boolean wieza(int ws, int ks, int wc, int kc) {

        // sprawdzamy tylko jedną oś na raz (X albo Y)
        if (ws == wc) {

            int s = Math.min(ks, kc);
            int e = Math.max(ks, kc);

            // iterujemy po polach pomiędzy startem a końcem
            for (int i = s + 1; i < e; i++)
                if (!figury[ws][i].equals("")) return false;

            return true;
        }

        if (ks == kc) {

            int s = Math.min(ws, wc);
            int e = Math.max(ws, wc);

            for (int i = s + 1; i < e; i++)
                if (!figury[i][ks].equals("")) return false;

            return true;
        }

        return false;
    }

    private boolean goniec(int ws, int ks, int wc, int kc) {

        // sprawdzamy czy różnica osi jest identyczna (przekątna)
        if (Math.abs(wc - ws) != Math.abs(kc - ks)) return false;

        int sw = (wc > ws) ? 1 : -1;
        int sk = (kc > ks) ? 1 : -1;

        int w = ws + sw;
        int k = ks + sk;

        // idziemy krok po kroku po przekątnej
        while (w != wc || k != kc) {
            if (!figury[w][k].equals("")) return false;
            w += sw;
            k += sk;
        }

        return true;
    }

    private boolean hetman(int ws, int ks, int wc, int kc) {
        // hetman to połączenie wieży i gońca
        return wieza(ws, ks, wc, kc) || goniec(ws, ks, wc, kc);
    }

    private boolean skoczek(int ws, int ks, int wc, int kc) {
        // L-kształt → nie sprawdzamy przeszkód
        int dw = Math.abs(wc - ws);
        int dk = Math.abs(kc - ks);
        return (dw == 2 && dk == 1) || (dw == 1 && dk == 2);
    }

    private boolean krol(int ws, int ks, int wc, int kc) {
        // jeden krok w dowolnym kierunku
        return Math.abs(wc - ws) <= 1 && Math.abs(kc - ks) <= 1;
    }

    // sprawdza kolor na podstawie zestawu symboli
    private boolean czyBiala(String f) {
        return f.equals("♙") || f.equals("♖") || f.equals("♘") ||
                f.equals("♗") || f.equals("♕") || f.equals("♔");
    }

    // porównanie kolorów dwóch figur
    private boolean czyTenSamKolor(String a, String b) {
        return czyBiala(a) == czyBiala(b);
    }

    // ta metoda omija kontrolę tury i służy tylko do sprawdzania ataku
    private boolean czyRuchBezTury(int ws, int ks, int wc, int kc) {

        String figura = figury[ws][ks];

        switch (figura) {

            case "♟": return pionCzarny(ws, ks, wc, kc);
            case "♙": return pionBialy(ws, ks, wc, kc);

            case "♜":
            case "♖": return wieza(ws, ks, wc, kc);

            case "♝":
            case "♗": return goniec(ws, ks, wc, kc);

            case "♛":
            case "♕": return hetman(ws, ks, wc, kc);

            case "♞":
            case "♘": return skoczek(ws, ks, wc, kc);

            case "♚":
            case "♔": return krol(ws, ks, wc, kc);
        }

        return false;
    }
}