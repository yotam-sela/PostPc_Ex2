package il.ac.huji.todolist;

import java.util.Date;
import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TodoDAL extends SQLiteOpenHelper {

	private static final String DATA_BASE_NAME = "todo_db";
	private static final String DB_TABLE_NAME = "todo";

	private SQLiteDatabase db;
	//private Cursor cursor;

	public TodoDAL(Context context){
		super(context, DATA_BASE_NAME, null, 1);
		db = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(
				"create table " + DB_TABLE_NAME + 
				" ( _id integer primary key autoincrement,"
				+  " title text," +
				" due integer );");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Nothing to do.
	}

	public boolean insert(ITodoItem todoItem) {
		ContentValues values = new ContentValues();
		values.put("title", todoItem.getTitle());
		values.put("due", (int)todoItem.getDueDate().getTime());
		db.insert(DB_TABLE_NAME, null, values);

		ParseObject parseObject = new ParseObject("todo");
		parseObject.put("title", todoItem.getTitle());
		parseObject.put("due", todoItem.getDueDate().getTime());
		parseObject.saveInBackground();

		return true;
	}

	public boolean update(ITodoItem todoItem) {
		ContentValues value = new ContentValues();
		value.put("title", todoItem.getTitle());
		value.put("due", (int)todoItem.getDueDate().getTime());

		boolean output =  db.update(DB_TABLE_NAME, value, "title" + "=" + todoItem.getTitle(), null) > 0;
		if(output)
		{

			ParseObject parseObject = new ParseObject("todo");
			parseObject.remove(todoItem.getTitle());
			parseObject.put("title", todoItem.getTitle());
			parseObject.put("due", todoItem.getDueDate().getTime());
			parseObject.saveInBackground();
		}
		return output;
	}

	public boolean delete(ITodoItem todoItem) {
		String[] whereClauseArgument = {todoItem.getTitle()};
		boolean output =  db.delete(DB_TABLE_NAME, "title =?", whereClauseArgument) > 0;
		
		final Date dueDate = todoItem.getDueDate();

		if(output)
		{
			//	cursor.requery();
			ParseQuery query = new ParseQuery("todo");
			query.whereStartsWith("title", "");
			query.findInBackground(new FindCallback(){

				@Override
				public void done(List<ParseObject> objects, ParseException e) {
					objects.get(0).remove("title");
					if(dueDate != null)
					{
						objects.get(0).remove("due");
					}
					objects.get(0).deleteInBackground();
				}
			});
		}
		return output;

	}
	
		public List<ITodoItem> all() {
			return null;
		}
	}
