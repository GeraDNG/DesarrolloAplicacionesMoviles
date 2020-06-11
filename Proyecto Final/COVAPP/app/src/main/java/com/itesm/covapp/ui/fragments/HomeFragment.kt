package com.itesm.covapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.itesm.covapp.R
import com.itesm.covapp.models.PostModel
import com.itesm.covapp.models.UserModel
import com.itesm.covapp.ui.adapters.PostAdapter
import com.itesm.covapp.utils.Intents
import com.itesm.covapp.utils.Msn
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment: Fragment() {

    //FirebaseInstance
    private lateinit var userDatabase: DatabaseReference
    private lateinit var postDatabase: DatabaseReference

    var state:String =""

    companion object{
        fun newInstance(): HomeFragment{
            val frag = HomeFragment()

            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        loadUserData()
    }

    private fun onClick(){
        btnAddPost.setOnClickListener {
            Intents.goToMakePost(requireActivity())
        }
    }

    private fun loadUserData(){
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        userDatabase = FirebaseDatabase.getInstance().reference.child("users").child(uid)
        //Get the user info
        userDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(item: DataSnapshot) {
                val user = item.getValue(UserModel::class.java)
                user?.let {
                    val stateUser = it.state
                    if (stateUser != null) {
                        loadPosts(stateUser)
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })


    }

    private fun loadPosts(stateUser:String){
        postDatabase = FirebaseDatabase.getInstance().reference.child("post").child(stateUser)
        postDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(data: DataSnapshot) {
                val orders = ArrayList<PostModel>()
                for(child in data.children.iterator()){
                    val order = child.getValue(PostModel::class.java)
                    if(order != null){
//                        order.id = child.key
//                        order.date = child.child("form").child("date").value.toString()
                        orders.add(order)
                    }else{
                        Msn.makeToast(activity!!,"No hay Post en tu ciudad")
                    }
                }
                val adapter = PostAdapter(activity!!,orders)
                recyclerViewPost.layoutManager = LinearLayoutManager(activity!!)
                recyclerViewPost.adapter = adapter
            }
        })
    }
}