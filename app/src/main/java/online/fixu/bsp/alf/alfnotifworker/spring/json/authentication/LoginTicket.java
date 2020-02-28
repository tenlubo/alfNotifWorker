package online.fixu.bsp.alf.alfnotifworker.spring.json.authentication;

public class LoginTicket {

    private LoginEntry entry;

    public LoginTicket() {
    }

    public LoginEntry getEntry() {
        return entry;
    }

    public void setEntry(LoginEntry entry) {
        this.entry = entry;
    }
}
