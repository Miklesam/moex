package mikle.sam.moex.database.favorite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tickers")
data class FavoriteTicker(
    @PrimaryKey val ticker: String
)


