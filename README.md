<p align="center">
  <img src="/images/answer.png" width="100" alt="logo" />
</p>

# 🎯 Quiz-Anwendung
Dies ist eine **Java-basierte Quiz-Anwendung**, entwickelt mit **Eclipse**, **Swing** für die GUI und **MySQL** für die Datenbankanbindung. Das Projekt ist **noch in Entwicklung**.
## 📝 Projektbeschreibung
Die Anwendung bbQQuiz dient Lehrkräften dazu, Quizze zu erstellen, zu speichern, zu verwalten und zu exportieren. Fragen bestehen aus einem Fragetext, vier Antwortmöglichkeiten, einer Kennzeichnung der richtigen Antwort(en) und einer zugehörigen Punktzahl (auch negativ bei falschen Antworten).
Die Fragen werden nach Themen und Kategorien strukturiert und als Einfach- oder Mehrfachwahl klassifiziert. Eine Exportfunktion ermöglicht die Ausgabe der Fragen als formatierte Textdatei (z.B. CSV).
### 📌 Hauptfunktionen:
- **Verbindung zur MySQL-Datenbank**  für die Verwaltung von Quizzen, Fragen, Antworten, Kategorien und Themen.
- **Objektorientierte Konzepte**: Vererbung, Polymorphismus, Datenkapselung für die Strukturierung der Quiz-Daten.
- **GUI mit Swing** zur Interaktion mit dem Benutzer, insbesondere zur Erstellung und Verwaltung von Quizfragen.
- **Export-Funktion** für das Speichern von Quizfragen als CSV- oder TXT-Dateien.
### 👥 Wichtige Klassen:
- **Lehrer**: Repräsentiert den Benutzer, der Quizze erstellt und verwaltet.
- **Quiz**: Repräsentiert ein Quiz, das Fragen enthält, und speichert Titel, Kategorie und Thema.
- **Frage**: Repräsentiert eine einzelne Quizfrage mit den zugehörigen Antwortmöglichkeiten und Punktzahlen.
- **Antwort**: Repräsentiert die möglichen Antworten auf eine Frage, einschließlich der Richtigkeit und Punktzahl.
## ⚙️ Technologien
- **Java** (Backend-Entwicklung)
- **MySQL** (Datenbank)
- **JDBC** (Datenbankanbindung)
- **Swing** (Frontend-GUI u.a. für die Frageerstellung)
## 📊 Architektur
- [**ER-Modell**](/images/mySQL_ERD.png), [**Klassendiagramm**](/images/KlassenDiagramm.png) & [**Packagediagramm**](/images/PackageDiagramm.png) zur Datenstruktur.
## 📐 Design
- Das [**Mockup**](/images/Mockup.pdf) zeigt das geplante Layout dieser Anwendung
## 🚧 Projektstatus
- **In Progress**: Die **Data Layer** (Datenbankschicht) wurde bereits implementiert, und weitere Funktionen werden kontinuierlich hinzugefügt.
---
### 🛠️ Weitere Features:
- **Benutzerverwaltung**: Möglichkeit für Lehrer, sich einzuloggen und Quizze zu erstellen und zu verwalten.
- **Exportfunktion**: Quizfragen können als CSV- oder TXT-Datei exportiert werden.
- **Erweiterte Suchfunktionen** für Fragen und Quizze nach Thema, Kategorie oder Titel.
