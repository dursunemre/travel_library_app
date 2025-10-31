package com.mobile.ddd

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.compose.ui.graphics.findFirstRoot
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.ddd.databinding.FragmentGeziDetayBinding

class FragmentGeziDetay : Fragment() {
    private var _binding: FragmentGeziDetayBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHandler: DBHandler
    var geziId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        geziId = arguments?.getInt("geziId") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeziDetayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHandler = DBHandler(requireContext())
        val gezi = dbHandler.getGezi(geziId)

        binding.geziAdi.text = gezi.getGeziAdi()
        binding.geziUlke.text = gezi.getGeziUlke()
        binding.geziSehir.text = gezi.getGeziSehir()
        binding.geziBaslangic.text = gezi.getGeziBaslangicTarih()
        binding.geziBitis.text = gezi.getGeziBitisTarih()
        binding.geziNot.text = gezi.getGeziNot()

        val recyclerView = binding.GeziGaleri
        val adapter = HorizontalImageAdapter(requireContext(), gezi.getGeziGaleri())
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        binding.menuButton.setOnClickListener {
            showPopupMenu(it)
        }

    }

    private fun showPopupMenu(anchor: View) {
        val popupMenu = PopupMenu(requireContext(), anchor)
        popupMenu.menuInflater.inflate(R.menu.gezi_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_edit -> {
                    println("Düzenle seçeneği seçildi")
                    navigateToEditScreen()
                    true
                }
                R.id.menu_delete -> {
                    println("Sil seçeneği seçildi")
                    delete()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun navigateToEditScreen() {
        val action = FragmentGeziDetayDirections.actionGeziDetayFragmentToGeziUpdateFragment(
            geziId = geziId
        )
        findNavController().navigate(action)
    }

    fun delete() {
        val dbHandler = DBHandler(requireContext())

        dbHandler.deleteGeziGaleri(geziId)

        val gezi = dbHandler.getGezi(geziId)
        dbHandler.deleteGezi(gezi)

        Toast.makeText(requireContext(), "Gezi ve galeri başarıyla silindi.", Toast.LENGTH_SHORT).show()

        val navController = findNavController()
        navController.navigate(R.id.action_GeziDetayFragment_to_FirstFragment)
    }

}


