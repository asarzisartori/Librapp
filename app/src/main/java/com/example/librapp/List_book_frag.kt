package com.example.librapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.*


class List_book_frag : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_list_book_frag, container, false)
        var listView = view.findViewById<ListView>(R.id.list_view_book)
        val datab : FirebaseDatabase = FirebaseDatabase.getInstance()
        val reference : DatabaseReference = datab.getReference("Item/Libro")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemList: ArrayList<Item> = ArrayList()
                for (itemSnap: DataSnapshot in snapshot.getChildren()) {
                    itemList.add(itemSnap.getValue(Item::class.java)!!)
                }
                val itemAdapter = ItemAdapter(activity, itemList)
                listView.adapter = itemAdapter
            }

            override fun onCancelled(error: DatabaseError) {}

        })

        listView.setOnItemClickListener { arg0, arg1, position, arg3 ->
            if(requireActivity() is UserLoggedActivity) {
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragment_container_userlogged, ProductWindowLogged_frag())
                transaction?.commit()
            } else {
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragment_container_freeaccess, ProductWindowNoLogged_frag())
                transaction?.commit()
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
    }

}