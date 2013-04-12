package il.ac.huji.todolist;

import java.util.Date;
import java.util.List;

import org.json.JSONObject;

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
	private static final String PARSE_TABLE_NAME = "todo";

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

		ParseObject parseObject = new ParseObject(PARSE_TABLE_NAME);
		parseObject.put("title", todoItem.getTitle());
		if(todoItem.getDueDate() == null){
			parseObject.put("due", JSONObject.NULL);
		}else{
			parseObject.put("due", todoItem.getDueDate().getTime());
		}


		parseObject.saveInBackground();

		return true;
	}

	public boolean update(ITodoItem todoItem) {
		ContentValues value = new ContentValues();
		value.put("title", todoItem.getTitle());
		value.put("due", (int)todoItem.getDueDate().getTime());

		String[] whereClauseArgument = {todoItem.getTitle()};
		boolean output =  db.update(DB_TABLE_NAME, value, "title" + "=?" , whereClauseArgument) > 0;
		if(output)
		{
			final Date dueDate = todoItem.getDueDate();
			
			ParseQuery query = new ParseQuery(PARSE_TABLE_NAME);
			query.whereStartsWith("title", todoItem.getTitle());
			query.findInBackground(new FindCallback(){

				@Override
				public void done(List<ParseObject> objects, ParseException e) {
					objects.get(0).remove("due");
					objects.get(0).deleteInBackground();
					
					if(dueDate == null){
						objects.get(0).put("due", JSONObject.NULL);
					}else{
						objects.get(0).put("due", dueDate.getTime());
					}


					objects.get(0).saveInBackground();
					
				}
			});
			
			
			

			ParseObject parseObject = new ParseObject(PARSE_TABLE_NAME);
			parseObject.remove(todoItem.getTitle());
			parseObject.put("title", todoItem.getTitle());
			if(todoItem.getDueDate() == null){
				parseObject.put("due", JSONObject.NULL);
			}else{
				parseObject.put("due", todoItem.getDueDate().getTime());
			}
			parseObject.saveInBackground();
		}
		return output;
	}

	public boolean delete(ITodoItem todoItem) {
		String[] whereClauseArgument = {todoItem.getTitle()};
		boolean output =  db.delete(DB_TABLE_NAME, "title =?", whereClauseArgument) > 0;

		if(output)
		{
			ParseQuery query = new ParseQuery(PARSE_TABLE_NAME);
			query.whereStartsWith("title", todoItem.getTitle());
			query.findInBackground(new FindCallback(){

				@Override
				public void done(List<ParseObject> objects, ParseException e) {
					objects.get(0).remove("title");
					objects.get(0).remove("due");
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
