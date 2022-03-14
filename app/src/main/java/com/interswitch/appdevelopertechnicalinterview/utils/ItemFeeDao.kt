package com.interswitch.appdevelopertechnicalinterview.utils

import androidx.room.*
import com.interswitch.appdevelopertechnicalinterview.data.model.ItemFee

@Dao
interface ItemFeeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItemFee(itemFee: ItemFee)

    @Query("select * from item_fee where id = :itemId")
    fun getItemFeeById(itemId: Int?): ItemFee?

    @Query("delete from item_fee")
    fun clearItemFeeRecords()
}