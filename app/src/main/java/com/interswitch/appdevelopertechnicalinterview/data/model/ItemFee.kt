package com.interswitch.appdevelopertechnicalinterview.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_fee")
data class ItemFee (
    val name: String?,
    val payConfiguration: MutableList<PayConfiguration>?,
    val isInclusiveInAmount:Boolean?,
    val hasProviderServiceCharge:Boolean?,
    val overrideBillerFee: Boolean?,
    val vat: Float?,
    val providerServiceCharge: Float?,
    val taxAccount: String?,
    val withholdingTax: Float?,
    val withholdingTaxAccount: String?,
    val excise: Float?,
    val exciseTaxAccount: String?,
    val providerServiceChargeAccount: String?,
    val feeGroups: MutableList<FeeGroup>?,
    val itemFeeMapSettings:MutableList<ItemFeeMapSetting>?,
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val isActive: Boolean?,
    val issueDate: String?
)

data class PayConfiguration(
    val source: String?,
    val payType: String?,
    val payValue: Float?,
    val maximumFeeBorn: Float?,
    val minimumFeeBorn: Float?,
    val itemFeeMapSettingId: Int?,
    val bandCode: String?,
    val hasExcise: Boolean?,
    val isPayVAT: Boolean?,
    val hasWithholdingTax: Boolean?,
    val hasServiceCharge: Boolean?
)

data class FeeGroup(
    val name: String?,
    val description: String?,
    val itemId: Int?,
    val itemFeeId: Int?,
    val item: String?,
    val clientFees: String?,
    val id: Int?,
    val isActive: Boolean?,
    val issueDate: String?
)

data class ItemFeeMapSetting(
    val itemFeeId: Int?,
    val bandCode: String?,
    val id: Int?,
    val isActive: Boolean?,
    val issueDate: String?
)