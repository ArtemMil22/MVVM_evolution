package com.example.myapplication.views.changecolor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemColorBinding
import com.example.myapplication.model.colors.NamedColor
import java.util.Collections.emptyList

/**
 * Адаптер для отображения списка доступных цветов
 * Обратный вызов прослушивателя @param, который уведомляет о действиях
 * пользователя над элементами в списке, подробности в [Listener].
 */
class ColorsAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<ColorsAdapter.Holder>(), View.OnClickListener {

    var items: List<NamedColorListItem> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onClick(v: View) {
        val item = v.tag as NamedColor
        listener.onColorChosen(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemColorBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val namedColor = items[position].namedColor
        val selected = items[position].selected
        with(holder.binding) {
            root.tag = namedColor
            colorNameTextView.text = namedColor.name
            colorView.setBackgroundColor(namedColor.value)
            selectedIndicatorImageView.visibility = if (selected) View.VISIBLE else View.GONE
        }
    }

    override fun getItemCount(): Int = items.size

    class Holder(
        val binding: ItemColorBinding
    ) : RecyclerView.ViewHolder(binding.root)


    interface Listener {
        /**
         * Вызывается, когда пользователь выбирает указанный цвет
         * @param namedColor цвет, выбранный пользователем
         */
        fun onColorChosen(namedColor: NamedColor)
    }

}