package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class TodoDAL extends SQLiteOpenHelper {

	private static final String DATA_BASE_NAME = "todo_db";
	private static final String DB_TABLE_NAME = "todo";
	private static final String PARSE_TABLE_NAME = "todo";
	private static final String KEY_ID = "_id";
	private static final String TITLE = "title";
	private static final String DUE = "due";

	private SQLiteDatabase db;

	public TodoDAL(Context context){
		super(context, DATA_BASE_NAME, null, 1);
		db = getWritableDatabase();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(
				"create table " + DB_TABLE_NAME + 
				" ( "+KEY_ID+" integer primary key autoincrement,"
				+  " "+TITLE+" text," +
				" "+DUE+" integer );");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Nothing to do.
	}

	public boolean insert(ITodoItem todoItem) {
		ContentValues values = new ContentValues();
		values.put(TITLE, todoItem.getTitle());
		values.put(DUE, todoItem.getDueDate().getTime());
		db.insert(DB_TABLE_NAME, null, values);

		ParseObject parseObject = new ParseObject(PARSE_TABLE_NAME);
		parseObject.put(TITLE, todoItem.getTitle());
		if(todoItem.getDueDate() == null){
			parseObject.put(DUE, JSONObject.NULL);
		}else{
			parseObject.put(DUE, todoItem.getDueDate().getTime());
		}
		parseObject.saveInBackground();
		return true;
	}

	public boolean update(ITodoItem todoItem) {
		ContentValues value = new ContentValues();
		value.put(TITLE, todoItem.getTitle());
		value.put(DUE, todoItem.getDueDate().getTime());

		String[] whereClauseArgument = {todoItem.getTitle()};
		boolean output =  db.update(DB_TABLE_NAME, value, TITLE + "=?" , whereClauseArgument) > 0;
		if(output){
			final Date dueDate = todoItem.getDueDate();

			ParseQuery query = new ParseQuery(PARSE_TABLE_NAME);
			query.whereStartsWith(TITLE, todoItem.getTitle());
			query.findInBackground(new FindCallback(){

				@Override
				public void done(List<ParseObject> objects, ParseException e) {
					objects.get(0).remove(DUE);
					objects.get(0).deleteInBackground();

					if(dueDate == null){
						objects.get(0).put(DUE, JSONObject.NULL);
					}else{
						objects.get(0).put(DUE, dueDate.getTime());
					}
					objects.get(0).saveInBackground();
				}
			});
			ParseObject parseObject = new ParseObject(PARSE_TABLE_NAME);
			parseObject.remove(todoItem.getTitle());
			parseObject.put(TITLE, todoItem.getTitle());
			if(todoItem.getDueDate() == null){
				parseObject.put(DUE, JSONObject.NULL);
			}else{
				parseObject.put(DUE, todoItem.getDueDate().getTime());
			}
			parseObject.saveInBackground();
		}
		return output;
	}

	public void deleteAll()
	{
		db.delete(DB_TABLE_NAME, null, null);
	}


	public boolean delete(ITodoItem todoItem) {
		String[] whereClauseArgument = {todoItem.getTitle(),todoItem.getDueDate().getTime()+""};
		boolean output =  db.delete(DB_TABLE_NAME, "title =? AND due =?", whereClauseArgument) > 0;

		if(output)
		{
			ParseQuery query = new ParseQuery(PARSE_TABLE_NAME);
			query.whereStartsWith(TITLE, todoItem.getTitle());
			query.findInBackground(new FindCallback(){

				@Override
				public void done(List<ParseObject> objects, ParseException e) {
					objects.get(0).remove(TITLE);
					objects.get(0).remove(DUE);
					objects.get(0).deleteInBackground();
				}
			});
		}
		return output;
	}

	public List<ITodoItem> all() {
		Cursor curser = db.query(DB_TABLE_NAME, new String[] { KEY_ID, TITLE,
				DUE }, null, null, null, null, null);
		List<ITodoItem> output = new ArrayList<ITodoItem>();
		if (curser.moveToFirst()) {
			do {
				String title = curser.getString(1);
				Date dueDate = new Date(curser.getLong(2));
				TodoHolder todoHolder = new TodoHolder(title,dueDate);
				output.add(todoHolder);	
			} while (curser.moveToNext());
		}
		return output;
	}

	/**
	 * used for init the db to add the data from the parser.
	 * @param todoItem
	 */
	public void insertWitoutParser(TodoHolder todoItem) {
		ContentValues values = new ContentValues();
		values.put(TITLE, todoItem.getTitle());
		values.put(DUE, todoItem.getDueDate().getTime());
		
		List<ITodoItem> dbList = this.all();
		boolean contains = true;
		for (ITodoItem iTodoItem : dbList) {
			if(iTodoItem.getTitle().equals(todoItem.getTitle()))
			{
				contains = true;
				break;
			}
		}
		
		if(contains)
		{
			Log.i("insertWitoutParse: ","title: "+todoItem.getTitle()+ " already exists.");
		}
		else{
			db.insert(DB_TABLE_NAME, null, values);
		}
	}
}
