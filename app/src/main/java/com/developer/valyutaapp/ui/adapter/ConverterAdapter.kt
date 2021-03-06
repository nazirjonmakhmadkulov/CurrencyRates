package com.developer.valyutaapp.ui.adapter

import android.text.Editable
import android.text.TextWatcher
import com.developer.valyutaapp.domain.entities.Valute
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import com.developer.valyutaapp.R
import com.developer.valyutaapp.core.base.BaseViewHolder
import com.developer.valyutaapp.core.base.Item
import com.developer.valyutaapp.core.base.ItemBase
import com.developer.valyutaapp.databinding.ItemConverterBinding
import com.developer.valyutaapp.utils.ImageResource
import com.developer.valyutaapp.utils.Utils.decFormat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ConverterAdapter(
    private val onChangeValute: (Double, Int) -> Unit,
    private val onItemValuteClick: (Valute) -> Unit,
) : ItemBase<ItemConverterBinding, Valute> {
    private var posSelect: Int = -1
    override fun isRelativeItem(item: Item): Boolean = item is Valute
    override fun getLayoutId() = R.layout.item_converter
    override fun getViewHolder(
        layoutInflater: LayoutInflater, parent: ViewGroup,
    ): BaseViewHolder<ItemConverterBinding, Valute> {
        val binding = ItemConverterBinding.inflate(layoutInflater, parent, false)
        return FavoriteViewHolder(binding, onItemValuteClick)
    }

    override fun getDiffUtil() = diffUtil
    private val diffUtil = object : DiffUtil.ItemCallback<Valute>() {
        override fun areItemsTheSame(oldItem: Valute, newItem: Valute) =
            oldItem.valId == newItem.valId

        override fun areContentsTheSame(oldItem: Valute, newItem: Valute) = oldItem == newItem
    }

    inner class FavoriteViewHolder(
        binding: ItemConverterBinding, val onItemValuteClick: (Valute) -> Unit,
    ) : BaseViewHolder<ItemConverterBinding, Valute>(binding) {
        override fun onBind(item: Valute) = with(binding) {
            super.onBind(item)
            val bt = ImageResource.getImageRes(binding.root.context, item.charCode)
            iconValute.setImageBitmap(bt)
            charCode.text = item.charCode
            if (bindingAdapterPosition != posSelect)
                moneyConvert.setText(decFormat(item.value.toDouble()))
            moneyConvert.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s!!.isNotBlank()) {
                        posSelect = bindingAdapterPosition
                        GlobalScope.launch {
                            delay(200)
                            onChangeValute(
                                moneyConvert.text.toString().trim().toDouble(),
                                bindingAdapterPosition
                            )
                        }
                    }
                }
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            itemView.setOnClickListener {
                onItemValuteClick(item)
            }
        }

        override fun onBind(item: Valute, payloads: List<Any>) {
            super.onBind(item, payloads)
            binding.name.text = item.value
            //println("payloads $item")
        }
    }
}