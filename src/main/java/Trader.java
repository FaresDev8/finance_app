public class Trader {

    private int traderId;
    private String traderName;
    private int credit;


    public Trader(int traderId, String traderName, int credit) {
        this.traderId = traderId;
        this.traderName = traderName;
        this.credit = credit;
    }

    public int getTraderId() {
        return traderId;
    }

    public void setTraderId(int traderId) {
        this.traderId = traderId;
    }

    public String getTraderName() {
        return traderName;
    }

    public void setTraderName(String traderName) {
        this.traderName = traderName;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String tradingAction(String quote){
        String action = "None";
        int quoteInt = Integer.parseInt(quote);

        if (quoteInt < this.credit / 10){
            action = "Buy";
            this.credit -= quoteInt;
        }
        else {
            action = "Sell";
            this.credit += quoteInt;
        }
        return action;
    }

}
