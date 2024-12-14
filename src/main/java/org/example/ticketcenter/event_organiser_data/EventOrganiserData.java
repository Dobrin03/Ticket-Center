package org.example.ticketcenter.event_organiser_data;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import org.example.ticketcenter.event_data.EventData;
import org.example.ticketcenter.user_factory.models.Organiser;

public class EventOrganiserData {
    private EventData eventData;
    private Organiser organiser;
    private Button accept, decline;
    private HBox pane;
    private String organiserName;
    private String eventName;
    private static EventOrganiserData instance;

    public static EventOrganiserData getInstance(){
        if(instance==null){
            instance=new EventOrganiserData();
        }
        return instance;
    }
    private EventOrganiserData(){

    }

    public EventOrganiserData(EventData eventData, Organiser organiser) {
        this.eventData = eventData;
        this.organiser = organiser;
        this.organiserName = organiser.getName();
        this.eventName = eventData.getName();
        this.accept = new Button("Accept");
        this.decline = new Button("Decline");
        this.pane = new HBox(accept,decline);

    }

    public EventData getEvent() {
        return eventData;
    }

    public void setEvent(EventData eventData) {
        this.eventData = eventData;
    }

    public Organiser getOrganiser() {
        return organiser;
    }

    public String getOrganiserName() {
        return organiserName;
    }

    public void setOrganiserName(String organiserName) {
        this.organiserName = organiserName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setOrganiser(Organiser organiser) {
        this.organiser = organiser;
    }

    public Button getAccept() {
        return accept;
    }

    public void setAccept(Button accept) {
        this.accept = accept;
    }

    public Button getDecline() {
        return decline;
    }

    public void setDecline(Button decline) {
        this.decline = decline;
    }

    public HBox getPane() {
        return pane;
    }

    public void setPane(HBox pane) {
        this.pane = pane;
    }
}
