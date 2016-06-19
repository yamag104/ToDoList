package yokoy.todolist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yokoy.todolist.R;
import yokoy.todolist.models.ToDo;

/**
 * Created by yokoy on 6/11/16.
 */
public class ToDoAdapter extends ArrayAdapter<ToDo> {

    public ToDoAdapter(Context context, List<ToDo> todos) {
        super(context, 0, todos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ToDo toDo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }
        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.title);
        // Populate the data into the template view using the data object
        title.setText(toDo.title);
        TextView duedateTextView = (TextView) convertView.findViewById(R.id.duedate_textview);
        if (toDo.dueDate != null) {
            String formattedDate = new SimpleDateFormat("dd/MM/yyyy, Ka").format(toDo.dueDate);
            duedateTextView.setText(formattedDate);
        }
        // Return the completed view to render on screen
        return convertView;
    }

}
