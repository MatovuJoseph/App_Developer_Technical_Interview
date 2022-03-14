package com.interswitch.appdevelopertechnicalinterview.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.interswitch.appdevelopertechnicalinterview.R
import com.interswitch.appdevelopertechnicalinterview.data.api.ApiService
import com.interswitch.appdevelopertechnicalinterview.data.model.FeeGroup
import com.interswitch.appdevelopertechnicalinterview.data.model.ItemFee
import com.interswitch.appdevelopertechnicalinterview.data.storage.LocalStorageDatabase
import com.interswitch.appdevelopertechnicalinterview.databinding.ActivityMainBinding
import com.interswitch.appdevelopertechnicalinterview.ui.adapters.FeeGroupAdapter
import org.json.JSONObject
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val db by lazy {
        LocalStorageDatabase.getDatabase(this)
    }

    private val itemFeeDao by lazy {
        db.itemFeeDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*initialize networking library*/
        AndroidNetworking.initialize(this)

        /*defaults*/
        binding.submitButton.isEnabled = false
        binding.progressBar.isVisible = false
        binding.itemFeeDetailsLayout.isVisible = false

        /*determining that state of submit with fee id input*/
        binding.feeIdInput.addTextChangedListener {
            binding.submitButton.isEnabled = it.toString().isNotEmpty()
        }

        /*perform data fetch on feed id submission*/
        binding.submitButton.setOnClickListener {
            val feedID = binding.feeIdInput.text.toString()
            getFeeDataById(feedID)
            hideKeyboardInput()

        }

    }

    /*function to hide keyboard after use*/
    private fun hideKeyboardInput() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun getFeeDataById(feeID: String?) {
        /*Accessing room database off the main thread*/
        thread {
            /*find item data from local storage*/
            val itemFee = itemFeeDao.getItemFeeById(feeID?.toInt())
            /*if item data exists, display item*/
            if (itemFee != null) {
                runOnUiThread {
                    displayItemFeeData(itemFee)
                }
            } else {
                /**/
                runOnUiThread {
                    fetchFeeDataFromServer(feeID)
                }
            }
        }

    }

    /*function to fetch item fee data by id*/
    private fun fetchFeeDataFromServer(feeID: String?) {
        binding.itemFeeDetailsLayout.isVisible = false
        binding.progressBar.isVisible = true
        AndroidNetworking.get(ApiService.baseUrl)
            .addPathParameter("feeId", feeID)
            .doNotCacheResponse()
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    //Log.d("TAG-data", "onResponse: $response")
                    binding.progressBar.isVisible = false
                    binding.feeIdInput.text?.clear()

                    when (response?.getInt("responseCode")) {
                        90000 -> {
                            val item: ItemFee =
                                Gson().fromJson(response.getString("response"), ItemFee::class.java)

                            // display item fee data
                            displayItemFeeData(item)

                            // save to local storage
                            saveItemFeeData(item)

                        }
                        else -> {
                            // display message to user from responseMessage
                            displayErrorMessage(response?.getString("responseMessage"))
                        }
                    }

                }

                override fun onError(anError: ANError?) {
                    //Log.d("TAG-data", "onError: ${anError?.errorBody}")
                    binding.progressBar.isVisible = false
                    when (anError?.errorCode) {
                        0 -> {
                            // no connection
                            displayErrorMessage("No Internet Connection, Please check your Connection and try again")
                        }
                        else -> {
                            // server error
                            displayErrorMessage("Service Failure.")
                        }
                    }

                }
            })
    }

    /*function to display error messages*/
    private fun displayErrorMessage(msg: String?) {
        MaterialAlertDialogBuilder(this)
            .setMessage(msg)
            .setPositiveButton("Okay") { d, w ->
                d.dismiss()
            }.setCancelable(false).create().show()
    }

    /*function to save retrieved Item data to local storage*/
    private fun saveItemFeeData(item: ItemFee) {
        thread {
            itemFeeDao.insertItemFee(item)
        }
    }

    /*function to display item retrieved data*/
    private fun displayItemFeeData(item: ItemFee) {
        binding.itemFeeDetailsLayout.isVisible = true

        /*main item fee data*/
        binding.name.text = item.name
        binding.vat.text = item.vat.toString()
        binding.issueDate.text = item.issueDate

        binding.providerServiceCharge.text = item.providerServiceCharge.toString()
        binding.providerServiceChargeAccount.text = item.providerServiceChargeAccount

        binding.withholdingTax.text = item.withholdingTax.toString()
        binding.withholdingTaxAccount.text = item.withholdingTaxAccount

        binding.excise.text = item.excise.toString()
        binding.exciseTaxAccount.text = item.exciseTaxAccount

        /*pay configuration data*/
        if (item.payConfiguration?.isNotEmpty() == true) {
            val payConfiguration = item.payConfiguration[0]
            binding.source.text = payConfiguration.source
            binding.payType.text = payConfiguration.payType
            binding.payValue.text = payConfiguration.payValue.toString()
            binding.maximumFeeBorn.text = payConfiguration.maximumFeeBorn.toString()
            binding.minimumFeeBorn.text = payConfiguration.minimumFeeBorn.toString()
            binding.bandCode.text = payConfiguration.bandCode
            binding.hasExcise.text = payConfiguration.hasExcise.toString()
            binding.isPayVAT.text = payConfiguration.isPayVAT.toString()
            binding.hasWithholdingTax.text = payConfiguration.hasWithholdingTax.toString()
            binding.hasServiceCharge.text = payConfiguration.hasServiceCharge.toString()
        }


        /*fee group data*/
        binding.feeGroupLayout.isVisible = item.feeGroups?.isEmpty() == false
        binding.feeGroupRv.adapter = item.feeGroups?.let { FeeGroupAdapter(this, it) }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear_memory -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Clear Cache")
                    .setMessage("You're attempting to clear all your saved search records!")
                    .setPositiveButton("Confirm") { d, w ->
                        thread {
                            itemFeeDao.clearItemFeeRecords()
                            runOnUiThread {
                                d.dismiss()
                                displayErrorMessage("All Saved Records Cleared!")
                            }
                        }
                    }.setNeutralButton("Cancel") { d, w ->
                        d.dismiss()
                    }.setCancelable(false)
                    .create().show()

            }
        }
        return true
    }

}