package hu.bme.aut.onlab.tripplanner.data.datasource

import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.data.network.TripBackendService
import hu.bme.aut.onlab.tripplanner.data.network.qualifiers.TripRetrofit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendDataSource @Inject constructor(
    private val service: TripBackendService
) {
    private suspend fun getAuthHeader(): String {
        val tokenResult = FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.await()
        val token = tokenResult?.token ?: throw Exception("No token")
        return "Bearer $token"
    }

    suspend fun getTrips(): List<TripListItem> {
        val header = getAuthHeader()
        val resp = service.getTrips(header)
        if (resp.isSuccessful) return resp.body() ?: emptyList()
        else throw Exception("Backend error: ${resp.code()} ${resp.message()}")
    }

    suspend fun onUploadTrip(newItem: TripListItem) {
        val header = getAuthHeader()
        val resp = service.addTrip(header, newItem)
        if (!resp.isSuccessful) throw Exception("Upload failed")
    }

    suspend fun onEditTrip(item: TripListItem) {
        val header = getAuthHeader()
        val id = item.id ?: throw Exception("Missing id")
        val resp = service.editTrip(header, id, item)
        if (!resp.isSuccessful) throw Exception("Edit failed")
    }

    suspend fun onDeleteTrip(item: TripListItem) {
        val header = getAuthHeader()
        val id = item.id ?: throw Exception("Missing id")
        val resp = service.deleteTrip(header, id)
        if (!resp.isSuccessful) throw Exception("Delete failed")
    }
}
