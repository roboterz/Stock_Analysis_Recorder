package com.example.option.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.option.data.AppDatabase
import com.example.option.data.entities.Stock
import com.example.option.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.keyboard.*
import com.example.option.R
import com.example.option.ui.keyboard.PriceKeyboard

import android.widget.EditText

import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var homeAdapter: HomeAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread {
            this.activity?.runOnUiThread {
                val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

                // specify the layout manager for recycler view
                binding.homeRecyclerview.layoutManager = linearLayoutManager
                homeAdapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

                homeAdapter = this.context?.let {
                    HomeAdapter(object: HomeAdapter.OnClickListener {
                        // catch the item click event from adapter
                        @RequiresApi(Build.VERSION_CODES.S)
                        override fun onItemClick(rID: Int, typeID: Int) {

                            homeViewModel.rID = rID
                            homeViewModel.tID = typeID
                            homeViewModel.stock = homeAdapter?.getStock(rID)!!

                            // switch to record fragment (Edit mode)
                            when (typeID){
                                0 -> {
                                    val dialogBuilder = AlertDialog.Builder(activity)

                                    dialogBuilder.setMessage("确定删除此记录？")
                                        .setCancelable(true)
                                        .setPositiveButton("确定",
                                            DialogInterface.OnClickListener{ _, _->
                                            // delete record
                                            homeAdapter?.getStock(rID)?.let { it1 ->
                                                AppDatabase.getDatabase(it).stock().deleteStock(it1)
                                            }
                                            loadDataToViewModel()
                                            refreshRecyclerView()
                                        })
                                        .setNegativeButton("取消",
                                            DialogInterface.OnClickListener{ dialog, _ ->
                                            // cancel
                                            dialog.cancel()
                                        })
                                        .setTitle("注意")

                                    // set Title Style
                                    //val titleView = layoutInflater.inflate(R.layout.popup_title,null)
                                    // set Title Text
                                    //titleView.tv_popup_title_text.text = getText(R.string.msg_Title_prompt)

                                    val alert = dialogBuilder.create()
                                    //alert.setIcon(R.drawable.ic_baseline_delete_forever_24)
                                    //alert.setCustomTitle(titleView)
                                    alert.show()
                                }
                                1 -> {
                                    PriceKeyboard(view!!).hide()
                                    codeEnter(homeViewModel.stock.code)

                                }
                                2 -> {

                                    PriceKeyboard(view!!).setValue(homeViewModel.stock.sell)
                                    PriceKeyboard(view!!).show()
                                }
                                3 -> {
                                    PriceKeyboard(view!!).setValue(homeViewModel.stock.buy_bottom)
                                    PriceKeyboard(view!!).show()
                                }
                                4 -> {
                                    PriceKeyboard(view!!).setValue(homeViewModel.stock.buy_top)
                                    PriceKeyboard(view!!).show()
                                }
                                5 -> {
                                    PriceKeyboard(view!!).setValue(homeViewModel.stock.breakthrough)
                                    PriceKeyboard(view!!).show()
                                    //binding.homeRecyclerview.isClickable = false
                                }
                                6 -> {
                                    PriceKeyboard(view!!).setValue(homeViewModel.stock.stress)
                                    PriceKeyboard(view!!).show()
                                }
                                7,8 -> {homeAdapter?.let { it1 ->
                                            AppDatabase.getDatabase(it).stock().updateStock(
                                                it1.getStock(rID))
                                            //Snackbar.make(requireView(), it1.getStock(rID).code, Snackbar.LENGTH_SHORT).show()
                                        }
                                    loadDataToViewModel()
                                    refreshRecyclerView()
                                }
                            }


                        }
                    })
                }


                binding.homeRecyclerview.adapter = homeAdapter
            }
        }.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textHome

        loadDataToViewModel()

        return root

    }




    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fab button
        this.fab.setOnClickListener { view ->
            codeEnter()
        }


        // price input
        priceInput()
        PriceKeyboard(view).initKeys()
    }

    override fun onResume() {
        super.onResume()

        refreshRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun codeEnter(str: String = ""){
        val alert = AlertDialog.Builder(context)
        val editText = EditText(activity)

        val filter = arrayOf<InputFilter>(LengthFilter(5))
        editText.filters = filter
        editText.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        editText.setText(str)

        editText.requestFocus()
        editText.selectAll()
        editText.post {
            (editText.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(editText,SHOW_IMPLICIT)
        }


        //alert.setMessage("Enter Your Message")
        alert.setTitle("Enter Stock Code")
            .setView(editText)
            .setPositiveButton("Confirm",
            DialogInterface.OnClickListener { dialog, whichButton -> //What ever you want to do with the value

                if (str =="") {
                    val stk = Stock()
                    stk.code = editText.text.toString()
                    AppDatabase.getDatabase(requireContext()).stock().addStock(stk)
                }else{
                    homeViewModel.stock.code = editText.text.toString()
                    AppDatabase.getDatabase(requireContext()).stock().addStock(homeViewModel.stock)
                }
                loadDataToViewModel()
                refreshRecyclerView()
            })
            .setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, whichButton ->
                // do nothing
            })
            .show()
    }


    private fun loadDataToViewModel() {
        homeViewModel.loadData(activity)
    }

    private fun refreshRecyclerView(){
        Thread {
            activity?.runOnUiThread {
                homeAdapter?.setList(homeViewModel.stocks)
                //binding.homeRecyclerview.adapter = homeAdapter
            }
        }.start()
    }


    @SuppressLint("ClickableViewAccessibility", "ResourceType")
    @RequiresApi(Build.VERSION_CODES.S)
    private fun priceInput(){

        tv_price_calc.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(activity)

            dialogBuilder.setMessage("确定删除此记录？")
                .setCancelable(true)
                .setPositiveButton("确定",
                    DialogInterface.OnClickListener{ _, _->
                        // delete record
                        AppDatabase.getDatabase(requireContext()).stock().deleteStock(homeViewModel.stock)
                        loadDataToViewModel()
                        refreshRecyclerView()
                    })
                .setNegativeButton("取消",
                    DialogInterface.OnClickListener{ dialog, _ ->
                        // cancel
                        dialog.cancel()
                    })
                .setTitle("注意")

            val alert = dialogBuilder.create()
            alert.show()
            price_input.visibility = View.GONE
        }


        // enter
        tv_price_enter.setOnClickListener {
            if (tv_price_show.length() > 0){
                when (homeViewModel.tID){
                    2 -> homeViewModel.stock.sell = tv_price_show.text.toString().toDouble()
                    3 -> homeViewModel.stock.buy_bottom = tv_price_show.text.toString().toDouble()
                    4 -> homeViewModel.stock.buy_top = tv_price_show.text.toString().toDouble()
                    5 -> homeViewModel.stock.breakthrough = tv_price_show.text.toString().toDouble()
                    6 -> homeViewModel.stock.stress = tv_price_show.text.toString().toDouble()
                }
                AppDatabase.getDatabase(requireContext()).stock().addStock(homeViewModel.stock)
                loadDataToViewModel()

                tv_price_show.text = ""
            }
            //val recyclerViewState: Parcelable =
            refreshRecyclerView()

            price_input.visibility = View.GONE
            //binding.homeRecyclerview.isClickable = true

        }
    }
}

