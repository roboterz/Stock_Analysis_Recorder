package com.example.option.ui.home

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.option.data.AppDatabase
import com.example.option.data.entities.Stock
import kotlinx.coroutines.flow.Flow

class HomeViewModel : ViewModel() {

    var stocks: List<Stock> = ArrayList()

    fun loadData(activity: FragmentActivity?){
        stocks = AppDatabase.getDatabase(activity!!).stock().getStocks()
    }
}