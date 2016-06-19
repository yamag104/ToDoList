package yokoy.todolist.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yokoy on 6/11/16.
 */
public class ToDo implements Serializable {
    public int id;
    public String title;
    public boolean completed;
    public Date dueDate;

    public ToDo() {

    }

    public ToDo(int id, String title, boolean completed, Date dueDate) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.dueDate = dueDate;
    }
}
