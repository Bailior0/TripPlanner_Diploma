package hu.bme.aut.onlab.tripplanner.data.datasource

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.data.disk.model.User
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDataSource @Inject constructor() {

    private val database = Firebase.firestore
    private val storageRef = Firebase.storage.reference

    suspend fun getTrips(): Flow<List<TripListItem>> = callbackFlow {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
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
        val uid = FirebaseAuth.getInstance().currentUser?.uid
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
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null)
            newItem.uid = uid

        database.collection("trips").add(newItem)
            .addOnSuccessListener { documentReference ->
                Log.d("success", "DocumentSnapshot written with ID: $documentReference.")
            }
            .addOnFailureListener { exception ->
                Log.d("failure", "Error getting documents: ", exception)
            }.await()

        if(uid != null)
            database.collection("users").document(uid).update("tripSize", FieldValue.increment(1))
                .addOnSuccessListener { documentReference ->
                    Log.d("success", "DocumentSnapshot written with ID: $documentReference.")
                }
                .addOnFailureListener { exception ->
                    Log.d("failure", "Error getting documents: ", exception)
                }
    }

    suspend fun onEditTrip(item: TripListItem) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
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
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if(item.uid == uid)
            database.collection("trips").document(item.fbid!!.toString()).delete()
                .addOnSuccessListener { documentReference ->
                    Log.d("success", "DocumentSnapshot written with ID: $documentReference.")
                }
                .addOnFailureListener { exception ->
                    Log.d("failure", "Error getting documents: ", exception)
                }.await()

        if(uid != null)
            database.collection("users").document(uid).update("tripSize", FieldValue.increment(-1))
                .addOnSuccessListener { documentReference ->
                    Log.d("success", "DocumentSnapshot written with ID: $documentReference.")
                }
                .addOnFailureListener { exception ->
                    Log.d("failure", "Error getting documents: ", exception)
                }
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

    suspend fun onUploadPost(trip: String, nick: String, title: String, comment: String, image: Bitmap?) {
        var imageId = ""
        if(image!= null) {
            imageId = UUID.randomUUID().toString() + ".jpg"
            val imageRef = storageRef.child(imageId)
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            var uploadTask = imageRef.putBytes(data)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                //Log.i("dolog", taskSnapshot.uploadSessionUri.toString())
            }
        }


        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val newPost = SharedData(null, uid, FirebaseAuth.getInstance().currentUser?.displayName, nick, title, comment, mutableListOf(), trip, imageId)

        database.collection("comments").add(newPost)
            .addOnSuccessListener { documentReference ->
                Log.d("success", "DocumentSnapshot written with ID: $documentReference.")
            }
            .addOnFailureListener { exception ->
                Log.d("failure", "Error getting documents: ", exception)
            }.await()
    }

    suspend fun onEditPost(item: SharedData, image: Bitmap?) {
        var imageId = ""
        if(image!= null) {
            imageId = UUID.randomUUID().toString() + ".jpg"
            val imageRef = storageRef.child(imageId)
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            var uploadTask = imageRef.putBytes(data)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                //Log.i("dolog", taskSnapshot.uploadSessionUri.toString())
            }
        }
        item.pic = imageId

        val uid = FirebaseAuth.getInstance().currentUser?.uid
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
        val uid = FirebaseAuth.getInstance().currentUser?.uid
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
        val uid = FirebaseAuth.getInstance().currentUser?.uid
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

    fun addUser(uid: String?, name: String, mail: String) {
        val newUser = User(name = name, email = mail)
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

    suspend fun getCurrentUser(): QuerySnapshot {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        return database.collection("users").whereEqualTo("id", uid).limit(1).get()
            .addOnSuccessListener { }
            .addOnFailureListener { exception ->
                Log.d("failure", "Error getting documents: ", exception)
            }
            .await()
    }

    suspend fun getCurrentIdToken(): String {
        val user = FirebaseAuth.getInstance().currentUser
            ?: return ""

        var lastError: Exception? = null

        repeat(3) { attempt ->
            try {
                val tokenResult = user.getIdToken(true).await()
                val token = tokenResult.token
                if (!token.isNullOrEmpty()) {
                    return token
                }
            } catch (e: Exception) {
                lastError = e
                Log.e("FirebaseAuth", "Token fetch failed (attempt $attempt): ${e.message}")
                delay(500)
            }
        }

        Log.e("FirebaseAuth", "Token fetch failed after retries: ${lastError?.message}")

        return ""
    }



    suspend fun uploadImageForComments(bitmap: Bitmap): String {
        val imageId = UUID.randomUUID().toString() + ".jpg"
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
        val data = baos.toByteArray()

        val imageRef = storageRef.child(imageId)
        imageRef.putBytes(data).await()

        return imageId
    }
}