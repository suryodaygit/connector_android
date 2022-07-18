package com.example.logintask.dbclass

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "RegistrationDatabase"
        private val TABLE_REGISTRATION = "RegistrationTable1"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
        private val KEY_MOBILE = "mobile"
        private val KEY_PASS = "password"
        private val KEY_GST_NO = "gstNo"
        private val KEY_PAN_CARD_NO = "panCardNo"
        private val KEY_COMPANY_NAME = "companyName"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_REGISTRATION + "("
                + KEY_NAME + " TEXT," + KEY_EMAIL + " TEXT,"
                + KEY_MOBILE + " TEXT," + KEY_PASS + " TEXT," + KEY_GST_NO + " TEXT,"
                + KEY_PAN_CARD_NO + " TEXT," + KEY_COMPANY_NAME + " TEXT,"+  ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTRATION)
        onCreate(db)
    }

    //method to insert data
    fun addCustomer(emp: RegistrationModel):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.userName)
        contentValues.put(KEY_EMAIL,emp.userEmail )
        contentValues.put(KEY_MOBILE, emp.userMobile)
        contentValues.put(KEY_PASS,emp.userPassword )
        val success = db.insert(TABLE_REGISTRATION, null, contentValues)
        db.close()
        return success
    }

    //method to read data
    @SuppressLint("Range")
    fun viewEmployee():List<RegistrationModel>{
        val empList:ArrayList<RegistrationModel> = ArrayList<RegistrationModel>()
        val selectQuery = "SELECT  * FROM $TABLE_REGISTRATION"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var userName: String
        var userEmail: String
        var userMobile: String
        var userPass: String

        if (cursor.moveToFirst()) {
            do {
                userName = cursor.getString(cursor.getColumnIndex("name"))
                userEmail = cursor.getString(cursor.getColumnIndex("email"))
                userMobile = cursor.getString(cursor.getColumnIndex("mobile"))
                userPass =  cursor.getString(cursor.getColumnIndex("pass"))

                val customer= RegistrationModel(userName = userName, userEmail = userEmail, userMobile = userMobile,userPass)
                empList.add(customer)
            } while (cursor.moveToNext())
        }
        return empList
    }
    //method to update data
    fun updateEmployee(emp: RegistrationModel):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.userName)
        contentValues.put(KEY_EMAIL,emp.userEmail )
        contentValues.put(KEY_MOBILE,emp.userMobile)
        contentValues.put(KEY_PASS,emp.userPassword )

        // Updating Row
        val success = db.update(TABLE_REGISTRATION, contentValues,"name="+emp.userName,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}