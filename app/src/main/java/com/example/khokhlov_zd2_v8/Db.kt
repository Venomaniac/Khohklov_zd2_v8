package com.example.khokhlov_zd2_v8

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "orders")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "dish_name") val dishName: String,
    val price: Int,
    val quantity: Int
)

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY id DESC")
    fun getAll(): LiveData<List<OrderItem>>

    @Insert
    fun insert(item: OrderItem)

    @Delete
    fun delete(item: OrderItem)

    @Query("DELETE FROM orders")
    fun clear()
}

@Database(entities = [OrderItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "food-database"
                ).build().also { INSTANCE = it }
            }
    }
}
