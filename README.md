# Memorandum
Progetto per la parte Android del corso MobDev, consiste in un'applicazione per la gestione di memo in *java* realizzata con *Android studio*.

## Features principali ed esempi applicativi
Con l'app **Memorandum** è possibile:

 - Aggiungere memo con un titolo, una descrizione, un luogo, una data ed un'ora. <br />
 
 - Impostare la memo come ''completata'' o riportarla allo stato ''attiva'' <br />

 - Visualizzare le memo attive, complete o scadute. <br />

 - Visualizzare sulla mappa tutte le notifiche attive. <br />

 - Ricevere una notifica sul dispositivo quando ci si avvicina ad una memo attiva (anche se l'app è chiusa). <br />


## Uno sguardo da vicino
L'applicazione è basata sul modello **MVC**: **M**odel, **V**iew, **C**ontroller.
### Model
>Il *model* si occupa di definire i dati dell'applicazione. <br />

Le classi *Memo* e *MemoList* costituiscono il model dell'applicazione. Ho deciso di usare una classe Forecast per gestire in modo. MemoList permette di ricavare una lista di tutte le memo attive ed ha anche altri metodi utilizzati dalle varie activity.
### View
> Le *view* visualizzano i dati contenuti nel model e "catturano" l'interazione dell'utente con l'applicazione
>
Per quanto riguarda le Views tutte le **activity** dell'applicazione si basano sull'utilizzo di un *ConstraintLayout*, utile per sviluppare un'UI in maniera intuitiva (senza l'utilizzo di views group nidificati).
L'unico **fragment** presente ha un linear layout verticale, vista la semplicità del suo contenuto.
 
### Controller
>I *controller* sono il cervello dell'applicazione: forniscono il collegamento tra view e model, gestiscono gli input dell'utente mandati dalla view e mandano al model eventuali aggiornamenti. 

L'applicazione è formata da 4 **activities** ed un **fragment**, la *comunicazione* tra activities avviente tramite l'utilizzo di **intent**.

 - La **mainActivity** è il punto d'ingresso principale dell'applicazione, è composta da una una *text label* che rappresenta che tipo di memo si sta visualizzando (attiva, completata o scaduta), da 3 *floatingActionButton* utilizzati per accedere alle altre 3 activities e da un **RecyclerViewer** utilizzato per mostrare le note.<br />
Oltre a mostrare le note la MainActivity si occupa di inizializzare il *locationManager* per ottenere la posizione dell'utente e inizializza le  *geofences* delle memo attive.<br />
//immagine mainactivity

 -  L' **addActivity** si occupa dell'aggiunta delle memo, una volta inseriti il *titolo*, una *descrizione*, il *luogo* nei rispettivi *EditText* e la *data* e l'*ora* negli appositi *widget* è possibile creare la memo premendo il tasto apposito.<br />
 Una volta premuto il tasto viene effettuato un controllo per vedere se dei campi sono vuoti e controllare che l'utente abbia inserito una data valida (dal giorno attuale in poi), infine verifica se l'operazione di **geocoding** per ricavare latitudine e longitudine dal luogo inserito è andata a buon fine. <br />
La funzione che si occupa di effettuare tutti questi controlli prende il nome di "*saveInput*" ed è impostata come proprietà *onclick* del FloatingActionButton nel file XML *activity_add*, in caso di campi non validi o mancanti viene chiamata una **ToastNotification** per segnalarlo all'utente.<br />
//immagine addActivity

 - La **detailActivity** viene chiamata nell'override dell'evento *onclick* del **memoAdapter**. Mostra titolo, scadenza, luogo e descrizione della memo selezionata, inoltre è presente un bottone per modificare lo status della memo (attiva/completata),se la memo è scaduta (controllo fatto tramite i due metodi della classe Utils) imposta il TextColor della TextView della data in colore rosso.<br />
 //immagine detailActivity
 
 - La **mapActivity** mostra una mappa,creata tramite l'API map di google, centrata sulla posizione dell'utente.<br />
 L'activity inoltre crea un'annotazione sulla mappa per ogni promemoria dell'ArrayList di memo attive ricevuto dalla mainActivity tramite *Intent*. 
 Quando l'utente clicca su un'annotazione viene mostrato il suo titolo.
 //immagine detailActivity
 
 - L'**infoFragment** è accessibile dalla MainActivity, cliccando sul *menuButton* del punto di domanda, mostra una TextView in cui viene illustrata una guida sul funzionamento dell'app. Nel menu è anche presente un altro tasto per mostrare le memo scadute.

### RecyclerViewer
>La classe RecyclerView semplifica la visualizzazione e la gestione di insiemi di dati di grandi dimensioni, è un contenitore per la visualizzazione di insiemi di dati di grandi dimensioni che è possibile scorrere in modo efficiente mantenendo un numero limitato di Views.

La visualizzazione delle memo avviene tramite **recyclerViewer** della mainActivity. La classe **MemoAdapter** rappresenta l'adattatore della recyclerView, viene utilizzato per fornire al LayoutManager informazioni su quanti elementi devono essere visualizzati e la View effettiva da utilizzare. <br />
In questo progetto tutte le memo sono presenti su unico Adapter dotato di un attributo "*status*" che indica quale tipo di memo mostrare (attive/completate/scadute), in base al bottone che l'utente preme viene impostato uno dei tre stati come attributo dalla MainActivity. <br />
Il controllo per decidere quale tipo di memo visualizzare viene eseguito nell'override del metodo *onBindViewHolder*, in base allo stato viene impostato anche il colore e lo stile del carattere da impostare. (un'alternativa valida a questo tipo di approccio era quella di utilizzare dei fragments per rappresentare le varie tipologie di memo)

### Notifications
