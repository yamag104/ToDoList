package yokoy.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Utils.setupTouchListener(this, findViewById(R.id.parent));
        editText = (EditText) findViewById(R.id.editText);
        String initialToDoText = getIntent().getStringExtra("initialToDo");
        editText.setText(initialToDoText);
    }

    public void onSave(View v) {
        String finalToDoText = editText.getText().toString();
        // Prepare data intent
        Intent data = new Intent();
        // Pass edited to do list back as a result
        data.putExtra("finalToDo", finalToDoText);
        // Activity finished ok, return the data
        // set result code and bundle data for response
        setResult(RESULT_OK, data);
        // closes the activity and returns to first screen
        this.finish();
    }
}
