package hu.bme.aut.onlab.tripplanner.ui.list.tripslist

import android.content.Context
import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.domain.AuthInteractor
import hu.bme.aut.onlab.tripplanner.domain.TripsInteractor
import javax.inject.Inject

class TripListPresenter @Inject constructor(
    private val tripsInteractor: TripsInteractor, private val authInteractor: AuthInteractor
) {

    suspend fun add(newItem: TripListItem): List<TripListItem> = withIOContext {
        return@withIOContext tripsInteractor.add(newItem)
    }

    suspend fun getUserEmail(): String? = withIOContext {
        return@withIOContext authInteractor.getCurrentUserEmail()
    }

    suspend fun signOut() = withIOContext {
        authInteractor.signOut()
    }

    suspend fun changePassword() = withIOContext {
        authInteractor.sendPasswordReset()
    }

    suspend fun changeEmail(context: Context, password: String?, newEmail: String?) = withIOContext {
        authInteractor.changeEmail(context, password, newEmail)
    }
}