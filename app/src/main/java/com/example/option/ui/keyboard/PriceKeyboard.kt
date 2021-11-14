package com.example.option.ui.keyboard

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.iterator
import com.example.option.R
import kotlinx.android.synthetic.main.keyboard.view.*

@SuppressLint("ClickableViewAccessibility")
class PriceKeyboard(view: View){
    private val key0: TextView = view.findViewById<TextView>(R.id.tv_price_0)
    private val keyBack: TextView = view.findViewById<TextView>(R.id.tv_price_back)
    private val keyDot: TextView = view.findViewById<TextView>(R.id.tv_price_dot)
    private val keyEnter: TextView = view.findViewById<TextView>(R.id.tv_price_enter)
    private val keyCalc: TextView = view.findViewById<TextView>(R.id.tv_price_calc)
    private val keyClear: TextView = view.findViewById<TextView>(R.id.tv_price_clear)
    private val numberDisplay: TextView = view.findViewById<TextView>(R.id.tv_price_show)
    private val keyboardPanel: ConstraintLayout = view.findViewById<ConstraintLayout>(R.id.price_input)
    private val keys: ConstraintLayout = view.findViewById<ConstraintLayout>(R.id.price_input_keys)

    fun initKeys() {

        keys.forEach{
            it.setOnTouchListener { view, motionEvent ->
                when (motionEvent.actionMasked){
                    MotionEvent.ACTION_DOWN -> {it.setBackgroundColor(ContextCompat.getColor(view.context, R.color.keyboard_frame))}
                    MotionEvent.ACTION_UP -> {it.setBackgroundResource(R.drawable.keyboard_border)}
                }
                false
            }
            when (it.tag){
                "1","2","3","4","5","6","7","8","9" -> {
                    it.setOnClickListener{ _ ->
                        if (numberDisplay.text.toString().toDouble() == 0.0){
                            numberDisplay.text = it.tag.toString()
                        }else {
                            numberDisplay.append(it.tag.toString())
                        }
                    }
                }
            }
        }

        key0.setOnClickListener {
            if (numberDisplay.text.toString() !="0"){
                numberDisplay.append(key0.text)
            }
        }
        keyDot.setOnClickListener {
            if (!numberDisplay.text.contains(keyDot.text.toString())) {
                numberDisplay.append(keyDot.text)
            }
        }
        keyCalc.setOnClickListener {

            //binding.priceInput.visibility = View.GONE
        }
        keyClear.setOnClickListener {
            numberDisplay.text = "0"
        }
        keyBack.setOnClickListener {
            if (numberDisplay.length() > 1){
                numberDisplay.text = numberDisplay.text.dropLast(1)
            }else{
                numberDisplay.text = "0"
            }
        }
        keyboardPanel.setOnClickListener {
            if (numberDisplay.length() > 0){

                numberDisplay.text = "0"
            }
            //val recyclerViewState: Parcelable =

            keyboardPanel.visibility = View.GONE
            //binding.homeRecyclerview.isClickable = true

        }

         
    }

    fun show(){
        keyboardPanel.visibility = View.VISIBLE
    }

    fun hide(){
        keyboardPanel.visibility = View.GONE
    }

    fun getResult(): String{
        return numberDisplay.text.toString()
    }

    fun setValue(value: Double){
        numberDisplay.text = value.toString()
    }

}