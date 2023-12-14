# Memorandum
Project for the Android part of the MobDev course, consists of an application for managing memos in *java* made with *Android studio*.
## Key features and application examples
With the **Memorandum** app you can:

 - Add memos with a title, description, place, date, and time. <br />
 <img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/Addactivtygif.gif width="25%" height="25%">
 
 - Set the memo to ''completed'' or return it to ''active'' state <br />
 <img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/Detailactivitygif.gif width="25%" height="25%">

 - View active, complete, or expired memos. <br />
 <img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/mainactivirtgif.gif width="25%" height="25%">

 - View all active reminders on the map. <br />
 <img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/MapActivitygif.gif width="25%" height="25%">
 
 - Receive a notification on your device when you approach an active memo (even if the app is closed). <br />
 <img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/Notificationgif.gif width="25%" height="25%">

## A close look on it
The application is based on the model **MVC**: **M**odel, **V**iew, **C**ontroller.
### Model
>The *model* is responsible for defining the application data. <br />

The *Memo* and *MemoList* classes are the application model. MemoList allows you to derive a list of all active memos and also has other methods used by the various activities.
### View
> *Views* display the data contained in the model and 'capture' the user's interaction with the application
>
As for Views, all the **activity** of the application are based on the use of a *ConstraintLayout*, useful for developing a UI in an intuitive way (without the use of nested views groups).

The only **fragment** present has a linear vertical layout, given the simplicity of its content.
 
### Controller
>*Controllers* are the brain of the application: they provide the link between view and model, manage user inputs sent from the view, and send any updates to the model.

The application consists of 4 **activities** and a **fragment**, the *communication* between activities starting through the use of **intent**.

 - The **mainActivity** is the main entry point of the application, consisting of a *text label* representing what type of memo you are viewing (active, completed or expired), 3 *floatingActionButtons* used to access the other 3 activities, and a **RecyclerViewer** used to display notes.<br />

In addition to showing notes, MainActivity takes care of initialising the *locationManager* to obtain the user's location and initialising the *geofences* of active memos. Whenever the mainActivity "*onPause*" method is called (that is, when other activities are opened, or the app is left in the background), the "*locationManager.removeUpdates()*" method is called to stop using the user's location to avoid unnecessary battery consumption of the device.<br />
<img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/MainActivity.png width="25%" height="25%">

 -  The **addActivity** takes care of adding the memos, once you have entered the *title*, a *description*, the *place* in the respective *EditText* and the *date* and the *time* in the appropriate *widgets* you can create the memo by pressing the appropriate button.<br />

Once the key is pressed, a check is made to see if any fields are empty, check that the date entered by the user is valid (from the current day onwards) and finally check if the **geocoding** operation to derive latitude and longitude from the place entered has been successful. <br />

The function that carries out all these checks is called '*saveInput*' and is set as the *onclick* property of the FloatingActionButton in the *activity_add* XML file, in case of invalid or missing fields a **ToastNotification** is called to report it to the user.<br />

<img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/AddActivity.png width="25%" height="25%">

 - The **detailActivity** is called in the **memoAdapter** *onclick* event override. Shows title, expiration, location and description of the selected memo, also there is a button to change the status of the memo (active/completed), if the memo has expired (check done using the two methods of the Utils class) sets the TextColor of the TextView of the date to red.<br />
<img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/DetailActivity.png width="25%" height="25%">
 
 - The **mapActivity** shows a map,created through the google map API, centred on the user's location.<br />

The activity also creates an annotation on the map for each reminder of the ArrayList of active memos received by the mainActivity via *Intent*.

When the user clicks on an annotation its title is shown.<br />
<img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/MapActivity.png width="25%" height="25%">
 
 - The **infoFragment** is accessible from the MainActivity, by clicking on the *menuButton* of the question mark, it shows a TextView that shows a guide on how the app works. There is also another button in the menu to show expired memos.<br />
<img src=https://github.com/GRicciardi00/MobDev-Giuseppe-Ricciardi-Android/blob/main/Screenshots/Fragment.png width="25%" height="25%">

### RecyclerViewer
>The RecyclerView class makes it easy to view and manage large data sets, it is a container for displaying large data sets that you can efficiently scroll through while maintaining a limited number of Views.

Memo displays are via the mainActivity **recyclerViewer**. The **MemoAdapter** class represents the recyclerView adaptor, it is used to provide the LayoutManager with information on how many items should be displayed and the actual View to be used. <br />

In this project, all memos are present on a single Adaptor with a '*status*' attribute that indicates what type of memo to show (active/completed/expired), depending on the button the user presses, one of the three states is set as the attribute by the MainActivity. <br />

The control to decide which type of memo to display is performed in the override of the *onBindViewHolder* method, depending on the status the colour and style of the font to be set is also set, all memos are shown in order of date. <br />
**NB** : An avaiable alternative to this type of approach is to use fragments with different memoadapters to represent the various types of memos.

### Notifications & Geofencing
The classes that create geofences and manage the sending of notifications are: *GeofenceHelper*,*Notification Helper*, *NotificationBroadcast receiver*.

- **GeofenceHelper** includes all the methods useful for creating geofences (via Google API), used in the mainActivity. User authorisation is required to use geofences for: ACCESS_BACKGROUND_LOCATION. Each GeoFenceHelper has an associated memo that is used by the 'getPendingIntent' method to obtain the memo information and pass it to the **GeoFencingClient** who takes care of the geofence registration.

- **NotificationBroadCastReceiver** retrieves information passed to the **GeoFencingClient** and calls the ''*sendHighPriorityNotification*'' method of the **NotificationHelper** class to send a notification.

- **NotificationHelper** manages all *NotificationManager* operations, creates a notification receiving channel and sends notifications to that channel.
