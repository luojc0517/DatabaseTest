package com.jackie.databasetest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {
    MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnCreateDB).setOnClickListener(this);
        findViewById(R.id.btnAdd).setOnClickListener(this);
        findViewById(R.id.btnDel).setOnClickListener(this);
        findViewById(R.id.btnUp).setOnClickListener(this);
        findViewById(R.id.btnRe).setOnClickListener(this);
        myDatabaseHelper = new MyDatabaseHelper(this, "book.db", null, 2);
    }

    @Override
    public void onClick(View v) {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.btnCreateDB:
                myDatabaseHelper.getWritableDatabase();
                break;
            case R.id.btnAdd:
                ContentValues book = new ContentValues();
                book.put("name", "无声告白");
                book.put("price", 17.15);
                book.put("pages", 290);
                book.put("author", "伍绮诗");
                db.insert("book", null, book);
                book.clear();
                book.put("name", "年少荒唐");
                book.put("price", 18.00);
                book.put("pages", 323);
                book.put("author", "朱炫");
                db.insert("book", null, book);
                break;
            case R.id.btnDel:
                db.delete("book","id=?",new String[]{"4"});
                break;
            case R.id.btnUp:
                ContentValues update = new ContentValues();
                update.put("price", 20.00);
                db.update("book",update,"id=?",new String[]{"1"});
                break;
            case R.id.btnRe:
                Log.v("jackie","查询数据");
                Cursor cursor = db.query("book",null,null,null,null,null,null,null);
                //遍历Cursor，用Log打印数据
                if(cursor.moveToFirst()){
                    do{
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        Log.v("jackie", "name is" + " " + name);
                        Log.v("jackie", "author is" + " " + author);
                        Log.v("jackie", "price is" + " " + price);
                        Log.v("jackie", "pages is" + " " + pages);
                    }while(cursor.moveToNext());
                }
                break;
        }
    }
}
