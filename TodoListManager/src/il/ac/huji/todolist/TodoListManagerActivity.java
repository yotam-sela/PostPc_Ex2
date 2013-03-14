package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {

	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		//setContentView(R.menu.todo_list_manager);

		List<String> todoList = new ArrayList<String>();

		ListView todoListView = (ListView)findViewById(R.id.lstTodoItems);

		adapter = new TodoListDisplayAdapter(this,todoList);
		todoListView.setAdapter(adapter);

//		findViewById(R.id.menuItemAdd).setOnClickListener(
//				new OnClickListener(){
//					
//					@Override
//					public void onClick(View v) {
//						EditText newTaskTodoView = (EditText)findViewById(R.id.edtNewItem);
//						String newTaskTodo = newTaskTodoView.getText().toString();
//						adapter.add(newTaskTodo);
//					}
//
//				});

	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		System.out.println("item.getItemId(): "+ item.getItemId());
		//switch (item.getItemId())
		return true;
	}

}
