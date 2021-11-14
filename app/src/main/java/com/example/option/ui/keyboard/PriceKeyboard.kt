package com.example.option.ui.keyboard

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import com.example.option.R
import com.example.option.databinding.KeyboardBinding

@SuppressLint("ClickableViewAccessibility")
class PriceKeyboard(){
    private var _binding: KeyboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    init {

        for (txtView in binding.priceInputKeys){
            txtView.setOnTouchListener { view, motionEvent ->
                when (motionEvent.actionMasked){
                    MotionEvent.ACTION_DOWN -> {txtView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.gray_background))}
                    MotionEvent.ACTION_UP -> {txtView.setBackgroundResource(R.drawable.textview_border)}
                }
                false
            }
        }
        binding.tvPrice1.setOnClickListener {
            binding.tvPriceShow.append(binding.tvPrice1.text)
        }
        binding.tvPrice2.setOnClickListener {
            binding.tvPriceShow.append(binding.tvPrice2.text)
        }
        binding.tvPrice3.setOnClickListener {
            binding.tvPriceShow.append(binding.tvPrice3.text)
        }
        binding.tvPrice4.setOnClickListener {
            binding.tvPriceShow.append(binding.tvPrice4.text)
        }
        binding.tvPrice5.setOnClickListener {
            binding.tvPriceShow.append(binding.tvPrice5.text)
        }
        binding.tvPrice6.setOnClickListener {
            binding.tvPriceShow.append(binding.tvPrice6.text)
        }
        binding.tvPrice7.setOnClickListener {
            binding.tvPriceShow.append(binding.tvPrice7.text)
        }
        binding.tvPrice8.setOnClickListener {
            binding.tvPriceShow.append(binding.tvPrice8.text)
        }
        binding.tvPrice9.setOnClickListener {
            binding.tvPriceShow.append(binding.tvPrice9.text)
        }
        binding.tvPrice0.setOnClickListener {
            if (binding.tvPriceShow.text.toString() !="0"){
                binding.tvPriceShow.append(binding.tvPrice0.text)
            }
        }
        binding.tvPriceDot.setOnClickListener {
            if (!binding.tvPriceShow.text.contains(binding.tvPriceDot.text)) {
                binding.tvPriceShow.append(binding.tvPriceDot.text)
            }
        }
        binding.tvPriceCalc.setOnClickListener {

            //binding.priceInput.visibility = View.GONE
        }
        binding.tvPriceBack.setOnClickListener {
            if (binding.tvPriceShow.length() > 0){
                binding.tvPriceShow.text = binding.tvPriceShow.text.dropLast(1)
            }else{
                binding.priceInput.visibility = View.GONE
            }
        }
        binding.tvPriceEnter.setOnClickListener {
            if (binding.tvPriceShow.length() > 0){

                binding.tvPriceShow.text = ""
            }
            //val recyclerViewState: Parcelable =

            binding.priceInput.visibility = View.GONE
            //binding.homeRecyclerview.isClickable = true

        }
    }

    fun show(){
        binding.priceInput.visibility = View.VISIBLE
    }

    fun hide(){
        binding.priceInput.visibility = View.GONE
    }


}