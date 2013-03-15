package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {

	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);



		Log.d("TodoListManagerActivity","I am here d");
		Log.v("TodoListManagerActivity","I am here v");
		Log.e("TodoListManagerActivity","I am here e");
		Log.i("TodoListManagerActivity","I am here i");

		List<String> todoList = new ArrayList<String>();

		ListView todoListView = (ListView)findViewById(R.id.lstTodoItems);

		adapter = new TodoListDisplayAdapter(this,todoList);
		todoListView.setAdapter(adapter);
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case R.id.menuItemAdd:
			EditText newTaskTodoView = (EditText)findViewById(R.id.edtNewItem);
			String newTaskTodo = newTaskTodoView.getText().toString();
			adapter.add(newTaskTodo);
			break;
		
		case R.id.menuItemDelete:
			ListView todoListView = (ListView)findViewById(R.id.lstTodoItems);
			if(!todoListView.getAdapter().isEmpty())
			{
				int selectedItemIndex = todoListView.getSelectedItemPosition();
				if(selectedItemIndex != AdapterView.INVALID_POSITION)
				{
					adapter.remove(adapter.getItem(selectedItemIndex));
				}
			}
			break;
		}
		return true;
	}

}
