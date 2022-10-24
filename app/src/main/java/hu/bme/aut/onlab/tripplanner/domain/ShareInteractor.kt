package hu.bme.aut.onlab.tripplanner.domain

import hu.bme.aut.onlab.tripplanner.data.datasource.FirebaseDataSource
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShareInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {
    suspend fun getItems(place: String): Flow<List<SharedData>> {
        return firebaseDataSource.getItems(place)
    }

    suspend fun getItemsOnce(place: String): List<SharedData> {
        return firebaseDataSource.getItemsOnce(place)
    }

    suspend fun uploadPost(place: String, nick: String, title: String, comment: String) {
        firebaseDataSource.onUploadPost(place, nick, title, comment)
    }

    suspend fun editPost(place: String, item: SharedData) {
        firebaseDataSource.onEditPost(place, item)
    }

    suspend fun deletePost(place: String, item: SharedData) {
        firebaseDataSource.onDeletePost(place, item)
    }

    suspend fun likePost(place: String, item: SharedData) {
        firebaseDataSource.onLikePost(place, item)
    }
}