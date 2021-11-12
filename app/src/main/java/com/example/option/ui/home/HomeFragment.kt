package com.example.option.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.option.R
import com.example.option.data.AppDatabase
import com.example.option.data.entities.Stock
import com.example.option.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_home.*

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

                homeAdapter = this.context?.let {
                    HomeAdapter(object: HomeAdapter.OnClickListener {
                        // catch the item click event from adapter
                        @RequiresApi(Build.VERSION_CODES.S)
                        override fun onItemClick(rID: Int, typeID: Int) {

                            homeViewModel.rID = rID
                            homeViewModel.tID = typeID

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
                                    //tv_code_show.text = homeAdapter?.getStock(rID)?.code
                                    price_input.visibility = View.GONE
                                    code_input.visibility = View.VISIBLE

                                }
                                2 -> {
                                    //tv_price_show.text = homeAdapter?.getStock(rID)?.sell.toString()
                                    code_input.visibility = View.GONE
                                    price_input.visibility = View.VISIBLE
                                }
                                3 -> {
                                    //tv_price_show.text = homeAdapter?.getStock(rID)?.buy_bottom.toString()
                                    code_input.visibility = View.GONE
                                    price_input.visibility = View.VISIBLE
                                }
                                4 -> {
                                    //tv_price_show.text = homeAdapter?.getStock(rID)?.buy_top.toString()
                                    code_input.visibility = View.GONE
                                    price_input.visibility = View.VISIBLE
                                }
                                5 -> {
                                    //tv_price_show.text = homeAdapter?.getStock(rID)?.breakthrough.toString()
                                    code_input.visibility = View.GONE
                                    price_input.visibility = View.VISIBLE
                                    //binding.homeRecyclerview.isClickable = false
                                }
                                6 -> {
                                    //tv_price_show.text = homeAdapter?.getStock(rID)?.stress.toString()
                                    code_input.visibility = View.GONE
                                    price_input.visibility = View.VISIBLE
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

                            homeAdapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                            homeViewModel.stock = homeAdapter?.getStock(rID)!!
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




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fab button
        this.fab.setOnClickListener { view ->
            AppDatabase.getDatabase(view.context).stock().addStock(Stock())
            loadDataToViewModel()
            refreshRecyclerView()
        }

        // code input
        codeInput()

        // price input
        priceInput()
    }

    override fun onResume() {
        super.onResume()

        refreshRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    // code input function
    @SuppressLint("ClickableViewAccessibility")
    private fun codeInput(){

        // press
        for (txtView in code_input_keys){
            txtView.setOnTouchListener { view, motionEvent ->
                when (motionEvent.actionMasked){
                    ACTION_DOWN -> {txtView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_background))}
                    ACTION_UP -> {txtView.setBackgroundResource(R.drawable.textview_border)}
                }
                false
            }
        }

        tv_code_a.setOnClickListener{
            tv_code_show.append(tv_code_a.text)
        }
        tv_code_b.setOnClickListener{
            tv_code_show.append(tv_code_b.text)
        }
        tv_code_c.setOnClickListener{
            tv_code_show.append(tv_code_c.text)
        }
        tv_code_d.setOnClickListener{
            tv_code_show.append(tv_code_d.text)
        }
        tv_code_e.setOnClickListener{
            tv_code_show.append(tv_code_e.text)
        }
        tv_code_f.setOnClickListener{
            tv_code_show.append(tv_code_f.text)
        }
        tv_code_g.setOnClickListener{
            tv_code_show.append(tv_code_g.text)
        }
        tv_code_h.setOnClickListener{
            tv_code_show.append(tv_code_h.text)
        }
        tv_code_i.setOnClickListener{
            tv_code_show.append(tv_code_i.text)
        }
        tv_code_j.setOnClickListener{
            tv_code_show.append(tv_code_j.text)
        }
        tv_code_k.setOnClickListener{
            tv_code_show.append(tv_code_k.text)
        }
        tv_code_l.setOnClickListener{
            tv_code_show.append(tv_code_l.text)
        }
        tv_code_m.setOnClickListener{
            tv_code_show.append(tv_code_m.text)
        }
        tv_code_n.setOnClickListener{
            tv_code_show.append(tv_code_n.text)
        }
        tv_code_o.setOnClickListener{
            tv_code_show.append(tv_code_o.text)
        }
        tv_code_p.setOnClickListener{
            tv_code_show.append(tv_code_p.text)
        }
        tv_code_q.setOnClickListener{
            tv_code_show.append(tv_code_q.text)
        }
        tv_code_r.setOnClickListener{
            tv_code_show.append(tv_code_r.text)
        }
        tv_code_s.setOnClickListener{
            tv_code_show.append(tv_code_s.text)
        }
        tv_code_t.setOnClickListener{
            tv_code_show.append(tv_code_t.text)
        }
        tv_code_u.setOnClickListener{
            tv_code_show.append(tv_code_u.text)
        }
        tv_code_v.setOnClickListener{
            tv_code_show.append(tv_code_v.text)
        }
        tv_code_w.setOnClickListener{
            tv_code_show.append(tv_code_w.text)
        }
        tv_code_x.setOnClickListener{
            tv_code_show.append(tv_code_x.text)
        }
        tv_code_y.setOnClickListener{
            tv_code_show.append(tv_code_y.text)
        }
        tv_code_z.setOnClickListener{
            tv_code_show.append(tv_code_z.text)
        }

        // delete last char | exit without save
        tv_code_back.setOnClickListener {
            if (tv_code_show.length() > 0) {
                tv_code_show.text = tv_code_show.text.dropLast(1)
            }else{
                code_input.visibility = View.GONE
            }
        }

        // save and exit
        tv_code_enter.setOnClickListener {
            if (tv_code_show.length() > 0) {
                homeViewModel.stock.code = tv_code_show.text.toString()
                AppDatabase.getDatabase(requireContext()).stock().addStock(homeViewModel.stock)
                loadDataToViewModel()
                refreshRecyclerView()
                tv_code_show.text = ""
            }
            code_input.visibility = View.GONE
        }
    }

    @SuppressLint("ClickableViewAccessibility", "ResourceType")
    @RequiresApi(Build.VERSION_CODES.S)
    private fun priceInput(){

        for (txtView in price_input_keys){
            txtView.setOnTouchListener { view, motionEvent ->
                when (motionEvent.actionMasked){
                    ACTION_DOWN -> {txtView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_background))}
                    ACTION_UP -> {txtView.setBackgroundResource(R.drawable.textview_border)}
                }
                false
            }
        }

        tv_price_1.setOnClickListener {
            tv_price_show.append(tv_price_1.text)
        }
        tv_price_2.setOnClickListener {
            tv_price_show.append(tv_price_2.text)
        }
        tv_price_3.setOnClickListener {
            tv_price_show.append(tv_price_3.text)
        }
        tv_price_4.setOnClickListener {
            tv_price_show.append(tv_price_4.text)
        }
        tv_price_5.setOnClickListener {
            tv_price_show.append(tv_price_5.text)
        }
        tv_price_6.setOnClickListener {
            tv_price_show.append(tv_price_6.text)
        }
        tv_price_7.setOnClickListener {
            tv_price_show.append(tv_price_7.text)
        }
        tv_price_8.setOnClickListener {
            tv_price_show.append(tv_price_8.text)
        }
        tv_price_9.setOnClickListener {
            tv_price_show.append(tv_price_9.text)
        }
        tv_price_0.setOnClickListener {
            if (tv_price_show.text.toString() !="0"){
                tv_price_show.append(tv_price_0.text)
            }
        }
        tv_price_dot.setOnClickListener {
            if (!tv_price_show.text.contains(tv_price_dot.text)) {
                tv_price_show.append(tv_price_dot.text)
            }
        }
        tv_price_negative.setOnClickListener {
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

        // delete last char | exit without save
        tv_price_back.setOnClickListener {
            if (tv_price_show.length() > 0){
                tv_price_show.text = tv_price_show.text.dropLast(1)
            }else{
                refreshRecyclerView()
                price_input.visibility = View.GONE
            }
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

