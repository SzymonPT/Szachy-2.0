package com.example.szachy21;

public class SzachyLogika {

    private String[][] figury;

    public SzachyLogika(String[][] figury) {
        this.figury = figury;
    }

    public boolean czyRuchDozwolony(int ws, int ks, int wc, int kc) {

        String figura = figury[ws][ks];
        String cel = figury[wc][kc];

        if (figura.equals("")) return false;

        // blokada bicia własnych
        if (!cel.equals("") &&
                Character.isUpperCase(figura.charAt(0)) == Character.isUpperCase(cel.charAt(0))) {
            return false;
        }

        switch (figura) {

            case "♟":
                return pionCzarny(ws, ks, wc, kc);

            case "♙":
                return pionBialy(ws, ks, wc, kc);

            case "♜":
            case "♖":
                return wieza(ws, ks, wc, kc);

            case "♝":
            case "♗":
                return goniec(ws, ks, wc, kc);

            case "♛":
            case "♕":
                return hetman(ws, ks, wc, kc);

            case "♞":
            case "♘":
                return skoczek(ws, ks, wc, kc);

            case "♚":
            case "♔":
                return krol(ws, ks, wc, kc);

            default:
                return false;
        }
    }

    // ================= PION BIAŁY =================
    private boolean pionBialy(int ws, int ks, int wc, int kc) {

        if (ks == kc &&
                wc == ws - 1 &&
                figury[wc][kc].equals("")) return true;

        if (ks == kc &&
                ws == 6 &&
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

        if (ks == kc &&
                ws == 1 &&
                wc == ws + 2 &&
                figury[ws + 1][ks].equals("") &&
                figury[wc][kc].equals("")) return true;

        return false;
    }

    // ================= WIEŻA =================
    private boolean wieza(int ws, int ks, int wc, int kc) {

        if (ws == wc) {
            int start = Math.min(ks, kc);
            int end = Math.max(ks, kc);

            for (int i = start + 1; i < end; i++)
                if (!figury[ws][i].equals("")) return false;

            return true;
        }

        if (ks == kc) {
            int start = Math.min(ws, wc);
            int end = Math.max(ws, wc);

            for (int i = start + 1; i < end; i++)
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

        while (w != wc && k != kc) {
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

        int dw = Math.abs(wc - ws);
        int dk = Math.abs(kc - ks);

        return dw <= 1 && dk <= 1;
    }
}