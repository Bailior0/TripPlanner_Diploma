package hu.bme.aut.onlab.tripplanner.data.disk.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseAppModule {

    @Singleton
    @Provides
    fun provideTripsDatabase( @ApplicationContext app: Context) = Room.databaseBuilder(
        app,
        TripListDatabase::class.java,
        "triplist-list"
    ).build()

    @Singleton
    @Provides
    fun provideYourDao(db: TripListDatabase) = db.tripListItemDao()
}