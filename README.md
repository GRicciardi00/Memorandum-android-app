# Memorandum
Progetto per la parte Android del corso MobDev, consiste in un'applicazione per la gestione di memo in *java* realizzata con *Android studio*.

## Features principali ed esempi applicativi
Con l'app **Memorandum** è possibile:

 - Aggiungere memo con un titolo, una descrizione, un luogo, una data ed un'ora. <br />
 <img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/Addactivtygif.gif width="25%" height="25%">
 
 - Impostare la memo come ''completata'' o riportarla allo stato ''attiva'' <br />
 <img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/Detailactivitygif.gif width="25%" height="25%">

 - Visualizzare le memo attive, complete o scadute. <br />
 <img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/mainactivirtgif.gif width="25%" height="25%">

 - Visualizzare sulla mappa tutti i promemoria attivi. <br />
 <img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/MapActivitygif.gif width="25%" height="25%">
 
 - Ricevere una notifica sul dispositivo quando ci si avvicina ad una memo attiva (anche se l'app è chiusa). <br />
 <img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/Notificationgif.gif width="25%" height="25%">

## Uno sguardo da vicino
L'applicazione è basata sul modello **MVC**: **M**odel, **V**iew, **C**ontroller.
### Model
>Il *model* si occupa di definire i dati dell'applicazione. <br />

Le classi *Memo* e *MemoList* costituiscono il model dell'applicazione. MemoList permette di ricavare una lista di tutte le memo attive ed ha anche altri metodi utilizzati dalle varie activities.
### View
> Le *view* visualizzano i dati contenuti nel model e "catturano" l'interazione dell'utente con l'applicazione
>
Per quanto riguarda le Views tutte le **activity** dell'applicazione si basano sull'utilizzo di un *ConstraintLayout*, utile per sviluppare un'UI in maniera intuitiva (senza l'utilizzo di views group nidificati).
L'unico **fragment** presente ha un linear layout verticale, vista la semplicità del suo contenuto.
 
### Controller
>I *controller* sono il cervello dell'applicazione: forniscono il collegamento tra view e model, gestiscono gli input dell'utente mandati dalla view e mandano al model eventuali aggiornamenti. 

L'applicazione è formata da 4 **activities** ed un **fragment**, la *comunicazione* tra activities avviente tramite l'utilizzo di **intent**.

 - La **mainActivity** è il punto d'ingresso principale dell'applicazione, composta da una una *text label* che rappresenta che tipo di memo si sta visualizzando (attiva, completata o scaduta), da 3 *floatingActionButton* utilizzati per accedere alle altre 3 activities e da un **RecyclerViewer** utilizzato per mostrare le note.<br />
Oltre a mostrare le note la MainActivity si occupa di inizializzare il *locationManager* per ottenere la posizione dell'utente e inizializza le  *geofences* delle memo attive. Ogni volta che viene chiamato il metodo "*onPause*" della mainActivity (ovvero quando vengno aperte altre activity, o l'app viene lasciata in background) viene chiamato il metodo "*locationManager.removeUpdates()*" per smettere di utilizzare la posizione dell'utente per evitare consumi inutili della batteria del dispositivo.<br />
<img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/MainActivity.png width="25%" height="25%">

 -  L' **addActivity** si occupa dell'aggiunta delle memo, una volta inseriti il *titolo*, una *descrizione*, il *luogo* nei rispettivi *EditText* e la *data* e l'*ora* negli appositi *widget* è possibile creare la memo premendo il tasto apposito.<br />
 Una volta premuto il tasto viene effettuato un controllo per vedere se dei campi sono vuoti,controlla che la data inserita dall'utente sia valida (dal giorno attuale in poi) ed infine verifica se l'operazione di **geocoding** per ricavare latitudine e longitudine dal luogo inserito è andata a buon fine. <br />
La funzione che si occupa di effettuare tutti questi controlli prende il nome di "*saveInput*" ed è impostata come proprietà *onclick* del FloatingActionButton nel file XML *activity_add*, in caso di campi non validi o mancanti viene chiamata una **ToastNotification** per segnalarlo all'utente.<br />
<img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/AddActivity.png width="25%" height="25%">

 - La **detailActivity** viene chiamata nell'override dell'evento *onclick* del **memoAdapter**. Mostra titolo, scadenza, luogo e descrizione della memo selezionata, inoltre è presente un bottone per modificare lo status della memo (attiva/completata),se la memo è scaduta (controllo fatto tramite i due metodi della classe Utils) imposta il TextColor della TextView della data in colore rosso.<br />
<img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/DetailActivity.png width="25%" height="25%">
 
 - La **mapActivity** mostra una mappa,creata tramite l'API map di google, centrata sulla posizione dell'utente.<br />
 L'activity inoltre crea un'annotazione sulla mappa per ogni promemoria dell'ArrayList di memo attive ricevuto dalla mainActivity tramite *Intent*. 
 Quando l'utente clicca su un'annotazione viene mostrato il suo titolo.<br />
<img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/MapActivity.png width="25%" height="25%">
 
 - L'**infoFragment** è accessibile dalla MainActivity, cliccando sul *menuButton* del punto di domanda, mostra una TextView in cui viene illustrata una guida sul funzionamento dell'app. Nel menu è anche presente un altro tasto per mostrare le memo scadute.<br />
<img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/Fragment.png width="25%" height="25%">

### RecyclerViewer
>La classe RecyclerView semplifica la visualizzazione e la gestione di insiemi di dati di grandi dimensioni, è un contenitore per la visualizzazione di insiemi di dati di grandi dimensioni che è possibile scorrere in modo efficiente mantenendo un numero limitato di Views.

La visualizzazione delle memo avviene tramite **recyclerViewer** della mainActivity. La classe **MemoAdapter** rappresenta l'adattatore della recyclerView, viene utilizzato per fornire al LayoutManager informazioni su quanti elementi devono essere visualizzati e la View effettiva da utilizzare. <br />
In questo progetto tutte le memo sono presenti su un unico Adapter dotato di un attributo "*status*" che indica quale tipo di memo mostrare (attive/completate/scadute), in base al bottone che l'utente preme viene impostato uno dei tre stati come attributo dalla MainActivity. <br />
Il controllo per decidere quale tipo di memo visualizzare viene eseguito nell'override del metodo *onBindViewHolder*, in base allo stato viene impostato anche il colore e lo stile del carattere da impostare, tutte le memo vengono mostrate in ordine di data. <br />
**NB** : un'alternativa valida a questo tipo di approccio era quella di utilizzare dei fragments con memoadapter differenti per rappresentare le varie tipologie di memo.

### Notifications & Geofencing
Le classi che si occupano di creare le geofences e gestire l'invio delle notifiche sono: *GeofenceHelper*,*Notification Helper*, *NotificationBroadcast receiver*.
 - **GeofenceHelper** comprende tutti i metodi utili alla creazione di geofences (tramite API Google), utilizzati nella mainActivity. Per utilizzare le geofences è necessario l'autorizzazione da parte dell'utente per: ACCESS_BACKGROUND_LOCATION. Ogni GeoFenceHelper ha una memo associata che viene utilizzata dal metodo "getPendingIntent" per ottenere le informazioni della memo e passarle al **GeoFencingClient** che si occupa della registrazione della geofence.
 - **NotificationBroadCastReceiver** recupera le informazioni passate al **GeoFencingClient** e chiama il metodo ''*sendHighPriorityNotification*'' della classe **NotificationHelper** per mandare una notifica.
 - **NotificationHelper** si occupa di gestire tutte le operazioni del *NotificationManager*, crea un canale di ricezione delle notifiche e manda le notifiche su quel canale.
