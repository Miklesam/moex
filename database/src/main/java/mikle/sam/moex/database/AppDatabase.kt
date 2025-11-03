package mikle.sam.moex.database

import androidx.room.Database
import androidx.room.RoomDatabase
import mikle.sam.moex.database.favorite.FavoriteDao
import mikle.sam.moex.database.favorite.FavoriteTicker

@Database(
    entities = [FavoriteTicker::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}





