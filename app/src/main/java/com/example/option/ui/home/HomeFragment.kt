package com.example.option.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.option.R
import com.example.option.data.AppDatabase
import com.example.option.data.entities.Stock
import com.example.option.databinding.FragmentHomeBinding
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
                homeAdapter = this.context?.let {
                    HomeAdapter(object: HomeAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(rID: Int) {
                            // switch to record fragment (Edit mode)

                        }
                    })
                }
                //recyclerView_activity.adapter = vpAdapter
            }
        }.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textHome
        Thread {
        homeViewModel.loadData(activity)
    }.start()
        return root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val fab: View = view.findViewById(R.id.fab)
        this.fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //.setAction("Action", null).show()
            AppDatabase.getDatabase(view.context).stock().addStock(Stock())
        }
    }

    override fun onResume() {
        super.onResume()
        //homeViewModel.stocks
        Thread {
            activity?.runOnUiThread {
                val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

                // specify the layout manager for recycler view
                binding.homeRecyclerview.layoutManager = linearLayoutManager

                //val mlist = ArrayList<Stock>()

                //mlist.add(Stock("NVDA",3225.00,2586.00,2587.00,2998.00,2984.00,true,true))
                homeAdapter?.setList(homeViewModel.stocks)
                binding.homeRecyclerview.adapter = homeAdapter
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}