package il.ac.huji.todolist;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_new_todo_item);
		
		
		findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				EditText edtTodoName = (EditText)findViewById(R.id.edtNewItem);
				String title = edtTodoName.getText().toString();
				
				DatePicker edtTodoDate = (DatePicker)findViewById(R.id.datePicker);
				Calendar tempCalender = Calendar.getInstance();
				tempCalender.clear();
				tempCalender.set(edtTodoDate.getYear(),edtTodoDate.getMonth(), edtTodoDate.getDayOfMonth());
				java.util.Date dueDate = tempCalender.getTime();
				
				if (title == null || "".equals(title)) {
					setResult(RESULT_CANCELED);
					finish();
				} else {
					Intent result = new Intent();
					result.putExtra("title", title);
					result.putExtra("dueDate", dueDate);
					setResult(RESULT_OK, result);
					finish();
				}
			}
		});
	}


}
