# Multiplayer

Der Multiplayer-Modus erlaubt es zwei Spielern per Netzwerk gegeneinader zu spielen. Dafür wird ein Protokoll benötigt, mit dem Server und Client kommunizieren.

Die Server-Suche und -Antwort laufen per UDP. Im Spiel wird per TCP kommuniziert. Alles läuft über Port 1337.

> Es werden keine aktuellen Spielinfos übertragen. Das macht das Protkoll sehr einfach. 
> Es wird nur übertragen, wenn Reihen > beim gegenspieler hinzugefügt werden müssen.

Dieses Verhalten ist zwar einfacher zu implementieren, aber m.E. auch etwas langweilig, weil ich als Spieler kaum nachverfolgen kann, wie sich mein Gegner schlägt. Schöner wäre, aktuelle Spielinfos zu übertragen, sodass ich sehe, was mein Gegner aktuell tut und ob ich womöglich bald mit einer neuen Reihe rechnen muss (siehe [Mockup zum Multiplayer-Mode](mockup/Multiplayer_Game_Mockup.png))

## Protokoll

Als Format wird JSON eingesetzt. Jede Nachricht ist auf eine Zeile festgelegt. Es dürfen keine Zeilenumbrüche auftauchen. So ist sichergestellt, dass die Nachricht komplett als solche gelesen werden aknn und auch das Ende der Nachricht klar definiert ist - Durch einen Zeilenumbruch.

Nachrichten sind generell so aufgebaut:

```javascript
{
    "cmd": <command>,
    "data": <data>
}
```
Das erlaubt auch das Protokoll ggf. zu erweitern.

### Ablauf

TODO: Sequenzdiagramm

Um einen Server zu suchen wird ein UDP-Broadcast mit einer Suchanfrage gesendet:

```javascript
{
    "cmd": "whoisserver",
    "data": {}
}
```

Diese Anfrage wird vom Server mit einer UDP-Antwort an den Client beantwortet:

```javascript
{
    "cmd": "iamserver",
    "data": {
    	"gamename": <gamename>
    }
}
```

Der Client baut eine TCP-Verbindung zum Server auf und tritt dem Spiel bei:

```javascript
{
    "cmd": "joingame",
    "data": {
    	"gamename": <gamename>
    }
}
```

Der Server startet das Spiel:

```javascript
{
    "cmd": "startgame",
    "data": {}
}
```

Während des Spiels werden Infos über hinzuzufügende Reihen von beiden Seiten gsendet:

```javascript
{
    "cmd": "addrows",
    "data": {
    	"count": <rowcount>
    }
}
```

Wenn ein Spieler am oben Rand ankommt oder den Beenden-Button drückt, also verliert, sendet er:

```javascript
{
    "cmd": "gameover",
    "data": {}
}
```

Pause-Button:

```javascript
{
    "cmd": "startpause",
    "data": {}
}
```

```javascript
{
    "cmd": "endpause",
    "data": {}
}
```

### Beispiel-Nachricht

Eine Nachricht sieht dann bspw. so aus (In Java würden aber erst ein Object erstellen, dass wir dann per Methode umwandeln):

```
{"cmd":"addrows","data":{"count":1}}
```

Eine Bestätigung lassen wir erst mal außen vor. Wir sind Fehlertolerant und nehmen an, dass jedes Kommando korrekt ankommt. Eine Fehlerbehandlung ist aufwendung und bringt für unser Spiel nur wenig. 

Eine Kollisionsbehandlung benötigen wir nur am Ende des Spiels. Es besteht die Möglichkeit, dass beide Spieler in etwa zeitgleich ihr Spiel beenden. Hier für wird einfach der Server als Maß herangezogen. Das was bei ihm als erstes passiert, ist entscheidend.
