package com.example.codechallenge.ui

import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import com.example.codechallenge.adapters.RatesAdapter
import com.example.codechallenge.interfaces.DataListener
import com.example.codechallenge.local.getCurrencies
import com.example.codechallenge.local.getLastUpdateTime
import com.example.codechallenge.local.getRates
import com.example.codechallenge.local.setCurrencies
import com.example.codechallenge.local.setLastUpdateTime
import com.example.codechallenge.local.setRates
import com.example.codechallenge.model.RatesModel
import com.example.codechallenge.utils.Utils
import com.google.gson.Gson
import com.market.codechallenge.R
import com.market.codechallenge.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.regex.Matcher
import java.util.regex.Pattern


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), DataListener {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var ratesAdapter: RatesAdapter

    private lateinit var ratesHM: HashMap<String, Double>
    private val currencyList: ArrayList<String> = ArrayList()
    private val ratesList: ArrayList<RatesModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        mainViewModel.setListener(this)
        setRatesRecyclerView()
        setCurrenciesSpinner()
        setAmountTextWatcher()
        getCurrenciesData()
        getRatesData()
    }

    private fun getRatesData() {
        when {
            getLastUpdateTime() == 0L -> {
                mainViewModel.getLatestRates()
                Toast.makeText(applicationContext, "first update", Toast.LENGTH_SHORT).show()
            }

            Utils.getDifferenceInSeconds(getLastUpdateTime(), System.currentTimeMillis()) > 30L -> {
                mainViewModel.getLatestRates()
                Toast.makeText(applicationContext, "update before ${Utils.getDifferenceInSeconds(getLastUpdateTime(), System.currentTimeMillis())}", Toast.LENGTH_LONG).show()
            }

            else -> {
                setRatesData(getRates()!!)
            }
        }
    }

    private fun getCurrenciesData() {
        if (getCurrencies() == null) {
            mainViewModel.getAllCurrencies()
        } else {
            setCurrenciesData(getCurrencies())
        }
    }

    private fun setAmountTextWatcher() {
        binding.etAmount.doAfterTextChanged {
            try {

                it?.let {
                    if (it.toString().isNotEmpty()) {
                        ratesAdapter.setAmount(it.toString().toDouble())
                    } else {
                        ratesAdapter.setAmount(1.0)
                    }
                }
            } catch (e: NumberFormatException) {
                binding.etAmount.setText("")
                return@doAfterTextChanged
            }
        }
    }

    private fun setRatesRecyclerView() {
        binding.rvCurrencies.layoutManager = GridLayoutManager(applicationContext, 3)
        ratesAdapter = RatesAdapter(ratesList)
        binding.rvCurrencies.adapter = ratesAdapter
    }

    private fun setCurrenciesSpinner() {
        val adapter = ArrayAdapter(applicationContext, R.layout.list_item_spinner_dropdown, currencyList)
        binding.tvCurrency.setAdapter(adapter)
        binding.tvCurrency.setDropDownBackgroundDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.bg_round_4_white1))

        binding.tvCurrency.setOnItemClickListener { parent, _, position, _ ->
            ratesAdapter.setExchange(ratesHM.getValue(parent.getItemAtPosition(position).toString()))
        }
    }


    private fun setCurrenciesData(data: String?) {
        val currencyHM = Gson().fromJson(data, HashMap::class.java) as HashMap<String, String>
        currencyList.clear()
        currencyList.addAll(currencyHM.keys.toTypedArray().toList())
        currencyList.sortBy { it }
        setCurrenciesSpinner()
    }

    private fun setRatesData(rates: String) {
        val json = JSONObject(rates)
        ratesHM = Gson().fromJson(json.get("rates").toString(), HashMap::class.java) as HashMap<String, Double>
        ratesList.clear()
        ratesHM.forEach { (key, value) ->
            ratesList.add(RatesModel(name = key, exchangeRate = value))
        }
        ratesList.sortBy { it.name }
        ratesAdapter.notifyDataSetChanged()
    }

    override fun onCurrenciesData(data: String) {
        setCurrencies(data)
        setCurrenciesData(data)
    }

    override fun onRatesData(data: String) {
        setRates(data)
        setRatesData(data)
        setLastUpdateTime(System.currentTimeMillis())
    }
}

internal class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) : InputFilter {
    private val mPattern: Pattern

    init {
        mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?")
    }

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): String? {
        val matcher: Matcher = mPattern.matcher(dest)
        return if (!matcher.matches()) "" else null
    }
}