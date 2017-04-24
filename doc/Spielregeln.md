# Spielregeln

Der Einfachheit halber halten wir uns an die klassische Gameboy-Version.

## Spielfeld

Das Spielfeld besteht aus quadratischen Blöcken. Es ist 10 Blöcke breit und 20 Blöcke hoch.

Rechts neben dem Spielfeld sehen wir die Punkte, das aktuelle Level und die geschafften Reihen. Darunter sieht man den nächsten Block.

![](https://upload.wikimedia.org/wikipedia/de/9/9e/Tetrisgb.jpg)

Das Bild soll nur ein ungefähres Beispiel sein.

## Ablauf

Es gibt einen Taktgeber für die fallenden Steine. Mit jedem Level wird der Takt verkürzt und die Steine fallen schneller. Ein Level ist nach 10 Reihen geschafft (TODO: 10 OK?)

Die Steine fallen von oben nach unten herunter. Sie erscheinen schrittweise, sind also beim ersten Takt nicht sofort komplett sichtbar (TODO: OK?).

Der vorletzte Takt eines Steins legt diesen ab. Er lässt sich dann aber noch seitlich vom Spieler verschieben. Erst der nachfolgende Takt beendet die Ablage des Steins.

Wenn eine Reihe voll mit Steinen ist, verschindet sie und der Spieler bekommen Punkte.

## Steuerung

Man spielt mit den Cursor-tasten auf der Tastatur. *Rechts* und *Links* schieben den Stein zu den Seiten. *Hoch* dreht den Stein um 90° im Uhrzeigersinn. *Runter* lässt den Stein schneller fallen.

## Steine

Es gibt diese Steine:

![](https://upload.wikimedia.org/wikipedia/commons/thumb/3/39/Tetrominoes_IJLO_STZ_Worlds.svg/360px-Tetrominoes_IJLO_STZ_Worlds.svg.png)

## Mehrspielermodus

Ein Spieler kann ein Spiel hosten (Server) (TODO: auch Taktgeber?). Ein zweiter Spieler kann sich zu diesem Spieler verbinden. Wenn ein Spieler eine Reihe abbaut, bekommt der andere Spieler diese Reihe unter seinem Steinstabel dazu mit einem zufälligen freien Block in der Reihe.