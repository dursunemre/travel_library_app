package com.mobile.ddd

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.mobile.ddd.databinding.FragmentGeziUpdateFragmentBinding
import android.provider.MediaStore
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class gezi_update_fragment : Fragment() {
    private val args: gezi_update_fragmentArgs by navArgs()

    private var _binding: FragmentGeziUpdateFragmentBinding? = null
    private val binding get() = _binding!!

    private var geziGaleri: ArrayList<Bitmap> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeziUpdateFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val geziId = args.geziId
        val db = DBHandler(requireContext())
        val gezi = db.getGezi(geziId)

        binding.geziAdiDuzenle.setText(gezi.getGeziAdi())
        binding.geziBaslangicDuzenle.text = gezi.getGeziBaslangicTarih()
        binding.geziBitisDuzenle.text = gezi.getGeziBitisTarih()
        binding.geziNotDuzenle.setText(gezi.getGeziNot())

        binding.geziBaslangicDuzenle.setOnClickListener {
            binding.baslangicTarihDuzenle.visibility = View.VISIBLE
            binding.bitisTarihDuzenle.visibility = View.GONE
            binding.geziGaleriEkleDuzenle.visibility = View.GONE
            binding.geziDuzenle.visibility = View.GONE
        }

        binding.baslangicTarihDuzenle.setOnDateChangedListener { _, year, month, day ->
            val date = formatDate(year, month, day)
            binding.geziBaslangicDuzenle.text = date
            binding.baslangicTarihDuzenle.visibility = View.GONE
            binding.geziGaleriEkleDuzenle.visibility = View.VISIBLE
            binding.geziDuzenle.visibility = View.VISIBLE
        }

        binding.geziBitisDuzenle.setOnClickListener {
            binding.bitisTarihDuzenle.visibility = View.VISIBLE
            binding.baslangicTarihDuzenle.visibility = View.GONE
            binding.geziGaleriEkleDuzenle.visibility = View.GONE
            binding.geziDuzenle.visibility = View.GONE
        }

        binding.bitisTarihDuzenle.setOnDateChangedListener { _, year, month, day ->
            val date = formatDate(year, month, day)
            binding.geziBitisDuzenle.text = date
            binding.bitisTarihDuzenle.visibility = View.GONE
            binding.geziGaleriEkleDuzenle.visibility = View.VISIBLE
            binding.geziDuzenle.visibility = View.VISIBLE
        }

        binding.geziGaleriEkleDuzenle.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }

        binding.geziDuzenle.setOnClickListener {
            val updatedGezi = Gezi(
                gezi_id = geziId,
                gezi_adi = binding.geziAdiDuzenle.text.toString(),
                gezi_ulke = gezi.getGeziUlke(),
                gezi_sehir = gezi.getGeziSehir(),
                gezi_baslangic_tarih = binding.geziBaslangicDuzenle.text.toString(),
                gezi_bitis_tarih = binding.geziBitisDuzenle.text.toString(),
                gezi_not = binding.geziNotDuzenle.text.toString(),
            )

            db.updateGezi(updatedGezi)

            if(geziGaleri.size != 0){
                db.deleteGeziGaleri(geziId)
            }

            for (bitmap in geziGaleri) {
                db.addGeziGaleri(geziId, bitmap)
            }

            geziGaleri.clear()

            Toast.makeText(requireContext(), "Gezi başarıyla güncellendi!", Toast.LENGTH_SHORT).show()
            binding.geziAdiDuzenle.setText(updatedGezi.getGeziAdi())
            binding.geziBaslangicDuzenle.text = updatedGezi.getGeziBaslangicTarih()
            binding.geziBitisDuzenle.text = updatedGezi.getGeziBitisTarih()
            binding.geziNotDuzenle.setText(updatedGezi.getGeziNot())
        }
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

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
