package com.mobile.ddd

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.util.ArrayList


public class DBHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val appContext: Context = context.applicationContext
        companion object {
            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "veritabani.db"
        }

        override fun onCreate(db: SQLiteDatabase?) {
            val CreateCountries = appContext.getString(R.string.Create_Countries)
            val CreateCities = appContext.getString(R.string.Create_Cities)
            val CreateGezi = appContext.getString(R.string.Create_Gezi)
            val CreateGeziGaleri = appContext.getString(R.string.Create_GeziGaleri)

            val assets = appContext.assets
            val inputStreamCountryCity = assets.open("CountryCity.sql")
            val CountryCity = inputStreamCountryCity.bufferedReader().use { it.readText() }
            val CountryCityLines = CountryCity.split(";")

            db?.execSQL(CreateCountries)
            db?.execSQL(CreateCities)
            for (line in CountryCityLines) {
                if (line.isNotBlank()) db?.execSQL(line)
            }
            db?.execSQL(CreateGezi)
            db?.execSQL(CreateGeziGaleri)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        }

        public fun getDatabase(): SQLiteDatabase {
            return readableDatabase
        }


        public fun getCountries(): ArrayList<String> {
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT country FROM countries;", null)
            val countries = ArrayList<String>()
            if (cursor.moveToFirst()) {
                do {
                    countries.add(cursor.getString(cursor.getColumnIndexOrThrow("country")))
                } while (cursor.moveToNext())
            }
            cursor.close()
            countries.sort()
            return countries
        }


        public fun getCities(country: String): ArrayList<String> {
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT city FROM cities WHERE country = '$country'", null)
            val cities = ArrayList<String>()
            if (cursor.moveToFirst()) {
                do {
                    cities.add(cursor.getString(cursor.getColumnIndexOrThrow("city")))
                } while (cursor.moveToNext())
            }
            cursor.close()
            cities.sort()
            return cities
        }


        public fun getGeziler(): ArrayList<Gezi> {
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT gezi_id, gezi_adi, gezi_ulke, gezi_sehir, gezi_baslangic_tarih, gezi_bitis_tarih, gezi_not FROM gezi", null)
            val geziler = ArrayList<Gezi>()
            if (cursor.moveToFirst()) {
                do {
                    val gezi = Gezi(
                        cursor.getInt(cursor.getColumnIndexOrThrow("gezi_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("gezi_adi")),
                        cursor.getString(cursor.getColumnIndexOrThrow("gezi_ulke")),
                        cursor.getString(cursor.getColumnIndexOrThrow("gezi_sehir")),
                        cursor.getString(cursor.getColumnIndexOrThrow("gezi_baslangic_tarih")),
                        cursor.getString(cursor.getColumnIndexOrThrow("gezi_bitis_tarih")),
                        cursor.getString(cursor.getColumnIndexOrThrow("gezi_not"))
                    )
                    geziler.add(gezi)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return geziler
        }

        public fun getGezi(geziId: Int): Gezi {
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT gezi_id, gezi_adi, gezi_ulke, gezi_sehir, gezi_baslangic_tarih, gezi_bitis_tarih, gezi_not FROM gezi WHERE gezi_id = $geziId", null)
            var geziid = 0
            var geziAdi = ""
            var geziUlke = ""
            var geziSehir = ""
            var geziBaslangicTarih = ""
            var geziBitisTarih = ""
            var geziNot = ""
            val galeri = ArrayList<Bitmap>()

            if (cursor.moveToFirst()) {
                do {
                    geziid = cursor.getInt(cursor.getColumnIndexOrThrow("gezi_id"))
                    geziAdi = cursor.getString(cursor.getColumnIndexOrThrow("gezi_adi"))
                    geziUlke = cursor.getString(cursor.getColumnIndexOrThrow("gezi_ulke"))
                    geziSehir = cursor.getString(cursor.getColumnIndexOrThrow("gezi_sehir"))
                    geziBaslangicTarih = cursor.getString(cursor.getColumnIndexOrThrow("gezi_baslangic_tarih"))
                    geziBitisTarih = cursor.getString(cursor.getColumnIndexOrThrow("gezi_bitis_tarih"))
                    geziNot = cursor.getString(cursor.getColumnIndexOrThrow("gezi_not"))
                } while (cursor.moveToNext())
            }
            cursor.close()
            val geziGaleri = getGeziGaleri(geziId)
            val gezi = Gezi(geziid, geziAdi, geziUlke, geziSehir, geziBaslangicTarih, geziBitisTarih, geziNot, geziGaleri)
            return gezi

        }

        public fun getGeziGaleri(geziId: Int): ArrayList<Bitmap> {
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT gorsel FROM gezi_galeri WHERE gezi_id = $geziId", null)
            val galeri = ArrayList<Bitmap>()
            if (cursor.moveToFirst()) {
                do {
                    val gorselBlob = cursor.getBlob(cursor.getColumnIndexOrThrow("gorsel"))
                    val gorselBitmap = BitmapFactory.decodeByteArray(gorselBlob, 0, gorselBlob.size)
                    val geziGaleri = gorselBitmap
                    galeri.add(geziGaleri)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return galeri
        }


        public fun addGezi(gezi: Gezi) {
            val db = writableDatabase
            if (gezi.getGeziId() == -1) db.execSQL("INSERT INTO gezi (gezi_adi, gezi_ulke, gezi_sehir, gezi_baslangic_tarih, gezi_bitis_tarih, gezi_not) VALUES('${gezi.getGeziAdi()}', '${gezi.getGeziUlke()}', '${gezi.getGeziSehir()}', '${gezi.getGeziBaslangicTarih()}', '${gezi.getGeziBitisTarih()}', '${gezi.getGeziNot()}')")
            else db.execSQL("INSERT INTO gezi (gezi_id, gezi_adi, gezi_ulke, gezi_sehir, gezi_baslangic_tarih, gezi_bitis_tarih, gezi_not) VALUES(${gezi.getGeziId()}, '${gezi.getGeziAdi()}', '${gezi.getGeziUlke()}', '${gezi.getGeziSehir()}', '${gezi.getGeziBaslangicTarih()}', '${gezi.getGeziBitisTarih()}', '${gezi.getGeziNot()}')")
        }


        public fun addGeziGaleri(geziID: Int, geziGaleri: Bitmap) {
            val db = writableDatabase
            val stmt = db.compileStatement("INSERT INTO gezi_galeri (gezi_id, gorsel) VALUES(?, ?)")
            stmt.bindLong(1, geziID.toLong())
            val stream = ByteArrayOutputStream()
            geziGaleri?.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            stmt.bindBlob(2, byteArray)
            stmt.executeInsert()
            stream.close()
            db.close()
        }

        public fun nextGeziId(): Int {
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT MAX(gezi_id) FROM gezi", null)
            var geziId = 0
            if (cursor.moveToFirst()) {
                geziId = cursor.getInt(cursor.getColumnIndexOrThrow("MAX(gezi_id)"))
            }
            cursor.close()
            return geziId + 1
        }

        public fun updateGezi(gezi: Gezi) {
            val db = writableDatabase
            db.execSQL("UPDATE gezi SET gezi_adi='${gezi.getGeziAdi()}', gezi_ulke='${gezi.getGeziUlke()}', gezi_sehir='${gezi.getGeziSehir()}', gezi_baslangic_tarih='${gezi.getGeziBaslangicTarih()}', gezi_bitis_tarih='${gezi.getGeziBitisTarih()}', gezi_not='${gezi.getGeziNot()}' WHERE gezi_id=${gezi.getGeziId()}")
        }


        public fun deleteGezi(gezi: Gezi) {
            val db = writableDatabase
            db.execSQL("DELETE FROM gezi WHERE gezi_id=${gezi.getGeziId()}")
        }

        public fun deleteGeziGaleri(geziId: Int) {
            val db = writableDatabase


            val query = "DELETE FROM gezi_galeri WHERE gezi_id = ?"
            val statement = db.compileStatement(query)
            statement.bindLong(1, geziId.toLong())

            statement.executeUpdateDelete()
        }



        public fun listTables(): ArrayList<String> {
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)
            val tables = ArrayList<String>()
            if (cursor.moveToFirst()) {
                do {
                    tables.add(cursor.getString(cursor.getColumnIndexOrThrow("name")))
                } while (cursor.moveToNext())
            }
            cursor.close()
            return tables
        }

        public fun getKapakFotoByGeziId(geziId: Int): Bitmap? {
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT gorsel FROM gezi_galeri WHERE gezi_id = $geziId LIMIT 1", null)
            var kapakFoto: Bitmap? = null
            if (cursor.moveToFirst()) {
                val gorselBlob = cursor.getBlob(cursor.getColumnIndexOrThrow("gorsel"))
                kapakFoto = BitmapFactory.decodeByteArray(gorselBlob, 0, gorselBlob.size)
            }
            cursor.close()
            return kapakFoto
        }

}
