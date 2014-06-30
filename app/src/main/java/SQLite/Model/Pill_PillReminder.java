package SQLite.Model;

public class Pill_PillReminder {

   private Pill pill;
   private PillReminder pillReminder;

    public Pill_PillReminder() {
    }

    public Pill_PillReminder(Pill pill, PillReminder pillReminder) {
        this.pill = pill;
        this.pillReminder = pillReminder;
    }

    public Pill getPill() {
        return pill;
    }

    public void setPill(Pill pill) {
        this.pill = pill;
    }

    public PillReminder getPillReminder() {
        return pillReminder;
    }

    public void setPillReminder(PillReminder pillReminder) {
        this.pillReminder = pillReminder;
    }
}
