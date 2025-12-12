package hu.bme.aut.onlab.tripplanner.data.recommender

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RecommenderModule {
    @Provides
    @Singleton
    fun provideDestinationRepository(@ApplicationContext context: Context): DestinationRepository =
        DestinationRepository(context)
}
