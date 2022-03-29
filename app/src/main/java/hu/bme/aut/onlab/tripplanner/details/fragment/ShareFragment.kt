package hu.bme.aut.onlab.tripplanner.details.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.aut.onlab.tripplanner.databinding.FragmentShareBinding
import hu.bme.aut.onlab.tripplanner.details.DetailsActivity
import hu.bme.aut.onlab.tripplanner.details.adapter.ShareAdapter
import hu.bme.aut.onlab.tripplanner.details.data.SharedData

class ShareFragment : Fragment(), ShareAdapter.SharelistItemClickListener {
    private lateinit var shareAdapter: ShareAdapter
    private lateinit var binding: FragmentShareBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShareBinding.inflate(layoutInflater, container, false)
        shareAdapter = ShareAdapter(requireActivity().applicationContext, this)
        binding.rvMain.adapter = shareAdapter
        shareAdapter.setActivity(activity as DetailsActivity)
        binding.rvMain.layoutManager = LinearLayoutManager(requireActivity().applicationContext).apply {
            reverseLayout = true
            stackFromEnd = true
        }
        binding.fab.setOnClickListener{
            val act = activity as DetailsActivity
            if(act.isOnline(requireActivity().applicationContext))
                NewShareItemDialogFragment().show(
                    childFragmentManager,
                    NewShareItemDialogFragment.TAG
                )
        }
        initPostsListener()
        return binding.root
    }

    private fun initPostsListener() {
        val act = activity as DetailsActivity
        val db = Firebase.firestore
        db.collection(act.place!!)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(requireActivity().applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> shareAdapter.addPost(dc.document.toObject(), dc.document.id)
                        DocumentChange.Type.MODIFIED -> shareAdapter.editPost(dc.document.toObject(), dc.document.id)
                        DocumentChange.Type.REMOVED -> shareAdapter.removePost(dc.document.id)
                    }
                }
            }
    }

    override fun onItemEdited(item: SharedData) {
        NewShareItemDialogFragment(item).show(
            childFragmentManager,
            NewShareItemDialogFragment.TAG
        )
    }

    override fun onItemLiked(item: SharedData) {
        val act = activity as DetailsActivity
        val db = Firebase.firestore
        val userId = act.getUId()
        val userFound = item.liked.find { it == userId }

        if(!userFound.isNullOrEmpty()) {
            val poz = item.liked.indexOf(userFound)
            item.liked.removeAt(poz)
        }
        else {
            item.liked.add(userId!!)
        }

        db.collection(act.place!!).document(item.id!!)
            .update(mapOf(
                "liked" to item.liked,
                "id" to item.id
            ))
    }

    override fun onItemRemoved(item: SharedData) {
        val act = activity as DetailsActivity
        if(item.uid == act.getUId()) {
            val db = Firebase.firestore

            Log.d("Trips", "" + item.id)

            db.collection(act.place!!).document(item.id!!)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(requireActivity().applicationContext, "Post removed", Toast.LENGTH_SHORT).show() }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireActivity().applicationContext, exception.message, Toast.LENGTH_SHORT).show() }
        }
    }
}