package hu.bme.aut.onlab.tripplanner.data.network.model

import com.google.firebase.firestore.DocumentId

data class SharedData (
    @DocumentId
    var id: String? = null,
    val uid: String? = null,
    val author: String? = null,
    var nickname: String? = null,
    var title: String? = null,
    var body: String? = null,
    var liked: MutableList<String> = mutableListOf(),
    var town: String? = null,
    var pic: String? = null
)