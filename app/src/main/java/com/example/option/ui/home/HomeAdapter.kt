package com.example.option.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.option.OnSwipeTouchListener
import com.example.option.R
import com.example.option.data.AppDatabase
import com.example.option.data.entities.Stock
import kotlinx.android.synthetic.main.home_cardview.view.*
import kotlin.collections.ArrayList

class HomeAdapter(
    private val onClickListener: OnClickListener
    )
    : RecyclerView.Adapter<HomeAdapter.ViewHolder>( ) {

        private var mlist: List<Stock> = ArrayList()


        // interface for passing the onClick event to fragment.
        interface OnClickListener {
            fun onItemClick(rID: Int, typeID: Int)
            // typeID, 1:code, 2:sell, 3:buy bottom, 4:buy top, 5:breakthrough, 6:stress, 7:direction, 8:star
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
                    onClickListener.onItemClick(position,7)
                }

                // star
                holder.tvStar.setOnClickListener{
                    star = !star
                    holder.tvStar.text = if (star) "⭐" else "⬜"
                    onClickListener.onItemClick(position,8)
                }

                // code
                holder.tvCode.setOnClickListener{
                    onClickListener.onItemClick(position,1)
                }

                // sell
                holder.tvSell.setOnClickListener{
                    onClickListener.onItemClick(position,2)
                }

                // buy bottom
                holder.tvBottomBuy.setOnClickListener{
                    onClickListener.onItemClick(position,3)
                }

                // buy top
                holder.tvTopBuy.setOnClickListener{
                    onClickListener.onItemClick(position,4)
                }

                // breakthrough
                holder.tvBreakthrough.setOnClickListener{
                    onClickListener.onItemClick(position,5)
                }

                // stress
                holder.tvStress.setOnClickListener{
                    onClickListener.onItemClick(position,6)
                }

                /*
                holder.cardView.setOnTouchListener(object: OnSwipeTouchListener() {
                    override fun onSwipeLeft() {
                        super.onSwipeLeft()
                        holder.homeItemMenu.visibility = View.VISIBLE

                        holder.homeItemMenuDelete.setOnClickListener {
                            onClickListener.onItemClick(position,0)
                            holder.homeItemMenu.visibility = View.GONE
                        }
                    }

                    override fun onTouch(v: View, event: MotionEvent): Boolean {
                        return super.onTouch(v, event)

                        onClickListener.onItemClick(position,6)
                    }
                })

                 */

                // whole item
                /*
                holder.cardView.setOnTouchListener(object: OnSwipeTouchListener() {
                    override fun onSwipeLeft() {
                        super.onSwipeLeft()
                        holder.homeItemMenu.visibility = View.VISIBLE

                        holder.homeItemMenuDelete.setOnClickListener {
                            onClickListener.onItemClick(position,0)
                            holder.homeItemMenu.visibility = View.GONE
                        }
                    }
                })

                 */


                /*
                holder.homeItem.setOnLongClickListener {
                    holder.homeItemMenu.visibility = View.VISIBLE

                    holder.homeItemMenuDelete.setOnClickListener {
                        //mlist.drop(position)
                        onClickListener.onItemClick(position,0)
                        holder.homeItemMenu.visibility = View.GONE
                    }

                    false
                }

                 */


            }

        }


        @SuppressLint("NotifyDataSetChanged")
        fun setList(list: List<Stock> = mlist){
            mlist = list
            notifyDataSetChanged()
        }

        @SuppressLint("NotifyItemChanged")
        fun setItem(idx: Int, item: Stock){
            mlist[idx].code = item.code
            mlist[idx].star = item.star
            mlist[idx].sell = item.sell
            mlist[idx].breakthrough = item.breakthrough
            mlist[idx].buy_bottom = item.buy_bottom
            mlist[idx].buy_top = item.buy_top
            mlist[idx].direction = item.direction
            mlist[idx].stress = item.stress
            notifyItemChanged(idx)
        }

        fun getStock(rID: Int): Stock{
            return mlist[rID]
        }


        override fun getItemCount(): Int {
            // the data set held by the adapter.
            return mlist.size
        }


        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val tvCode: TextView = itemView.tv_code_value
            val tvSell: TextView = itemView.tv_sell_value
            val tvBottomBuy: TextView = itemView.tv_buy_bottom_value
            val tvTopBuy: TextView = itemView.tv_buy_top_value
            val tvBreakthrough: TextView = itemView.tv_breakthrough_value
            val tvStress: TextView = itemView.tv_stress_value
            val tvDirection: TextView = itemView.tv_direction_value
            val tvStar: TextView = itemView.tv_star_value
            val homeItem: ConstraintLayout = itemView.layout_home_item
            val homeItemMenu: ConstraintLayout = itemView.layout_home_Item_menu
            val homeItemMenuDelete: TextView = itemView.tv_home_item_delete
            val cardView: CardView = itemView.trans_card_view

            val upColor = ContextCompat.getColor(itemView.context, R.color.green)
            val downColor = ContextCompat.getColor(itemView.context, R.color.red)
            val activeColor = ContextCompat.getColor(itemView.context, R.color.yellow_active)
            val originColor = ContextCompat.getColor(itemView.context, R.color.design_default_color_background)

        }


        // this two methods useful for avoiding duplicate item
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }
}