package hu.bme.aut.onlab.tripplanner.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.onlab.tripplanner.data.network.qualifiers.SharedRetrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedBackendModule {

    @Provides
    @Singleton
    @SharedRetrofit
    fun provideSharedRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://<own address>:8080/")   //saját backend címe
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideSharedBackendService(@SharedRetrofit retrofit: Retrofit): SharedBackendService =
        retrofit.create(SharedBackendService::class.java)
}
