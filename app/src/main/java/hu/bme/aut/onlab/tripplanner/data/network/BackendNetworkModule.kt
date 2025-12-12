package hu.bme.aut.onlab.tripplanner.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.onlab.tripplanner.data.network.qualifiers.TripRetrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackendNetworkModule {
    @Provides
    @Singleton
    @TripRetrofit
    fun provideTripBackendRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://<own address>:8080/") //saját backend címe
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTripBackendService(@TripRetrofit retrofit: Retrofit): TripBackendService =
        retrofit.create(TripBackendService::class.java)
}
