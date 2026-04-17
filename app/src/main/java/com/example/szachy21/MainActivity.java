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

    // UI w Androidzie to drzewa widoków (View hierarchy)
    // tutaj trzymamy referencje do 3 głównych elementów ekranu
    LinearLayout menu;     // ekran startowy
    LinearLayout gra;      // ekran gry
    GridLayout plansza;    // siatka 8x8 (szachownica)

    // te zmienne przechowują STAN kliknięcia
    // Android nie pamięta "wyboru", więc robimy to ręcznie
    int wybranyWiersz = -1;
    int wybranaKolumna = -1;

    // obiekty logiki gry (oddzielone od UI)
    SzachyLogika logika;
    TuraManager tura;

    // TABLICA STANU GRY
    // to jest „źródło prawdy” — UI tylko to wyświetla
    String[][] figury = {
            {"♜","♞","♝","♛","♚","♝","♞","♜"},
            {"♟","♟","♟","♟","♟","♟","♟","♟"},
            {"","","","","","","",""},
            {"","","","","","","",""},
            {"","","","","","","",""},
            {"","","","","","","",""},
            {"♙","♙","♙","♙","♙","♙","♙","♙"},
            {"♖","♘","♗","♕","♔","♗","♘","♖"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ładowanie XML → tworzy wszystkie View w pamięci
        setContentView(R.layout.activity_main);

        // findViewById łączy XML z Javą (binding ręczny)
        menu = findViewById(R.id.menuLayout);
        gra = findViewById(R.id.gameLayout);
        plansza = findViewById(R.id.chessBoard);

        // kliknięcie start = zmiana ekranu + start logiki
        Button btn = findViewById(R.id.btnPlay);
        btn.setOnClickListener(v -> startGry());
    }

    // ================= START GRY =================
    private void startGry() {

        // Android NIE usuwa widoków — tylko zmienia ich widoczność
        menu.setVisibility(View.GONE);
        gra.setVisibility(View.VISIBLE);

        // tworzymy logikę i przekazujemy REFERENCJĘ do tablicy
        // czyli logika i UI pracują na tych samych danych
        logika = new SzachyLogika(figury);

        // start tury (domyślnie białe)
        tura = new TuraManager();

        // render pierwszej klatki planszy
        zbudujPlansze();
    }

    // ================= RENDER SZACHOWNICY =================
    private void zbudujPlansze() {

        // kasujemy wszystkie stare TextView
        // (Android nie "odświeża automatycznie" siatki)
        plansza.removeAllViews();

        // 2 pętle = przejście po 8x8
        for (int w = 0; w < 8; w++) {
            for (int k = 0; k < 8; k++) {

                // każde pole to osobny obiekt UI
                TextView pole = new TextView(this);

                // kolorowanie szachownicy (checker pattern)
                pole.setBackgroundColor(((w + k) % 2 == 0)
                        ? Color.parseColor("#EEEED2")
                        : Color.parseColor("#769656"));

                // tekst = aktualna zawartość tablicy figury
                pole.setText(figury[w][k]);

                pole.setTextSize(28);
                pole.setGravity(android.view.Gravity.CENTER);

                // ważne: usuwa przesunięcia fontu emoji
                pole.setIncludeFontPadding(false);

                pole.setTextColor(Color.BLACK);

                // GridLayout wymaga wag 1f żeby kafelki były równe
                GridLayout.LayoutParams p = new GridLayout.LayoutParams();
                p.width = 0;
                p.height = 0;
                p.rowSpec = GridLayout.spec(w, 1f);
                p.columnSpec = GridLayout.spec(k, 1f);

                pole.setLayoutParams(p);

                // kopiujemy indeksy, bo Java lambda wymaga final
                final int ww = w;
                final int kk = k;

                // ================= KLIK NA POLE =================
                pole.setOnClickListener(v -> {

                    // ==================================================
                    // FAZA 1: wybór figury
                    // ==================================================
                    if (wybranyWiersz == -1) {

                        // nie można kliknąć pustego pola
                        if (figury[ww][kk].equals("")) return;

                        // sprawdzamy kto ma ruch (logika tury)
                        boolean biale = tura.czyBialeTeraz();
                        String figura = figury[ww][kk];

                        // blokada: nie możesz kliknąć figury przeciwnika
                        if (biale && !tura.czyBiala(figura)) return;
                        if (!biale && tura.czyBiala(figura)) return;

                        // zapamiętujemy start ruchu
                        wybranyWiersz = ww;
                        wybranaKolumna = kk;
                        return;
                    }

                    // ==================================================
                    // FAZA 2: próba ruchu
                    // ==================================================
                    boolean biale = tura.czyBialeTeraz();

                    // pytamy logikę: czy ten ruch jest legalny
                    boolean ruch = logika.czyRuchDozwolony(
                            wybranyWiersz,
                            wybranaKolumna,
                            ww,
                            kk,
                            biale
                    );

                    if (ruch) {

                        // zapisujemy ruch w tablicy (MODEL)
                        figury[ww][kk] = figury[wybranyWiersz][wybranaKolumna];
                        figury[wybranyWiersz][wybranaKolumna] = "";

                        // odtwarzamy logikę (bo zmienił się stan planszy)
                        logika = new SzachyLogika(figury);

                        // zmiana tury
                        tura.zmienTure();

                        boolean nowaTuraBiale = tura.czyBialeTeraz();

                        // sprawdzanie szacha po ruchu
                        if (logika.czySzach(nowaTuraBiale)) {
                            Toast.makeText(this, "SZACH!", Toast.LENGTH_SHORT).show();
                        }

                        // sprawdzanie mata
                        if (logika.czyMat(nowaTuraBiale)) {
                            Toast.makeText(this, "MAT! KONIEC GRY", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(this, "Nielegalny ruch", Toast.LENGTH_SHORT).show();
                    }

                    // reset wyboru (żeby następny klik był od nowa)
                    wybranyWiersz = -1;
                    wybranaKolumna = -1;

                    // pełny redraw UI (Android nie aktualizuje automatycznie TextView w GridLayout)
                    zbudujPlansze();
                });

                // dodajemy pole do siatki
                plansza.addView(pole);
            }
        }
    }
}