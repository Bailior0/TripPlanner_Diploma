package hu.bme.aut.onlab.tripplanner.data.datasource

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.data.disk.model.User
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDataSource @Inject constructor() {

    private val database = Firebase.firestore
    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    suspend fun getTrips(): Flow<List<TripListItem>> = callbackFlow {
        val listenerRegistration = database.collection("trips").whereEqualTo("uid", uid)
            .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                if (firebaseFirestoreException != null) {
                    cancel(message = "Error fetching items", cause = firebaseFirestoreException)
                    return@addSnapshotListener
                }
                val items = mutableListOf<TripListItem>()
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

    suspend fun getTripsOnce(): List<TripListItem> {
        val items = mutableListOf<TripListItem>()
        database.collection("trips").whereEqualTo("uid", uid).get()
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

    suspend fun onUploadTrip(newItem: TripListItem) {
        if (uid != null)
            newItem.uid = uid

        database.collection("trips").add(newItem)
            .addOnSuccessListener { documentReference ->
                Log.d("success", "DocumentSnapshot written with ID: $documentReference.")
            }
            .addOnFailureListener { exception ->
                Log.d("failure", "Error getting documents: ", exception)
            }.await()
    }

    suspend fun onEditTrip(item: TripListItem) {
        if(item.uid == uid)
            database.collection("trips").document(item.fbid!!.toString()).set(item)
                .addOnSuccessListener { documentReference ->
                    Log.d("success", "DocumentSnapshot written with ID: $documentReference.")
                }
                .addOnFailureListener { exception ->
                    Log.d("failure", "Error getting documents: ", exception)
                }.await()
    }

    suspend fun onDeleteTrip(item: TripListItem) {
        if(item.uid == uid)
            database.collection("trips").document(item.fbid!!.toString()).delete()
                .addOnSuccessListener { documentReference ->
                    Log.d("success", "DocumentSnapshot written with ID: $documentReference.")
                }
                .addOnFailureListener { exception ->
                    Log.d("failure", "Error getting documents: ", exception)
                }.await()
    }

    suspend fun getPosts(trip: String): Flow<List<SharedData>> = callbackFlow {
        val listenerRegistration = database.collection("comments").whereEqualTo("town", trip)
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

    suspend fun getPostsOnce(trip: String): List<SharedData> {
        val items = mutableListOf<SharedData>()
        database.collection("comments").whereEqualTo("town", trip).get()
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
        val newPost = SharedData(null, uid, FirebaseAuth.getInstance().currentUser?.displayName, nick, title, comment, mutableListOf(), trip)

        database.collection("comments").add(newPost)
            .addOnSuccessListener { documentReference ->
                Log.d("success", "DocumentSnapshot written with ID: $documentReference.")
            }
            .addOnFailureListener { exception ->
                Log.d("failure", "Error getting documents: ", exception)
            }.await()
    }

    suspend fun onEditPost(item: SharedData) {
        if(item.uid == uid)
            database.collection("comments").document(item.id!!).set(item)
                .addOnSuccessListener { documentReference ->
                    Log.d("success", "DocumentSnapshot written with ID: $documentReference.")
                }
                .addOnFailureListener { exception ->
                    Log.d("failure", "Error getting documents: ", exception)
                }.await()
    }

    suspend fun onDeletePost(item: SharedData) {
        if(item.uid == uid)
            database.collection("comments").document(item.id!!).delete()
                .addOnSuccessListener { documentReference ->
                    Log.d("success", "DocumentSnapshot written with ID: $documentReference.")
                }
                .addOnFailureListener { exception ->
                    Log.d("failure", "Error getting documents: ", exception)
                }.await()
    }

    suspend fun onLikePost(item: SharedData) {
        val userFound = item.liked.find { it == uid }

        if(!userFound.isNullOrEmpty()) {
            val poz = item.liked.indexOf(userFound)
            item.liked.removeAt(poz)
        }
        else {
            item.liked.add(uid!!)
        }

        database.collection("comments").document(item.id!!)
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

    fun addUser(uid: String?, name: String) {
        val newUser = User(name = name)
        if(uid != null)
            newUser.id = uid
        else
            newUser.id = UUID.randomUUID().toString()

        database.collection("users").document(newUser.id).set(newUser)
            .addOnSuccessListener { documentReference ->
                Log.d("success", "DocumentSnapshot written with ID: $documentReference.")
            }
            .addOnFailureListener { exception ->
                Log.d("failure", "Error getting documents: ", exception)
            }
    }
}