package com.najwa.task.di

import android.content.Context
import com.najwa.task.Utils
import com.najwa.task.data.api.DataApi
import com.najwa.task.data.dataSource.DataRemoteDataSource
import com.najwa.task.data.repository.DataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    @Singleton
    @Named("FakeData")
    fun provideFakeData(@ApplicationContext context: Context): String =
        Utils.getJsonFromAssets(context, "getListOfFilesResponse.json")

    @Provides
    @Singleton
    fun provideDataRemoteDataSource(
        dataApi: DataApi,
    ) = DataRemoteDataSource(dataApi)

    @Provides
    @Singleton
    fun provideDataRepository(
        dataSource: DataRemoteDataSource,
    ) = DataRepository(dataSource)

}