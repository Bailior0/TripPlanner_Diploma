package hu.bme.aut.onlab.tripplanner.data.datasource

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDataSource @Inject constructor() {

    private val database = Firebase.firestore

    suspend fun getItems(trip: String): Flow<List<SharedData>> = callbackFlow {
        val listenerRegistration = database.collection(trip)
            .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                if (firebaseFirestoreException != null) {
                    cancel(message = "Error fetching items", cause = firebaseFirestoreException)
                    return@addSnapshotListener
                }
                val items = mutableListOf<SharedData>()
                if (querySnapshot != null) {
                    for(document in querySnapshot) {
                        items.add(document.toObject())
                    }
                }
                this.trySend(items).isSuccess
            }
        awaitClose {
            Log.d("failure", "Cancelling items listener")
            listenerRegistration.remove()
        }
    }

    suspend fun getItemsOnce(trip: String): List<SharedData> {
        val items = mutableListOf<SharedData>()
        database.collection(trip).get()
            .addOnSuccessListener { documents ->
                for(document in documents)
                    items.add(document.toObject())
            }
            .addOnFailureListener { exception ->
                Log.d("failure", "Error getting documents: ", exception)
            }
            .await()

        return items
    }

    suspend fun onUploadPost(trip: String, nick: String, title: String, comment: String) {
        val newPost = SharedData(null, FirebaseAuth.getInstance().currentUser?.uid, FirebaseAuth.getInstance().currentUser?.displayName, nick, title, comment)

        database.collection(trip).add(newPost)
            .addOnSuccessListener { documentReference ->
                Log.d("success", "DocumentSnapshot written with ID: $documentReference.")
            }
            .addOnFailureListener { exception ->
                Log.d("failure", "Error getting documents: ", exception)
            }.await()
    }

    suspend fun onEditPost(trip: String, item: SharedData) {
        if(item.uid == FirebaseAuth.getInstance().currentUser?.uid)
            database.collection(trip).document(item.id!!).set(item)
                .addOnSuccessListener { documentReference ->
                    Log.d("success", "DocumentSnapshot written with ID: $documentReference.")
                }
                .addOnFailureListener { exception ->
                    Log.d("failure", "Error getting documents: ", exception)
                }.await()
    }

    suspend fun onDeletePost(place: String, item: SharedData) {
        if(item.uid == FirebaseAuth.getInstance().currentUser?.uid)
            Firebase.firestore.collection(place).document(item.id!!).delete()
                .addOnSuccessListener { documentReference ->
                    Log.d("success", "DocumentSnapshot written with ID: $documentReference.")
                }
                .addOnFailureListener { exception ->
                    Log.d("failure", "Error getting documents: ", exception)
                }.await()
    }

    suspend fun onLikePost(place: String, item: SharedData) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userFound = item.liked.find { it == userId }

        if(!userFound.isNullOrEmpty()) {
            val poz = item.liked.indexOf(userFound)
            item.liked.removeAt(poz)
        }
        else {
            item.liked.add(userId!!)
        }

        Firebase.firestore.collection(place).document(item.id!!)
            .update(mapOf(
                "liked" to item.liked
            ))
            .addOnSuccessListener { documentReference ->
                Log.d("success", "DocumentSnapshot written with ID: $documentReference.")
            }
            .addOnFailureListener { exception ->
                Log.d("failure", "Error getting documents: ", exception)
            }.await()
    }
}