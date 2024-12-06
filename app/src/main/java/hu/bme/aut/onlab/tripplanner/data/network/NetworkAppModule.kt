package hu.bme.aut.onlab.tripplanner.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.onlab.tripplanner.BuildConfig
import hu.bme.aut.onlab.tripplanner.data.mapdirections.MapDirectionsService
import hu.bme.aut.onlab.tripplanner.data.mapdirections.MapsRepository
import hu.bme.aut.onlab.tripplanner.data.mapdirections.MapsRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkAppModule {
    private const val SERVICE_URL = "https://api.openweathermap.org"

    @Provides
    @Singleton
    @Named("Weather")
    fun provideOkHttpClientWeather(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val original = it.request()
            val httpUrl = original.url
            val newHttpUrl = httpUrl.newBuilder().addQueryParameter("appid", BuildConfig.APP_ID).build()
            val request = original.newBuilder().url(newHttpUrl).build()
            it.proceed(request)
        }
        .build()

    @Provides
    @Singleton
    @Named("Weather")
    fun provideRetrofitWeather(@Named("Weather") okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(SERVICE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApi(@Named("Weather") retrofit: Retrofit): WeatherApi = retrofit.create(WeatherApi::class.java)

    private const val MAPS_API_BASE_URL = "https://maps.googleapis.com/maps/api/"

    @Singleton
    @Provides
    @Named("Maps")
    fun provideOkHttpClientMaps(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    @Named("Maps")
    fun provideRetrofitMaps(
        @Named("Maps") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MAPS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Named("Maps")
    fun providesMapDirectionsService(
        @Named("Maps") retrofit: Retrofit
    ): MapDirectionsService = retrofit.create(MapDirectionsService::class.java)

    @Provides
    @Singleton
    fun providesMapsRepository(
        @Named("Maps") mapDirectionsService: MapDirectionsService,
    ): MapsRepository = MapsRepositoryImpl(mapDirectionsService)
}