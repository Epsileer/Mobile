package com.example.zues.healthok.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.zues.healthok.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class GCMRequestData {
    private static GCMRequestData gcmRequestData;
    public String message;
    Db d;

    private GCMRequestData()
    {
        message = "f";
    }


  public static GCMRequestData getInstance()
  {
      if(gcmRequestData == null)
      {
          gcmRequestData = new GCMRequestData();
      }

      return gcmRequestData;
  }



    public String getMessage() {
        return message;

    }

    public void setMessage(String message) {

        this.message = message;

    }

}

