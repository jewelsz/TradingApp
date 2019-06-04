package Messages;

public class AcceptTradeMessage
{
    Boolean accept;

    public AcceptTradeMessage(Boolean accept) {
        this.accept = accept;
    }

    public Boolean getAccept() {
        return accept;
    }
}
