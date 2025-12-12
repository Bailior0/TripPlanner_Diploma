package hu.bme.aut.onlab.tripplanner.domain

import android.graphics.Bitmap
import hu.bme.aut.onlab.tripplanner.data.datasource.FirebaseDataSource
import hu.bme.aut.onlab.tripplanner.data.datasource.SharedBackendDataSource
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShareInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
    private val backendDataSource: SharedBackendDataSource
) {

    var useBackend: Boolean = true

    suspend fun getItems(place: String): Flow<List<SharedData>> {
        return if (useBackend) {
            backendDataSource.getPosts(place)
        } else {
            firebaseDataSource.getPosts(place)
        }
    }

    suspend fun getItemsOnce(place: String): List<SharedData> {
        return if (useBackend) {
            backendDataSource.getPostsOnce(place)
        } else {
            firebaseDataSource.getPostsOnce(place)
        }
    }

    suspend fun uploadPost(
        place: String,
        nick: String,
        title: String,
        comment: String,
        image: Bitmap?
    ) {
        if (!useBackend) {
            firebaseDataSource.onUploadPost(place, nick, title, comment, image)
            return
        }

        backendDataSource.uploadPost(place, nick, title, comment, image)
    }

    suspend fun editPost(item: SharedData, image: Bitmap?) {
        if (!useBackend) {
            firebaseDataSource.onEditPost(item, image)
            return
        }

        val deleteImage = (image == null && item.pic == null)

        backendDataSource.editPost(item, image, deleteImage)
    }


    suspend fun deletePost(item: SharedData) {
        if (!useBackend) {
            firebaseDataSource.onDeletePost(item)
            return
        }

        backendDataSource.deletePost(item)
    }

    suspend fun likePost(item: SharedData) {
        if (!useBackend) {
            firebaseDataSource.onLikePost(item)
            return
        }

        backendDataSource.likePost(item)
    }
}
