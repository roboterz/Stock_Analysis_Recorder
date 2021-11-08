package com.example.option.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.option.R
import com.example.option.data.AppDatabase
import com.example.option.data.entities.Stock
import kotlin.collections.ArrayList

class HomeAdapter(
    private val onClickListener: OnClickListener
    )
    : RecyclerView.Adapter<HomeAdapter.ViewHolder>( ) {

        private var mlist: List<Stock> = ArrayList()


        // interface for passing the onClick event to fragment.
        interface OnClickListener {
            fun onItemClick(rID: Int, typeID: Int)
            // typeID, 0:code, 1:sell, 2:buy bottom, 3:buy top, 4:breakthrough, 5:stress, 6:direction, 7:star
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // inflate the custom view from xml layout file
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.home_cardview,parent,false)


            // return the view holder
            return ViewHolder(view)

        }


        //@SuppressLint("ResourceAsColor")
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // display the custom class
            mlist[position].apply {
                holder.tvCode.text = code
                holder.tvSell.text = "$sell"
                holder.tvBottomBuy.text = "$buy_bottom"
                holder.tvTopBuy.text = "$buy_top"
                holder.tvBreakthrough.text ="$breakthrough"
                holder.tvStress.text = "$stress"
                if (direction) {
                    holder.tvDirection.text = "↘️"
                    holder.tvDirection.setBackgroundColor(holder.downColor)
                }else{
                    holder.tvDirection.text = "↗️"
                    holder.tvDirection.setBackgroundColor(holder.upColor)
                }
                //holder.tvDirection.text = if (direction) "⤵️" else "⤴️"
                holder.tvStar.text = if (star) "⭐" else "⬜"


                // direction
                holder.tvDirection.setOnClickListener{
                    direction = !direction

                    if (direction) {
                        holder.tvDirection.text = "↘️"
                        holder.tvDirection.setBackgroundColor(holder.downColor)
                    }else{
                        holder.tvDirection.text = "↗️"
                        holder.tvDirection.setBackgroundColor(holder.upColor)
                    }
                    onClickListener.onItemClick(position,6)
                }

                // star
                holder.tvStar.setOnClickListener{
                    star = !star
                    holder.tvStar.text = if (star) "⭐" else "⬜"
                    onClickListener.onItemClick(position,7)
                }

                // code
                holder.tvCode.setOnClickListener{
                    onClickListener.onItemClick(position,0)
                }

                // sell
                holder.tvSell.setOnClickListener{

                    onClickListener.onItemClick(position,1)
                }

                // buy bottom
                holder.tvBottomBuy.setOnClickListener{

                    onClickListener.onItemClick(position,2)
                }

                // buy top
                holder.tvTopBuy.setOnClickListener{

                    onClickListener.onItemClick(position,3)
                }

                // breakthrough
                holder.tvBreakthrough.setOnClickListener{

                    onClickListener.onItemClick(position,4)
                }

                // stress
                holder.tvStress.setOnClickListener{

                    onClickListener.onItemClick(position,5)
                }
            }

        }


        @SuppressLint("NotifyDataSetChanged")
        fun setList(list: List<Stock> = mlist){
            mlist = list
            notifyDataSetChanged()
        }

        fun getStock(rID: Int): Stock{
            return mlist[rID]
        }


        override fun getItemCount(): Int {
            // the data set held by the adapter.
            return mlist.size
        }


        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val tvCode: TextView = itemView.findViewById(R.id.tv_code_value)
            val tvSell: TextView = itemView.findViewById(R.id.tv_sell_value)
            val tvBottomBuy: TextView = itemView.findViewById(R.id.tv_buy_bottom_value)
            val tvTopBuy: TextView = itemView.findViewById(R.id.tv_buy_top_value)
            val tvBreakthrough: TextView = itemView.findViewById(R.id.tv_breakthrough_value)
            val tvStress: TextView = itemView.findViewById(R.id.tv_stress_value)
            val tvDirection: TextView = itemView.findViewById(R.id.tv_direction_value)
            val tvStar: TextView = itemView.findViewById(R.id.tv_star_value)

            val upColor = ContextCompat.getColor(itemView.context, R.color.green)
            val downColor = ContextCompat.getColor(itemView.context, R.color.red)

        }


        // this two methods useful for avoiding duplicate item
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }
}