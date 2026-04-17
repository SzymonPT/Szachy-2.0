package com.example.szachy21;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // ===== UI =====
    LinearLayout menu;     // ekran menu startowego
    LinearLayout gra;      // ekran gry
    GridLayout plansza;    // szachownica 8x8

    // ===== WYBÓR POLA =====
    // przechowuje pierwszy klik (skąd ruszamy)
    int wybranyWiersz = -1;
    int wybranaKolumna = -1;

    // ===== LOGIKA GRY =====
    SzachyLogika logika;   // sprawdzanie ruchów i szacha/mata
    TuraManager tura;      // kontrola czyje są ruchy (białe/czarne)

    // ===== PLANSZA SZACHOWA =====
    String[][] figury = {
            {"♜","♞","♝","♛","♚","♝","♞","♜"}, // czarne figury
            {"♟","♟","♟","♟","♟","♟","♟","♟"}, // czarne piony
            {"","","","","","","",""},
            {"","","","","","","",""},
            {"","","","","","","",""},
            {"","","","","","","",""},
            {"♙","♙","♙","♙","♙","♙","♙","♙"}, // białe piony
            {"♖","♘","♗","♕","♔","♗","♘","♖"}  // białe figury
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // wczytanie layoutu XML
        setContentView(R.layout.activity_main);

        // podpięcie widoków z XML
        menu = findViewById(R.id.menuLayout);
        gra = findViewById(R.id.gameLayout);
        plansza = findViewById(R.id.chessBoard);

        // przycisk start gry
        Button btn = findViewById(R.id.btnPlay);
        btn.setOnClickListener(v -> startGry());
    }

    // ================= START GRY =================
    private void startGry() {

        // ukryj menu
        menu.setVisibility(View.GONE);

        // pokaż planszę
        gra.setVisibility(View.VISIBLE);

        // tworzenie logiki gry
        logika = new SzachyLogika(figury);

        // ustawienie tury (domyślnie białe)
        tura = new TuraManager();

        // rysowanie planszy
        zbudujPlansze();
    }

    // ================= RYSOWANIE SZACHOWNICY =================
    private void zbudujPlansze() {

        // czyścimy starą planszę
        plansza.removeAllViews();

        // przechodzimy po wszystkich polach 8x8
        for (int w = 0; w < 8; w++) {
            for (int k = 0; k < 8; k++) {

                // tworzymy pojedyncze pole
                TextView pole = new TextView(this);

                // kolor pola (szachownica)
                pole.setBackgroundColor(((w + k) % 2 == 0)
                        ? Color.parseColor("#EEEED2")
                        : Color.parseColor("#769656"));

                // ustawienie figury na polu
                pole.setText(figury[w][k]);
                pole.setTextSize(28);
                pole.setGravity(android.view.Gravity.CENTER);
                pole.setIncludeFontPadding(false);
                pole.setTextColor(Color.BLACK);

                // rozciąganie pola w GridLayout (równe kafelki)
                GridLayout.LayoutParams p = new GridLayout.LayoutParams();
                p.width = 0;
                p.height = 0;
                p.rowSpec = GridLayout.spec(w, 1f);
                p.columnSpec = GridLayout.spec(k, 1f);

                pole.setLayoutParams(p);

                // zapis współrzędnych klikniętego pola
                final int ww = w;
                final int kk = k;

                // ================= KLIK NA POLE =================
                pole.setOnClickListener(v -> {

                    // ===== 1. WYBÓR FIGURY =====
                    if (wybranyWiersz == -1) {

                        // jeśli pole jest puste → nic nie rób
                        if (figury[ww][kk].equals("")) return;

                        boolean biale = tura.czyBialeTeraz(); // kto ma ruch teraz
                        String figura = figury[ww][kk];

                        // sprawdzenie czy gracz kliknął swoją figurę
                        if (biale && !tura.czyBiala(figura)) {
                            Toast.makeText(this, "Nie twoja tura", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!biale && tura.czyBiala(figura)) {
                            Toast.makeText(this, "Nie twoja tura", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // zapamiętanie wybranej figury
                        wybranyWiersz = ww;
                        wybranaKolumna = kk;
                        return;
                    }

                    // ===== 2. WYKONANIE RUCHU =====
                    boolean biale = tura.czyBialeTeraz();

                    // sprawdzanie czy ruch jest legalny
                    boolean ruch = logika.czyRuchDozwolony(
                            wybranyWiersz,
                            wybranaKolumna,
                            ww,
                            kk,
                            biale
                    );

                    if (ruch) {

                        // wykonanie ruchu na planszy
                        figury[ww][kk] = figury[wybranyWiersz][wybranaKolumna];
                        figury[wybranyWiersz][wybranaKolumna] = "";

                        // odświeżenie logiki (ważne!)
                        logika = new SzachyLogika(figury);

                        // zmiana tury
                        tura.zmienTure();

                        boolean nowaTuraBiale = tura.czyBialeTeraz();

                        // ===== SZACH =====
                        if (logika.czySzach(nowaTuraBiale)) {
                            Toast.makeText(this, "SZACH!", Toast.LENGTH_SHORT).show();
                        }

                        // ===== MAT =====
                        if (logika.czyMat(nowaTuraBiale)) {
                            Toast.makeText(this, "MAT! KONIEC GRY", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        // nielegalny ruch
                        Toast.makeText(this, "Nielegalny ruch", Toast.LENGTH_SHORT).show();
                    }

                    // reset wyboru figury
                    wybranyWiersz = -1;
                    wybranaKolumna = -1;

                    // przerysowanie planszy
                    zbudujPlansze();
                });

                // dodanie pola do planszy
                plansza.addView(pole);
            }
        }
    }
}