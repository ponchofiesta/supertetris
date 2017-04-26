# Multiplayer

Der Multiplayer-Modus erlaubt es zwei Spielern per Netzwerk gegeneinader zu spielen. Dafür wird ein Protokoll benötigt, mit dem Server und Client kommunizieren.

Es werden keine aktuellen Spielinfos übertragen. Das macht das Protkoll sehr einfach. Es wird nur übertragen, wenn Reihen beim gegenspieler hinzugefügt werden müssen.

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

Das bisher einzige Kommando ist das Hinzufügen von Reihen:

```javascript
{
	"cmd": "addrows",
    "data": {
    	"count": <rowcount>
    }
}
```

Eine Nachricht sieht dann bspw. so aus (In Java würden aber erst ein Object erstellen, dass wir dann per Methode umwandeln):

```
{"cmd":"addrows","data":{"count":1}}
```

Eine Bestätigung lassen wir erst mal außen vor. Wir sind Fehlertolerant und nehmen an, dass jedes Kommando korrekt ankommt. Eine Fehlerbehandlung ist aufwendung und bringt für unser Spiel nur wenig. 

Eine Kollisionsbehandlung benötigen wir nur am Ende des Spiels. Es besteht die Möglichkeit, dass beide Spieler in etwa zeitgleich ihr Spiel beenden. Hier für wird einfach der Server als Maß herangezogen. Das was bei ihm als erstes passiert, ist entscheidend.