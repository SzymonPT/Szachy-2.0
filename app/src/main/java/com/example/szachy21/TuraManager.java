package com.example.szachy21;

public class TuraManager {

    private boolean bialeTeraz = true;

    public boolean czyMoznaRuszyc(String figura) {

        if (figura.equals("")) return false;

        // białe figury
        boolean biala =
                figura.equals("♙") ||
                        figura.equals("♖") ||
                        figura.equals("♘") ||
                        figura.equals("♗") ||
                        figura.equals("♕") ||
                        figura.equals("♔");

        if (bialeTeraz && !biala) return false;
        if (!bialeTeraz && biala) return false;

        return true;
    }

    public void zmienTure() {
        bialeTeraz = !bialeTeraz;
    }

    public boolean czyBialeTeraz() {
        return bialeTeraz;
    }

    public String napisTury() {
        return bialeTeraz ? "Ruch białych" : "Ruch czarnych";
    }
}