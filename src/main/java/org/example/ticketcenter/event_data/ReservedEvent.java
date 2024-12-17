package org.example.ticketcenter.event_data;

public class ReservedEvent {
    private static ReservedEvent instance;
    private EventData eventData;

    public EventData getEventData() {
        return eventData;
    }

    public void setEventData(EventData eventData) {
        this.eventData = eventData;
    }

    public static ReservedEvent getInstance(){
        if(instance==null){
            instance=new ReservedEvent();
        }
        return instance;
    }
}
