package org.example.ticketcenter.event_distributor_data;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import org.example.ticketcenter.event_data.Event;
import org.example.ticketcenter.user_factory.models.Distributor;
import org.example.ticketcenter.user_factory.models.Organiser;

public class EventDistributorData {

    private Event event;

    private Organiser organiser;

    private Button accept, decline;
    private HBox pane;
    private String organiserName;

    private String eventName;

    public EventDistributorData(Event event, Organiser organiser) {
        this.event = event;
        this.organiser = organiser;
        this.organiserName = organiser.getName();
        this.eventName = event.getName();
        this.accept = new Button("Accept");
        this.decline = new Button("Decline");
        this.pane = new HBox(accept,decline);

    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
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
