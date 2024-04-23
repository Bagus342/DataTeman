package com.example.datateman

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RecyclerViewAdapter (private val dataTeman: ArrayList<data_teman>, context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
        private val context: Context

        val database = FirebaseDatabase.getInstance()
        private var auth: FirebaseAuth? = null

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val Nama: TextView
            val Alamat: TextView
            val NoHP: TextView
            val ListItem: LinearLayout

            init {
                Nama = itemView.findViewById(R.id.namax)
                Alamat = itemView.findViewById(R.id.alamatx)
                NoHP = itemView.findViewById(R.id.no_hpx)
                ListItem = itemView.findViewById(R.id.list_item)
            }
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val V: View = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false)
        return ViewHolder(V)
    }
    override fun getItemCount(): Int {
        return dataTeman.size
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val Nama: String? = dataTeman.get(position).nama
        val Alamat: String? = dataTeman.get(position).alamat
        val NoHp: String? = dataTeman.get(position).no_hp

        holder.Nama.text = "Nama : $Nama"
        holder.Alamat.text = "Alamat : $Alamat"
        holder.NoHP.text = "No Hp : $NoHp"
        holder.ListItem.setOnLongClickListener {
            holder.ListItem.setOnLongClickListener { view ->
                val action = arrayOf("Update", "Delete")
                val alert: AlertDialog.Builder = AlertDialog.Builder(view.context)
                alert.setItems(action, DialogInterface.OnClickListener { dialog, i ->
                    when (i) {
                        0 -> {
                            val bundle = Bundle()
                            bundle.putString("dataNama", dataTeman[position].nama)
                            bundle.putString("dataAlamat", dataTeman[position].alamat)
                            bundle.putString("dataNoHP", dataTeman[position].no_hp)
                            bundle.putString("getPrimaryKey", dataTeman[position].key)
                            Log.e("RecyclerViewAdapter", dataTeman[position].nama!!)
                            val intent = Intent(view.context.applicationContext, UpdateData::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.putExtras(bundle)
                            context.startActivity(intent)
                        }

                        1 -> {
                            deleteItem(position)
                        }
                    }
                })
                alert.create()
                alert.show()
                true
            }
            true
        }
    }
    fun deleteItem(position: Int) {
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference().child("Admin").child(getUserID).child("DataTeman")

        val deletedItem = dataTeman[position]
        getReference.child(deletedItem.key!!).removeValue()
        dataTeman.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()

        Toast.makeText(context, "Data ${deletedItem.nama} berhasil dihapus", Toast.LENGTH_SHORT).show()
    }


    init {
        this.context = context
        auth = FirebaseAuth.getInstance()
    }
}