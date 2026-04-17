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

    LinearLayout menu;
    LinearLayout gra;
    GridLayout plansza;

    int wybranyWiersz = -1;
    int wybranaKolumna = -1;

    SzachyLogika logika;
    TuraManager tura;

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
        setContentView(R.layout.activity_main);

        menu = findViewById(R.id.menuLayout);
        gra = findViewById(R.id.gameLayout);
        plansza = findViewById(R.id.chessBoard);

        Button btn = findViewById(R.id.btnPlay);
        btn.setOnClickListener(v -> startGry());
    }

    private void startGry() {

        menu.setVisibility(View.GONE);
        gra.setVisibility(View.VISIBLE);

        logika = new SzachyLogika(figury);
        tura = new TuraManager();

        zbudujPlansze();
    }

    private void zbudujPlansze() {

        plansza.removeAllViews();

        for (int w = 0; w < 8; w++) {
            for (int k = 0; k < 8; k++) {

                TextView pole = new TextView(this);

                pole.setBackgroundColor(((w + k) % 2 == 0)
                        ? Color.parseColor("#EEEED2")
                        : Color.parseColor("#769656"));

                pole.setText(figury[w][k]);
                pole.setTextSize(28);
                pole.setGravity(android.view.Gravity.CENTER);
                pole.setIncludeFontPadding(false);
                pole.setTextColor(Color.BLACK);

                GridLayout.LayoutParams p = new GridLayout.LayoutParams();
                p.width = 0;
                p.height = 0;
                p.rowSpec = GridLayout.spec(w, 1f);
                p.columnSpec = GridLayout.spec(k, 1f);

                pole.setLayoutParams(p);

                final int ww = w;
                final int kk = k;

                pole.setOnClickListener(v -> {

                    // ================= WYBÓR FIGURY =================
                    if (wybranyWiersz == -1) {

                        if (figury[ww][kk].equals("")) return;

                        boolean biale = tura.czyBialeTeraz();
                        String figura = figury[ww][kk];

                        // blokada tury
                        if (biale && !tura.czyBiala(figura)) {
                            Toast.makeText(this, "Nie twoja tura", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!biale && tura.czyBiala(figura)) {
                            Toast.makeText(this, "Nie twoja tura", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        wybranyWiersz = ww;
                        wybranaKolumna = kk;
                        return;
                    }

                    // ================= RUCH =================
                    boolean biale = tura.czyBialeTeraz();

                    boolean ruch = logika.czyRuchDozwolony(
                            wybranyWiersz,
                            wybranaKolumna,
                            ww,
                            kk,
                            biale
                    );

                    if (ruch) {

                        // wykonanie ruchu
                        figury[ww][kk] = figury[wybranyWiersz][wybranaKolumna];
                        figury[wybranyWiersz][wybranaKolumna] = "";

                        // synchronizacja logiki
                        logika = new SzachyLogika(figury);

                        // zmiana tury
                        tura.zmienTure();

                        boolean nowaTuraBiale = tura.czyBialeTeraz();

                        // SZACH
                        if (logika.czySzach(nowaTuraBiale)) {
                            Toast.makeText(this, "SZACH!", Toast.LENGTH_SHORT).show();
                        }

                        // MAT
                        if (logika.czyMat(nowaTuraBiale)) {
                            Toast.makeText(this, "MAT! KONIEC GRY", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(this, "Nielegalny ruch", Toast.LENGTH_SHORT).show();
                    }

                    wybranyWiersz = -1;
                    wybranaKolumna = -1;

                    zbudujPlansze();
                });

                plansza.addView(pole);
            }
        }
    }
}