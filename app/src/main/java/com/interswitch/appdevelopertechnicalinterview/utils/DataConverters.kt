package com.interswitch.appdevelopertechnicalinterview.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.interswitch.appdevelopertechnicalinterview.data.model.FeeGroup
import com.interswitch.appdevelopertechnicalinterview.data.model.ItemFeeMapSetting
import com.interswitch.appdevelopertechnicalinterview.data.model.PayConfiguration

class DataConverters {

    /*payConfiguration dataType converters in Room Database*/
    @TypeConverter
    fun listToPayConfigurationString(list: MutableList<PayConfiguration>?): String?{
        return Gson().toJson(list)
    }
    @TypeConverter
    fun payConfigurationToList(string: String?): MutableList<PayConfiguration>?{
        return Gson().fromJson(string, object : TypeToken<MutableList<PayConfiguration>>(){}.type)
    }

    /*feeGroups dataType Converters in Room Database*/
    @TypeConverter
    fun feeGroupsToString(list: MutableList<FeeGroup>?) : String?{
        return Gson().toJson(list)
    }
    @TypeConverter
    fun feeGroupStringToList(string: String?) : MutableList<FeeGroup>?{
        return Gson().fromJson(string, object : TypeToken<MutableList<FeeGroup>>(){}.type)
    }

    /*itemFeeMapSettings data Type Converters in Room Database*/
    @TypeConverter
    fun itemFeeMapSettingsToString(list: MutableList<ItemFeeMapSetting>?): String?{
        return Gson().toJson(list)
    }
    @TypeConverter
    fun itemFeeMapSettingsStringToList(string: String?): MutableList<ItemFeeMapSetting>?{
        return Gson().fromJson(string, object :TypeToken<MutableList<ItemFeeMapSetting>>(){}.type)
    }
}