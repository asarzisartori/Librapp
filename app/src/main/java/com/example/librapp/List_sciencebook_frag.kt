package com.example.librapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import com.google.firebase.database.*
import kotlin.collections.ArrayList

class List_sciencebook_frag : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_list_sciencebook_frag, container, false)
        var listView = view.findViewById<ListView>(R.id.list_view_sciencebook)
        val datab : FirebaseDatabase = FirebaseDatabase.getInstance()
        val reference : DatabaseReference = datab.getReference("Item/Scienza")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemList : ArrayList<Item> =  ArrayList()
                for( itemSnap : DataSnapshot  in snapshot.getChildren()){
                    itemList.add(itemSnap.getValue(Item::class.java)!!)
                }
                val itemAdapter = ItemAdapter(activity, itemList)
                listView.adapter = itemAdapter
            }

            override fun onCancelled(error: DatabaseError) { }

        })

        listView.setOnItemClickListener {
            parent: AdapterView<*>?, view: View, position: Int, id: Long ->
            if(requireActivity() is UserLoggedActivity) {
                val item = listView.getAdapter().getItem(position)
                val intent = Intent(activity, ProductWindowLoggedActivity::class.java)
                intent.putExtra("Titolo", item.toString())
                intent.putExtra("Tipologia", "Scienza")
                startActivity(intent)
            } else {
                val item = listView.getAdapter().getItem(position)
                val intent = Intent(activity, ProductWindowNoLoggedActivity::class.java)
                intent.putExtra("Titolo", item.toString())
                intent.putExtra("Tipologia", "Scienza")
                startActivity(intent)
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
    }

}