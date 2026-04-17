#Szachy Android – Projekt
Opis aplikacji

Jest to prosta aplikacja szachowa na Androida napisana w Javie. Gra umożliwia rozgrywkę dwóch graczy na jednym urządzeniu z podstawową logiką ruchów, turami oraz wykrywaniem szacha i mata.

#W projekcie wykorzystano:

Java (Android SDK)
Android Studio
LinearLayout – układ menu i ekranu gry
GridLayout – plansza szachowa 8×8
TextView – pola szachownicy (figury i interakcje)
Button – start gry
Toast – komunikaty (ruch, szach, mat)
Kolory HEX – stylizacja szachownicy
Struktura projektu
MainActivity.java

Odpowiada za:

wyświetlanie menu i gry
budowanie szachownicy
obsługę kliknięć na polach
wykonywanie ruchów
komunikację z logiką gry i turami
SzachyLogika.java

Odpowiada za:

sprawdzanie legalności ruchów
implementację zasad dla figur:
piony
wieże
gońce
hetman
skoczek
król
sprawdzanie szacha i mata
kontrolę koloru figur
TuraManager.java

Odpowiada za:

zarządzanie turami graczy
kontrolę kto wykonuje ruch (białe / czarne)
zmianę aktywnego gracza po ruchu
Jak działa gra
1. Start gry

Po kliknięciu przycisku „GRAJ”:

ukrywane jest menu
wyświetlana jest plansza
inicjalizowana jest logika gry i tury
2. Plansza
Plansza ma rozmiar 8×8
Każde pole to TextView
Kolory pól są naprzemienne:
jasne: #EEEED2
ciemne: #769656
Figury są zapisane jako znaki Unicode (piony, wieże, skoczki itd.)
3. Wybór i ruch figury

Proces gry:

Gracz klika figurę
System zapisuje jej pozycję
Gracz klika pole docelowe
System:
sprawdza turę
sprawdza legalność ruchu (SzachyLogika)
wykonuje ruch
zmienia turę
4. Zasady ruchów

Każda figura ma własne reguły:

pion – ruch o 1 lub 2 pola do przodu
wieża – ruch w liniach prostych
goniec – ruch po przekątnej
hetman – wieża + goniec
skoczek – ruch w kształcie L
król – jedno pole w każdym kierunku

Dodatkowo sprawdzane jest blokowanie ruchu przez inne figury.

5. Tury
Gra zaczyna się od białych
Po każdym ruchu zmienia się tura
Nie można ruszyć figurą przeciwnika
6. Szach i mat

Szach:
System sprawdza, czy król jest atakowany przez figurę przeciwnika.

Mat:
Występuje, gdy:

król jest w szachu
nie istnieje żaden legalny ruch, który go ratuje
Interfejs
Menu startowe z przyciskiem „GRAJ”
Plansza szachowa w GridLayout
Pasek informacyjny nad planszą
Komunikaty Toast informujące o:
ruchu
nielegalnym ruchu
szachu
macie
Architektura

Projekt jest podzielony na 3 warstwy:

UI (MainActivity) – obsługa widoku i kliknięć
Logika (SzachyLogika) – zasady gry
Stan gry (figury + TuraManager) – aktualny stan planszy
Możliwe ulepszenia
podświetlanie legalnych ruchów
animacje przesuwania figur
bicie w przelocie
roszada
zapis i wczytywanie gry
AI przeciwnika
tryb online
