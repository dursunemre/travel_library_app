package com.mobile.ddd

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.ddd.databinding.FragmentFirstBinding


class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.FAB.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_AddGeziFragment)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.gezilerListe)
        val dbHandler = DBHandler(requireContext())
        val geziList = dbHandler.getGeziler()

        val adapter = GeziAdapter(requireContext(), geziList)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = adapter

        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val childView = rv.findChildViewUnder(e.x, e.y)
                if (childView != null) {
                    val position = rv.getChildAdapterPosition(childView)
                    if (position != RecyclerView.NO_POSITION && position < geziList.size) {
                        val gezi = geziList[position]
                        val bundle = Bundle()
                        bundle.putInt("geziId", gezi.getGeziId())
                        try {
                            findNavController().navigate(R.id.action_FirstFragment_to_GeziDetayFragment, bundle)
                        } catch (ex: Exception) {
                            Log.e("NavigationError", "Hata oluştu: ${ex.localizedMessage}")
                        }
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}