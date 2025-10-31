package com.mobile.ddd

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.ddd.databinding.ItemGeziBinding

class GeziAdapter(private val context: Context, private val geziler: List<Gezi>) :
    RecyclerView.Adapter<GeziAdapter.GeziViewHolder>() {

    class GeziViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemGeziBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeziViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_gezi, parent, false)
        return GeziViewHolder(view)
    }

    override fun onBindViewHolder(holder: GeziViewHolder, position: Int) {
        val gezi = geziler[position]
        holder.binding.gezininAdi.text = gezi.getGeziAdi()

        val dbHandler = DBHandler(context)

        val kapakFoto = dbHandler.getKapakFotoByGeziId(gezi.getGeziId())

        kapakFoto?.let {
            holder.binding.kapakFoto.setImageBitmap(it)
        }
    }

    override fun getItemCount(): Int {
        return geziler.size
    }
}

