package com.example.szachy21;

public class SzachyLogika {

    private String[][] figury;

    public SzachyLogika(String[][] figury) {
        this.figury = figury;
    }

    // ================= GŁÓWNA LOGIKA RUCHU =================
    public boolean czyRuchDozwolony(int ws, int ks, int wc, int kc, boolean bialeTeraz) {

        String figura = figury[ws][ks];
        String cel = figury[wc][kc];

        if (figura.equals("")) return false;

        // tura
        if (bialeTeraz && !czyBiala(figura)) return false;
        if (!bialeTeraz && czyBiala(figura)) return false;

        // nie można bić swoich
        if (!cel.equals("") && czyTenSamKolor(figura, cel)) return false;

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

    // ================= SZACH =================
    public boolean czySzach(boolean bialeTeraz) {

        String krol = bialeTeraz ? "♔" : "♚";
        int kw = -1, kk = -1;

        for (int w = 0; w < 8; w++) {
            for (int k = 0; k < 8; k++) {
                if (figury[w][k].equals(krol)) {
                    kw = w;
                    kk = k;
                }
            }
        }

        if (kw == -1) return false;

        for (int w = 0; w < 8; w++) {
            for (int k = 0; k < 8; k++) {

                String figura = figury[w][k];
                if (figura.equals("")) continue;

                if (bialeTeraz && czyBiala(figura)) continue;
                if (!bialeTeraz && !czyBiala(figura)) continue;

                if (czyRuchBezTury(w, k, kw, kk)) {
                    return true;
                }
            }
        }

        return false;
    }

    // ================= MAT =================
    public boolean czyMat(boolean bialeTeraz) {

        // jeśli nie ma szacha → nie ma mata
        if (!czySzach(bialeTeraz)) return false;

        // sprawdź wszystkie możliwe ruchy
        for (int ws = 0; ws < 8; ws++) {
            for (int ks = 0; ks < 8; ks++) {

                String figura = figury[ws][ks];
                if (figura.equals("")) continue;

                // filtr koloru
                if (bialeTeraz && !czyBiala(figura)) continue;
                if (!bialeTeraz && czyBiala(figura)) continue;

                for (int wc = 0; wc < 8; wc++) {
                    for (int kc = 0; kc < 8; kc++) {

                        if (czyRuchDozwolony(ws, ks, wc, kc, bialeTeraz)) {
                            return false; // jest ruch → nie mat
                        }
                    }
                }
            }
        }

        return true;
    }

    // ================= PION BIAŁY =================
    private boolean pionBialy(int ws, int ks, int wc, int kc) {

        if (ks == kc &&
                wc == ws - 1 &&
                figury[wc][kc].equals("")) return true;

        if (Math.abs(kc - ks) == 1 &&
                wc == ws - 1 &&
                !figury[wc][kc].equals("") &&
                !czyBiala(figury[wc][kc])) return true;

        if (ws == 6 &&
                ks == kc &&
                wc == ws - 2 &&
                figury[ws - 1][ks].equals("") &&
                figury[wc][kc].equals("")) return true;

        return false;
    }

    // ================= PION CZARNY =================
    private boolean pionCzarny(int ws, int ks, int wc, int kc) {

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

    // ================= WIEŻA =================
    private boolean wieza(int ws, int ks, int wc, int kc) {

        if (ws == wc) {
            int s = Math.min(ks, kc);
            int e = Math.max(ks, kc);

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

    // ================= GONIEC =================
    private boolean goniec(int ws, int ks, int wc, int kc) {

        if (Math.abs(wc - ws) != Math.abs(kc - ks)) return false;

        int sw = (wc > ws) ? 1 : -1;
        int sk = (kc > ks) ? 1 : -1;

        int w = ws + sw;
        int k = ks + sk;

        while (w != wc || k != kc) {
            if (!figury[w][k].equals("")) return false;
            w += sw;
            k += sk;
        }

        return true;
    }

    // ================= HETMAN =================
    private boolean hetman(int ws, int ks, int wc, int kc) {
        return wieza(ws, ks, wc, kc) || goniec(ws, ks, wc, kc);
    }

    // ================= SKOCZEK =================
    private boolean skoczek(int ws, int ks, int wc, int kc) {
        int dw = Math.abs(wc - ws);
        int dk = Math.abs(kc - ks);
        return (dw == 2 && dk == 1) || (dw == 1 && dk == 2);
    }

    // ================= KRÓL =================
    private boolean krol(int ws, int ks, int wc, int kc) {
        return Math.abs(wc - ws) <= 1 && Math.abs(kc - ks) <= 1;
    }

    // ================= POMOCNICZE =================
    private boolean czyBiala(String f) {
        return f.equals("♙") || f.equals("♖") || f.equals("♘") ||
                f.equals("♗") || f.equals("♕") || f.equals("♔");
    }

    private boolean czyTenSamKolor(String a, String b) {
        return czyBiala(a) == czyBiala(b);
    }

    // ================= ATAK BEZ TURY =================
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