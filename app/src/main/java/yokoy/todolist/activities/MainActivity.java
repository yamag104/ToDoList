package yokoy.todolist.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yokoy.todolist.R;
import yokoy.todolist.adapters.ToDoAdapter;
import yokoy.todolist.models.ToDo;
import yokoy.todolist.models.TodoItemDatabase;
import yokoy.todolist.utils.Utils;

public class MainActivity extends AppCompatActivity {

    // REQUEST_CODE can be any value we like, used to determine the result type later
    private final int REQUEST_CODE = 20;

    TodoItemDatabase database;
    List<ToDo> todoItems;
    ToDoAdapter adapter;
    ListView lvItems;
    EditText etEditText;
    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        Utils.setupTouchListener(this, findViewById(R.id.parent));
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(adapter);
        etEditText = (EditText) findViewById(R.id.etEditText);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                adapter.notifyDataSetChanged();
                ToDo deleteToDo = todoItems.get(position);
                todoItems.remove(position);
                database.deleteToDo(deleteToDo);
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToDo selectedToDo = (ToDo) lvItems.getItemAtPosition(position);
                String clickedText = selectedToDo.title;
                currentPosition = position;
                launchEditItemView(clickedText);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String finalToDo = data.getExtras().getString("finalToDo");
            editItem(finalToDo, currentPosition);
        }
    }

    public void editItem(String finalToDo, int position) {
        ToDo currentTodo = todoItems.get(position);
        ToDo updatedTodo = new ToDo(currentTodo.id, finalToDo, false);
        todoItems.remove(position);
        todoItems.add(position, updatedTodo);
        database.updateToDo(updatedTodo);
        adapter.notifyDataSetChanged();
    }

    public void launchEditItemView(String clickedText) {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        // put "extras" into the bundle for access in the second activity
        i.putExtra("initialToDo", clickedText);
        // brings up the second activity
        startActivityForResult(i, REQUEST_CODE);
    }

    public void populateArrayItems() {
        todoItems = new ArrayList<ToDo>();
        database = TodoItemDatabase.getInstance(this);
        todoItems = database.getAllToDos();
        adapter = new ToDoAdapter(this, todoItems);
        adapter.addAll(todoItems);
    }

    public void onAddItem(View view) {
        ToDo newTodo = new ToDo(0, etEditText.getText().toString(), false);
        adapter.add(newTodo);
        etEditText.setText("");
        database.addToDo(newTodo);
    }
}
