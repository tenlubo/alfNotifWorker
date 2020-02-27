package online.fixu.bsp.alf.alfnotifworker.spring.json;

public class LoginTicket {

    private LoginEntry entry;

    public LoginTicket() {
    }

    public LoginTicket(String statusText, String localizedMessage) {
        entry = new LoginEntry();
        entry.setId(statusText);
        entry.setUserId(localizedMessage);
    }

    public LoginEntry getEntry() {
        return entry;
    }

    public void setEntry(LoginEntry entry) {
        this.entry = entry;
    }
}
