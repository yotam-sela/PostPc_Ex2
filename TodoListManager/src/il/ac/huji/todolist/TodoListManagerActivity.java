package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {

	private ArrayAdapter<TodoHolder> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_todo_list_manager);
	

		List<TodoHolder> todoList = new ArrayList<TodoHolder>();

		ListView todoListView = (ListView)findViewById(R.id.lstTodoItems);

		adapter = new TodoListDisplayAdapter(this,todoList);
		todoListView.setAdapter(adapter);

		registerForContextMenu(todoListView);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo info) {
		getMenuInflater().inflate(R.menu.contex_menu, menu);
		
		AdapterContextMenuInfo adapterInfo = (AdapterContextMenuInfo)info;		
		int pos = adapterInfo.position;
		String title = adapter.getItem(pos).title;
		menu.setHeaderTitle(title);

		if(title.startsWith("Call "))
		{
		    MenuItem item = menu.getItem(1);
		    item.setTitle(title);
		}
		else
		{	
			menu.removeItem(R.id.menuItemCall);	
		}
		
		
		
		
	}
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)
				item.getMenuInfo();
		int selectedItemIndex = info.position;
		switch (item.getItemId()){
		case R.id.menuItemDelete:
			adapter.remove(adapter.getItem(selectedItemIndex));
			break;
		case R.id.menuItemCall:
			Log.d("onContextItemSelected","Call, not supporeted currently"); //TODO: add support.
			String actionDialTxt = (String) item.getTitle();
			actionDialTxt = actionDialTxt.substring("Call ".length());
			actionDialTxt = "tel:"+actionDialTxt;
			Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse(actionDialTxt));
			startActivity(dial);
			break;
		}
		return true;
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

			Intent intent = new Intent(this, AddNewTodoItemActivity.class);
    		startActivityForResult(intent, 1337);
    		break;
		}
		return true;
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == 1337 && resultCode == RESULT_OK) {
    		String title = data.getStringExtra("title");
    		java.util.Date dueDate = (Date) data.getSerializableExtra("dueDate");
    		adapter.add(new TodoHolder(title, dueDate));
    	}
    	else
    	{
    		Log.d("TodoListManagerActivity","result failed.");
    	}
    }

}
