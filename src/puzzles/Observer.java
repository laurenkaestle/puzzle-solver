package puzzles;

public interface Observer<Subject, ClientData> {
    void update(Subject subject, ClientData data);
}
