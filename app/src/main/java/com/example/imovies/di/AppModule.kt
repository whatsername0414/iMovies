package com.example.imovies.di

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.imovies.common.Constant.PREFERENCES_STORE_NAME
import com.example.imovies.data.repository.common.local.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        Database::class.java,
        "imovies"
    ).build()

    @Singleton
    @Provides
    fun provideMovieDao(db: Database) = db.movieDao()

    @Singleton
    @Provides
    fun provideFavoriteDao(db: Database) = db.favoriteDao()

    @Singleton
    @Provides
    fun providePreferences(@ApplicationContext app: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                app.preferencesDataStoreFile(PREFERENCES_STORE_NAME)
            })
    }

    @Singleton
    @Provides
    fun provideAppPreferences(@ApplicationContext app: Context): SharedPreferences {
        return app.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    }
}