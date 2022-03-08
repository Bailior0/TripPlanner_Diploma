package hu.bme.aut.onlab.tripplanner.details.fragment

import android.os.Bundle
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

class ShareFragment : Fragment() {
    private lateinit var shareAdapter: ShareAdapter
    private lateinit var binding: FragmentShareBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShareBinding.inflate(layoutInflater, container, false)
        shareAdapter = ShareAdapter(requireActivity().applicationContext)
        binding.rvMain.adapter = shareAdapter
        binding.rvMain.layoutManager = LinearLayoutManager(requireActivity().applicationContext).apply {
            reverseLayout = true
            stackFromEnd = true
        }
        binding.fab.setOnClickListener{
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
                        DocumentChange.Type.ADDED -> shareAdapter.addPost(dc.document.toObject())
                        DocumentChange.Type.MODIFIED -> Toast.makeText(requireActivity().applicationContext, dc.document.data.toString(), Toast.LENGTH_SHORT).show()
                        DocumentChange.Type.REMOVED -> Toast.makeText(requireActivity().applicationContext, dc.document.data.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}