package hu.bme.aut.onlab.tripplanner.ui.details.pages.share

import android.graphics.Bitmap
import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import hu.bme.aut.onlab.tripplanner.domain.AuthInteractor
import hu.bme.aut.onlab.tripplanner.domain.ShareInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SharePresenter @Inject constructor(
    private val shareInteractor: ShareInteractor, private val authInteractor: AuthInteractor
) {
    suspend fun getItems(place: String): Flow<List<SharedData>> = withIOContext {
        shareInteractor.getItems(place)
    }

    suspend fun getItemsOnce(place: String): List<SharedData> = withIOContext {
        shareInteractor.getItemsOnce(place)
    }

    suspend fun uploadPost(place: String, nick: String, title: String, comment: String, image: Bitmap?) = withIOContext {
        shareInteractor.uploadPost(place, nick, title, comment, image)
    }

    suspend fun editPost(item: SharedData, image: Bitmap?) = withIOContext {
        shareInteractor.editPost(item, image)
    }

    suspend fun deletePost(item: SharedData) = withIOContext {
        shareInteractor.deletePost(item)
    }

    suspend fun likePost(item: SharedData) = withIOContext {
        shareInteractor.likePost(item)
    }

    suspend fun getCurrentUser(): String? = withIOContext {
        authInteractor.getCurrentUser()
    }
}
