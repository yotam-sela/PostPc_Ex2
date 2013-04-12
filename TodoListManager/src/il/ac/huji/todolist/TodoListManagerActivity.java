package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;
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
	private TodoDAL helper;
	
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
		
		helper = new TodoDAL(this);
		
		Parse.initialize(this, "HBBBzwEoTLWFuwkZfwTz7ypV4D3m7mMd48VhBQvA", "35YHJgV830cK7msfxyqXFEf7kG0MChuEB3ClVwi0"); 
		PushService.subscribe(this, "", TodoListManagerActivity.class);
		PushService.setDefaultPushCallback(this, TodoListManagerActivity.class);

		ParseQuery query = new ParseQuery("todo");
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> objects, ParseException exc) {
				if (exc != null) {
					exc.printStackTrace();
				} else {
					for (ParseObject object : objects) {
						String title = object.getString("title");
						Date dueDate = null;
						if(!object.get("due").equals(null))
						{
							dueDate = new Date(object.getLong("due"));
						}
						TodoHolder todoItem = new TodoHolder(title, dueDate);
						adapter.add(todoItem);
						helper.insertWitoutParser(todoItem);
					}
				}
			}
		}); 
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo info) {
		getMenuInflater().inflate(R.menu.contex_menu, menu);
		
		AdapterContextMenuInfo adapterInfo = (AdapterContextMenuInfo)info;		
		int pos = adapterInfo.position;
		String title = adapter.getItem(pos).getTitle();
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
			TodoHolder tempTodoHolder = adapter.getItem(selectedItemIndex);
			adapter.remove(tempTodoHolder);
			helper.delete(tempTodoHolder);
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
    		TodoHolder tempTodoHolder = new TodoHolder(title, dueDate);
    		
    		if (!title.equals("3"))
    		{
    			adapter.add(tempTodoHolder);
    			helper.insert(tempTodoHolder);
    		}
    		else
    		{
    			helper.update(tempTodoHolder);
    		}
    		
    	}
    	else
    	{
    		Log.d("TodoListManagerActivity","result failed.");
    	}
    }

}
