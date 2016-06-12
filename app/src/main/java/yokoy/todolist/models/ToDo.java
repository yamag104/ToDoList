package yokoy.todolist.models;

/**
 * Created by yokoy on 6/11/16.
 */
public class ToDo {
    public int id;
    public String title;
    public boolean completed;

    public ToDo() {

    }

    public ToDo(int id, String title, boolean completed) {
        this.id = id;
        this.title = title;
        this.completed = completed;
    }
}
