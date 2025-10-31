package com.mobile.ddd

import android.R
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mobile.ddd.databinding.FragmentAddGeziBinding


class AddGeziFragment : Fragment() {
    private var _binding: FragmentAddGeziBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHandler: DBHandler
    private var geziGaleri: ArrayList<Bitmap> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddGeziBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val geziAdi = binding.geziAdi
        val geziUlke = binding.geziUlke
        val geziSehir = binding.geziSehir
        val baslangicTus = binding.geziBaslangic
        val bitisTus = binding.geziBitis
        val baslangicTarih = binding.baslangicTarih
        val bitisTarih = binding.bitisTarih
        val geziNot = binding.geziNot
        val geziGaleriEkle = binding.geziGaleriEkle
        val geziEkle = binding.geziEkle

        geziAdi.setOnFocusChangeListener{
                view, hasFocus ->
            if(!hasFocus)
            {
                geziEkle.isEnabled = areAllFieldsPopulated()
            }
        }
        geziNot.setOnFocusChangeListener{
                view, hasFocus ->
            if(!hasFocus) {
                geziEkle.isEnabled = areAllFieldsPopulated()
            }}


        baslangicTus.setOnClickListener {
            baslangicTarih.visibility = View.VISIBLE
            geziGaleriEkle.visibility = View.GONE
            geziEkle.visibility = View.GONE
            geziEkle.isEnabled = areAllFieldsPopulated()
        }


        bitisTus.setOnClickListener {
            bitisTarih.visibility = View.VISIBLE
            geziGaleriEkle.visibility = View.GONE
            geziEkle.visibility = View.GONE
            geziEkle.isEnabled = areAllFieldsPopulated()
        }


        baslangicTarih.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
            baslangicTus.text = selectedDate
            baslangicTarih.visibility = View.GONE
            geziGaleriEkle.visibility = View.VISIBLE
            geziEkle.visibility = View.VISIBLE
            geziEkle.isEnabled = areAllFieldsPopulated()
        }


        bitisTarih.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
            bitisTus.text = selectedDate
            bitisTarih.visibility = View.GONE
            geziGaleriEkle.visibility = View.VISIBLE
            geziEkle.visibility = View.VISIBLE
            geziEkle.isEnabled = areAllFieldsPopulated()
        }


        geziUlke.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val secilenUlke = geziUlke.getItemAtPosition(i).toString()
                val sehirler = dbHandler.getCities(secilenUlke)
                val sehirAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, sehirler)
                sehirAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.geziSehir.adapter = sehirAdapter
                geziEkle.isEnabled = areAllFieldsPopulated()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        };

        geziGaleriEkle.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
            geziEkle.isEnabled = areAllFieldsPopulated()
        }

        geziEkle.setOnClickListener {
            val ad = geziAdi.text.toString()
            val ulke = geziUlke.selectedItem.toString()
            val sehir = geziSehir.selectedItem.toString()
            val bas = baslangicTus.text.toString()
            val bit = bitisTus.text.toString()
            val not = geziNot.text.toString()
            val gezi = Gezi(-1, ad, ulke, sehir, bas, bit, not)
            dbHandler.addGezi(gezi)
            for (bitmap in geziGaleri) {
                dbHandler.addGeziGaleri(dbHandler.nextGeziId() - 1, bitmap)
            }
            geziGaleri.clear()
            Toast.makeText(requireContext(), "Gezi Eklendi", Toast.LENGTH_SHORT).show()
            geziAdi.text.clear()
            geziUlke.setSelection(0)
            geziSehir.setSelection(0)
            baslangicTus.text = "Gezinin Başlangıcı"
            bitisTus.text = "Gezinin Bitişi"
            baslangicTarih.visibility = View.GONE
            bitisTarih.visibility = View.GONE
            geziNot.text.clear()
            geziEkle.isEnabled = false
            findNavController().popBackStack()
        }

        dbHandler = DBHandler(requireContext())
        dbHandler.getDatabase()
        val ulkeler = dbHandler.getCountries()
        val ulkeAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, ulkeler)
        ulkeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.geziUlke.adapter = ulkeAdapter


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    fun areAllFieldsPopulated(): Boolean
    {
        val geziAdi = binding.geziAdi.text.toString()
        val geziUlke = binding.geziUlke.getSelectedItem()?.toString()
        val geziSehir = binding.geziSehir.getSelectedItem()?.toString()
        val geziBaslangic = binding.geziBaslangic.text.toString()
        val geziBitis = binding.geziBitis.text.toString()
        val geziNot = binding.geziNot.text.toString()

        return geziAdi.isNotBlank() && !geziUlke.isNullOrBlank() && !geziSehir.isNullOrBlank() && geziBaslangic.isNotBlank() && geziBaslangic != "Gezinin Başlangıcı" && geziBitis.isNotBlank() && geziBitis != "Gezinin Bitişi" && geziNot.isNotBlank()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return
            }

            if (data.clipData != null) {

                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                    geziGaleri.add(bitmap)
                }
            } else if (data.data != null) {

                val imageUri = data.data!!
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                geziGaleri.add(bitmap)
            }
        }
    }

}