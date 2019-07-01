# VRP-Offline
Trasporto pubblico nella città di Roma: un approccio metaeuristico all'ottimizzazione delle rotte degli autobus

Istruzioni per importare il progetto offline nell'ambiente di sviluppo:

- Creare un nuovo progetto Java denominato "VRP-Offline"
- Scaricare il file .zip di questa Github repository ed importarlo nel progetto appena creato
- Assicurarsi che la cartella "src" sia "source folder" (da usare per i file sorgente) 
- Assicurarsi che "core", "gui", "test" e "utils" (all'interno di "src") siano importati come package
- Scaricare il file .zip al seguente URL: https://drive.google.com/open?id=1i79DTvzXBMgfTH2_M1SY1fBmM63MOgjG
- Estrarre il precedente file .zip e inserire la cartella estratta "data" nella directory principale del progetto
- Includere tale cartella "data" nel classpath del progetto tramite le relative configurazioni build path
- Includere le librerie contenute nella cartella "VRP-Offline_lib" nel module path tramite le stesse configurazioni build path del progetto
- Creare una libreria utente denominata "commons.math3" che includa il file .jar memorizzato nella cartella "CommonsMath_lib" del progetto ed aggiungerla al module path del progetto
- Scaricare la libreria JavaFX 12.0.1 SDK specifica per il sistema operativo in uso al seguente URL: https://gluonhq.com/products/javafx/ 
- Estrarla ed aggiungere i file .jar (path: /lib) in una nuova libreria utente "JavaFX12" (senza modificare i percorsi delle sottocartelle sul disco fisso e/o spostare gli altri file di sistema)
- Includere tale libreria "JavaFX12" nel module path del progetto (per sicurezza effettuare un cleaning del progetto al termine delle precedenti operazioni)
- Utilizzare versioni JDK 11+ per assicurarsi che il programma funzioni
- Effettuare un cleaning del progetto
- Eseguire la classe "Main", all'interno del package "gui" (percorso: gui.Main)

Note: per testare una linea bisogna scrivere semplicemente il numero corrispondente nel campo di testo e cliccare sul bottone desiderato; sono supportate le seguenti linee: 23, 30, 60, 64, 160, 218, 649, 671, 714, 716; i relativi file di testo sono memorizzati nella cartella "data/buses/" del progetto

Non è stato possibile caricare tutti i file direttamente su questa repository a causa dei limiti di spazio

![RacoMaps1](https://user-images.githubusercontent.com/51203516/60355243-3d2a3600-99ce-11e9-8066-f6ef378548ae.PNG)
![RacoMaps2](https://user-images.githubusercontent.com/51203516/60355260-487d6180-99ce-11e9-9999-eab6590fed12.png)
![RacoMaps3](https://user-images.githubusercontent.com/51203516/60355273-4fa46f80-99ce-11e9-829e-775d518326b0.png)
![RacoMaps4](https://user-images.githubusercontent.com/51203516/60355294-5632e700-99ce-11e9-8be9-baa026bc60e2.png)
