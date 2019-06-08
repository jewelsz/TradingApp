package Models;

public class TradeResponse
{
    boolean check;

    public TradeResponse(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
