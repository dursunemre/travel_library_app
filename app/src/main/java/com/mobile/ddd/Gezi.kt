package com.mobile.ddd

import android.graphics.Bitmap

public class Gezi 
{
    private var gezi_id: Int = 0
    private var gezi_adi: String = ""
    private var gezi_ulke: String = ""
    private var gezi_sehir: String = ""
    private var gezi_baslangic_tarih: String = ""
    private var gezi_bitis_tarih: String = ""
    private var gezi_not: String = ""
    private var gezi_galeri: ArrayList<Bitmap> = ArrayList<Bitmap>()

    constructor(gezi_id: Int, gezi_adi: String, gezi_ulke: String, gezi_sehir: String, gezi_baslangic_tarih: String, gezi_bitis_tarih: String, gezi_not: String, gezi_galeri: ArrayList<Bitmap>) {
        this.gezi_id = gezi_id
        this.gezi_adi = gezi_adi
        this.gezi_ulke = gezi_ulke
        this.gezi_sehir = gezi_sehir
        this.gezi_baslangic_tarih = gezi_baslangic_tarih
        this.gezi_bitis_tarih = gezi_bitis_tarih
        this.gezi_not = gezi_not
        this.gezi_galeri = gezi_galeri

    }
    constructor(gezi_id: Int, gezi_adi: String, gezi_ulke: String, gezi_sehir: String, gezi_baslangic_tarih: String, gezi_bitis_tarih: String, gezi_not: String) {
        this.gezi_id = gezi_id
        this.gezi_adi = gezi_adi
        this.gezi_ulke = gezi_ulke
        this.gezi_sehir = gezi_sehir
        this.gezi_baslangic_tarih = gezi_baslangic_tarih
        this.gezi_bitis_tarih = gezi_bitis_tarih
        this.gezi_not = gezi_not
    }

    public fun getGeziId(): Int {
        return gezi_id
    }

    public fun getGeziAdi(): String {
        return gezi_adi
    }

    public fun getGeziUlke(): String {
        return gezi_ulke
    }

    public fun getGeziSehir(): String {
        return gezi_sehir
    }

    public fun getGeziBaslangicTarih(): String {
        return gezi_baslangic_tarih
    }

    public fun getGeziBitisTarih(): String {
        return gezi_bitis_tarih
    }

    public fun getGeziNot(): String {
        return gezi_not
    }

    public fun getGeziGaleri(): ArrayList<Bitmap> {
        return gezi_galeri
    }

}