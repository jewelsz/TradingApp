package Messages;

import Enums.MessageOperation;

public class CommunicatorWebsocketMessage
{
    // Operation that is requested at client side
    private MessageOperation operation;

    // Property
    private String property;

    // Content
    private String content;

    public MessageOperation getOperation() {
        return operation;
    }

    public void setOperation(MessageOperation operation) {
        this.operation = operation;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
