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

        Button przyciskGraj = findViewById(R.id.btnPlay);
        przyciskGraj.setOnClickListener(v -> startGry());
    }

    private void startGry() {
        menu.setVisibility(View.GONE);
        gra.setVisibility(View.VISIBLE);

        logika = new SzachyLogika(figury);
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
                pole.setTextColor(Color.BLACK);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(w, 1f);
                params.columnSpec = GridLayout.spec(k, 1f);

                pole.setLayoutParams(params);

                final int ww = w;
                final int kk = k;

                pole.setOnClickListener(v -> {

                    if (wybranyWiersz == -1) {

                        if (!figury[ww][kk].equals("")) {
                            wybranyWiersz = ww;
                            wybranaKolumna = kk;

                            Toast.makeText(this,
                                    "Wybrano: " + figury[ww][kk],
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        if (logika.czyRuchDozwolony(
                                wybranyWiersz,
                                wybranaKolumna,
                                ww,
                                kk)) {

                            figury[ww][kk] = figury[wybranyWiersz][wybranaKolumna];
                            figury[wybranyWiersz][wybranaKolumna] = "";

                            Toast.makeText(this,
                                    "Ruch wykonany",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(this,
                                    "Nielegalny ruch",
                                    Toast.LENGTH_SHORT).show();
                        }

                        wybranyWiersz = -1;
                        wybranaKolumna = -1;

                        zbudujPlansze();
                    }
                });

                plansza.addView(pole);
            }
        }
    }
}