import java.util.Random;
public class Company {

    private String companyName;
    private int companyQuote;

    public Company(String companyName, int companyQuote) {
        this.companyName = companyName;
        this.companyQuote = companyQuote;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getCompanyQuote() {
        return companyQuote;
    }

    public void setCompanyQuote(int companyQuote) {
        this.companyQuote = companyQuote;
    }

    public void updateQuote(){
        Random random = new Random();
        int amount = random.nextInt((10 - 1) + 1) + 1;
        int change = random.nextInt((1 - 0) + 1) + 0;

        if(change == 1){
            this.companyQuote += amount;
        }
        else{
            this.companyQuote -= amount;
        }
    }

}
