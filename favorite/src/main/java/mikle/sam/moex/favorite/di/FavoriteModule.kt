package mikle.sam.moex.favorite.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mikle.sam.moex.database.favorite.FavoriteDao
import mikle.sam.moex.favorite.FavoriteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoriteModule {

    @Provides
    @Singleton
    fun provideRepository(dao: FavoriteDao): FavoriteRepository = FavoriteRepository(dao)
}


