package hu.bme.aut.onlab.tripplanner.domain

import hu.bme.aut.onlab.tripplanner.data.datasource.FirebaseDataSource
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShareInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {
    suspend fun getItems(place: String): Flow<List<SharedData>> {
        return firebaseDataSource.getPosts(place)
    }

    suspend fun getItemsOnce(place: String): List<SharedData> {
        return firebaseDataSource.getPostsOnce(place)
    }

    suspend fun uploadPost(place: String, nick: String, title: String, comment: String) {
        firebaseDataSource.onUploadPost(place, nick, title, comment)
    }

    suspend fun editPost(item: SharedData) {
        firebaseDataSource.onEditPost(item)
    }

    suspend fun deletePost(item: SharedData) {
        firebaseDataSource.onDeletePost(item)
    }

    suspend fun likePost(item: SharedData) {
        firebaseDataSource.onLikePost(item)
    }
}