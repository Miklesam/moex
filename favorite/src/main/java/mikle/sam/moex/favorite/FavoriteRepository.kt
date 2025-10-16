package mikle.sam.moex.favorite

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mikle.sam.moex.database.favorite.FavoriteDao
import mikle.sam.moex.database.favorite.FavoriteTicker
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(
    private val favoriteDao: FavoriteDao
) {
    suspend fun addFavorite(ticker: String) {
        withContext(Dispatchers.IO) {
            favoriteDao.insert(FavoriteTicker(ticker = ticker))
        }
    }

    fun favorites(): Flow<List<FavoriteTicker>> = favoriteDao.getAll()
}


