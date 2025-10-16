package mikle.sam.moex.database.favorite

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteTicker: FavoriteTicker)

    @Query("SELECT * FROM favorite_tickers ORDER BY ticker ASC")
    fun getAll(): Flow<List<FavoriteTicker>>
}


