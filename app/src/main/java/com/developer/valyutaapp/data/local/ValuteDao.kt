package com.developer.valyutaapp.data.local

import androidx.room.*
import com.developer.valyutaapp.domain.entities.Valute
import kotlinx.coroutines.flow.Flow

@Dao
interface ValuteDao {

    @Query("SELECT * FROM valute WHERE valId=:valId")
    fun getValuteById(valId: Int): Valute

    @Query("SELECT EXISTS(SELECT * FROM valute WHERE valId = :valId)")
    fun getValuteExist(valId: Int): Boolean

    @Query("SELECT * FROM valute")
    fun getAllValutes(): Flow<List<Valute>>

    @Query("SELECT * FROM valute WHERE favoritesConverter = 1")
    fun getAllConverterValutes(): Flow<List<Valute>>

    @Query("SELECT * FROM valute WHERE favoritesValute = 1")
    fun getAllFavoritesValutes(): Flow<List<Valute>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertValute(valute: Valute)

    @Update
    fun updateValute(valutes: Valute)

    @Query("UPDATE valute SET charCode=:code, nominal=:nomi, name=:name, value=:value, dates=:dates WHERE valId=:id")
    fun updateValuteFromRemote(
        code: String, nomi: Int, name: String, value: String, dates: String, id: Int
    )

    @Delete
    fun deleteValute(valutes: Valute)

    @Query("DELETE FROM valute")
    fun deleteAllValutes()
}