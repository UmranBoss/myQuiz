<p align="center">
  <h1>My MemoBoard</h1>
</p>

# 🧠 My MemoBoard
**My MemoBoard** ist eine **Java-basierte Webanwendung**, entwickelt mit **Eclipse** und betrieben über einen **Tomcat-Webserver**. Sie dient als persönliche Organisationsplattform mit mehreren geplanten Modulen für Aufgabenverwaltung, Protokolle, Finanzen und mehr.  
⚠️ Das Projekt befindet sich derzeit **in aktiver Entwicklung** – zwei Module sind bereits nutzbar, jedoch noch nicht vollständig fertiggestellt.

## 📝 Projektbeschreibung
Die Anwendung umfasst sechs geplante Module zur persönlichen Verwaltung:

- ✅ **DoneDing**: Aufgaben- und To-do-Liste  
- 🧾 **StudySnap**: Unterrichtsprotokolle verwalten und speichern  
- 💸 **CashCheck**: Budgetübersicht und Finanzplanung  
- 📄 **VertragVox**: Verwaltung von Verträgen und Abonnements  
- 🩺 **DocDrop**: Überblick über Arzttermine und Erinnerungen  
- 📘 **SeelenSkript**: Persönliche Notizen und tägliche Einträge

### 🧩 Derzeit implementierte Module:

#### ✅ DoneDing
Das Aufgabenmodul bietet derzeit folgende Funktionen:
- Aufgaben mit **Titel, Beschreibung und Fälligkeitsdatum** erstellen
- Optional: **Erinnerungsdatum und -uhrzeit** setzen
- Aufgaben können **kategorisiert** und mit **Prioritäten** versehen werden

#### 🧾 StudySnap
Dieses Modul dient zur Erfassung von Unterrichtsprotokollen – aktuell beispielhaft angepasst an die Ausbildung zum **Fachinformatiker für Anwendungsentwicklung**:
- Auswahl des **Unterrichtsmoduls** über ein Dropdown-Menü (z. B. Netzwerktechnik, Datenbanken, Programmierung)
- Freitextfeld für das **konkrete Thema**
- Protokolle können als **CSV-Datei heruntergeladen** werden – ideal zur Archivierung oder Weitergabe
  
## 💾 Datenspeicherung
- Derzeit erfolgt die **persistente Datenspeicherung über Serialisierung** in .ser-Dateien.
- In späteren Entwicklungsphasen ist ein **Wechsel auf SQLite oder eine andere relationale Datenbank** geplant.

## 🌐 Technische Umsetzung
Die Anwendung ist als **dynamische Webanwendung** aufgebaut. Ein Tomcat-Servlet-Container verarbeitet die Client-Server-Kommunikation. Die Serverlogik wird mit **Java-Servlets** umgesetzt, erste Seiten mit einfachem HTML/JSP.

## ⚙️ Technologien
- **Java** (Backend – Servlets, Serialisierung)
- **Apache Tomcat** (Webserver)
- **HTML/CSS/JavaScript** (Frontend)
- **Eclipse IDE**
- (Zukünftig: **SQLite** für Datenpersistenz)


## 📐 Design & Struktur
- Simple grafische Mockups in Arbeit

## 🚧 Projektstatus
- ✅ Erste Funktionen von DoneDing und StudySnap lauffähig
- 📁 Speicherung erfolgt lokal via .ser-Dateien
- 🧱 Fundamentale Serverlogik (Routing, Sessions etc.) implementiert
- 🛠️ Weitere Module in Planung & Strukturierung

---

### 🛠️ Geplante Erweiterungen
- Benutzerverwaltung & Login-System
- Datenexport (weitere Formate)
- Responsives Frontend (mobile Ansicht)
- SQLite-Anbindung statt Serialisierung
- Kalender- und Erinnerungsfunktionen
- Mobile App (iOS)
- KI (TTS, STT)
- Personalisierte Statistiken
- Dark/Light-Modus & personalisierte UI
- Sicherheit (Ende-zu-Ende-Verschlüsselung, OAuth)
- Offline-Modus
- Mehrsprachigkeit
  

