package yokoy.todolist.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import yokoy.todolist.R;
import yokoy.todolist.models.ToDo;
import yokoy.todolist.utils.Utils;

public class EditItemActivity extends AppCompatActivity {

    EditText editText;
    TextView duedateTextView;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Utils.setupTouchListener(this, findViewById(R.id.parent));
        editText = (EditText) findViewById(R.id.editText);
        String initialToDoText = getIntent().getStringExtra("initialToDo");
        editText.setText(initialToDoText);

        duedateTextView = (TextView) findViewById(R.id.duedateTextView);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        duedateTextView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    public void onSave(View v) {
        String finalToDoText = editText.getText().toString();
        Date date = new Date();
        try {
            String dateText = duedateTextView.getText().toString();
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            date = format.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ToDo editedToDo = new ToDo();
        editedToDo.title = finalToDoText;
        editedToDo.dueDate = date;

        // Prepare data intent
        Intent data = new Intent();
        // Pass edited to do list back as a result
        data.putExtra("ToDo", editedToDo);
        // Activity finished ok, return the data
        // set result code and bundle data for response
        setResult(RESULT_OK, data);
        // closes the activity and returns to first screen
        this.finish();
    }
}
