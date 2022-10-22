package hu.bme.aut.onlab.tripplanner.ui.details.pages.share

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import hu.bme.aut.onlab.tripplanner.domain.ShareInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SharePresenter @Inject constructor(
    private val shareInteractor: ShareInteractor
) {
    suspend fun getItems(place: String): Flow<List<SharedData>> = withIOContext {
        shareInteractor.getItems(place)
    }

    suspend fun uploadPost(place: String, nick: String, title: String, comment: String) = withIOContext {
        shareInteractor.uploadPost(place, nick, title, comment)
    }

    suspend fun editPost(place: String, item: SharedData) = withIOContext {
        shareInteractor.editPost(place, item)
    }

    suspend fun deletePost(place: String, item: SharedData) = withIOContext {
        shareInteractor.deletePost(place, item)
    }

    suspend fun likePost(place: String, item: SharedData) = withIOContext {
        shareInteractor.likePost(place, item)
    }
}
