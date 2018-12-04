package com.guzzipirate.helpdesk;

import java.util.List;

public interface ITicketHandler {
    public Ticket createTicket(Ticket t);

    public Ticket getTicket(int ticketId);

    public List<Ticket> getUserTickets(String uuid);

    public List<Ticket> getEditorTickets(String uuid);

    public int addChangeSet(TicketChangeset cs);

    public int setEditor(int ticketId, String uuid);
}
