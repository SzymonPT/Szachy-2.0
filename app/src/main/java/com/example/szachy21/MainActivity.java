package com.example.szachy21;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // ===== GŁÓWNE ELEMENTY INTERFEJSU =====

    LinearLayout menu;        // ekran startowy
    LinearLayout gra;         // ekran faktycznej gry
    GridLayout plansza;       // szachownica

    @Override
    protected void onCreate(Bundle zapisanyStan) {
        super.onCreate(zapisanyStan);
        setContentView(R.layout.activity_main);

        menu = findViewById(R.id.menuLayout);
        gra = findViewById(R.id.gameLayout);
        plansza = findViewById(R.id.chessBoard);

        Button przyciskGraj = findViewById(R.id.btnPlay);
        przyciskGraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startGry(); // przejście z menu do gry
            }
        });
    }

    // ==== START ====
    private void startGry() {

        // Ukrywamy menu
        menu.setVisibility(View.GONE);

        // Pokazujemy ekran gry
        gra.setVisibility(View.VISIBLE);

        // Budujemy szachownicę
        zbudujPlansze();
    }

    // ===== TWORZENIE SZACHOWNICY =====
    private void zbudujPlansze() {

        // tworzenie 8 wierszy
        for (int wiersz = 0; wiersz < 8; wiersz++) {

            // tworzenie 8 kolumn
            for (int kolumna = 0; kolumna < 8; kolumna++) {

                // Tworzymy pole
                View pole = new View(this);

                // ===== KOLORY SZACHOWNICY =====
                // jeśli suma wiersza i kolumny jest parzysta → jasne pole
                if ((wiersz + kolumna) % 2 == 0)
                    pole.setBackgroundColor(Color.parseColor("#EEEED2"));
                else
                    pole.setBackgroundColor(Color.parseColor("#769656"));

                // ===== PARAMETRY UKŁADU =====
                GridLayout.LayoutParams parametry = new GridLayout.LayoutParams();

                // szerokość i wysokość (0 + weight = równe kwadraty)
                parametry.width = 0;
                parametry.height = 0;

                // gdzie ma się znaleźć pole w siatce
                parametry.rowSpec = GridLayout.spec(wiersz, 1f);
                parametry.columnSpec = GridLayout.spec(kolumna, 1f);

                // przypisujemy parametry do pola
                pole.setLayoutParams(parametry);

                // zapisujemy współrzędne
                final int w = wiersz;
                final int k = kolumna;

                // ==== KLIKNIĘCIE POLA ====
                pole.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // pokazuje które pole kliknięto
                        Toast.makeText(MainActivity.this,
                                "Kliknięto pole: " + w + "," + k,
                                Toast.LENGTH_SHORT).show();
                    }
                });

                // dodajemy pole do planszy
                plansza.addView(pole);
            }
        }
    }
}