package it.unisalento.myfood.View;

import javax.swing.*;

public class UtentePanel extends JPanel {

    // TODO mostra le informazioni dell'utente all'interno di TextLabel non modificabili (come per esempio la sezione commenti)
    //  in basso ci sarà il tasto di modifica che abiliterà la modifica. In questo modo abbiamo un unico Panel per modifica e visualizzazione
    //  usare lo stesso Panel con i campi vuoti per l'aggiunta (dovrebbe esserci una JCheckBox per selezionare se aggiungere un Cliente-Admin-Cucina.
    //  Se viene fatto così, queste checkbox verranno visualizzate sempre, ma non sono mai modificabili, tranne che conl'aggiunta.
    //  Se viene selezionata la checkbox del cliente deve apparire la Label "Residenza"
    //  .
    //  In questo modo con UtentePanel facciamo tutti i casi d'uso (Aggiunta, Modifica, Visualizzazione)
    //  Inoltre quando si entra in UtentePanel in modalità di Visualizzazione ci saranno al BorderLayout.SOUTH i tasti di PulsantieraGestioneUtenti.
    //  Per modificare, disabilitare/abilitare, rimuovere l'utente
    //  Se invece si entra in UtentePanel in modalità di Aggiunta devono uscire i tasti "Crea Utente" e "Annulla"

}
