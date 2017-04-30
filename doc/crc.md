# CRC-Karten
Bevor ein konkretes Klassenmodell entwickelt wird, können wir mit CRC-Karten brainstormen.

## Vorgehensweise
Vorgehen wie in der [Einführung](http://www.dfpug.de/konf/konf_1998/02_oop/d_crc/d_crc.htm) beschrieben:
1. Mögliche Klassen ermitteln
2. Genannte Klassen sinnvoll filtern
3. Szenarien festlegen ("Was passiert wenn..."); von Standardfällen hin zu Ausnahmefällen
4. Szenario der Klasse unter *Responsibility* zuordnen, die dafür verantwortlich ist
5. Mitverantwortliche Klassen für das Szenario ermitteln ("Mit wem muss die Klasse dafür zusammenarbeiten")
6. Mitverantwortliche Klasse bei der hauptverantwortlichen Klasse unter *Collaboration* für das Szenario vermerken
7. Szenario bei der mitverantwortlichen Klasse unter *Responsibility* vermerken (Formulierungen hier z.B. "kann/kennt/markiert XYZ")
8. Klassen ggf. umstrukturieren (neue Klassen ergänzen, Superklassen ermitteln)

## Vorlage CRC-Karte

Klasse | XYZ 
-------|---------
*Responsibility* | *Collaboration*
Szenarion 1 | mitverantwortliche Klasse

## Vorschläge Klassen
* Timer (für Zeitmessung)
* Player (mit Attributen wie Punkte, Reihen, Level)
* Stone
* Clock (Taktgeber, lässt sich vielleicht mit Timer kombinieren?)
* Stone Generator (für zufällige Steinfolge)
* Board (Spielfeld)
* Game Controller (reagiert auf Eingaben des Nutzers)
* Message (zum Austausch von Nachrichten zwischen Client und Server)


## Vorschläge Szenarien
### Szenarien Spielstart und -ende
* Der Nutzer startet die Anwendung
* Der Nutzer startet ein Einzelspieler-Spiel
* Der Nutzer startet ein neues Mehrspieler-Spiel
* Der Nutzer tritt einem Mehrspieler-Spiel bei
* Der Nutzer pausiert das Spiel
* Der Nutzer beendet das Spiel
* Der Nutzer startet ein neues Spiel

### Szenarien Spielverlauf
* Ein neuer Stein erscheint
* Der Nutzer betätigt die Pfeiltasten, um den Stein zu bewegen
* Der Stein berührt den linken/rechten Rand des Spielfelds
* Der Stein berührt den unteren Rand des Spielfelds
* Der Stein berührt den oberen Rand des Spielfelds
* Der Stein berührt einen anderen Stein
* Eine Reihe ist vollständig
* Der Nutzer erreicht ein höheres Level
