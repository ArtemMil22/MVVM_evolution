package com.example.myapplication.views.changecolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foundation.views.BaseFragment
import com.example.foundation.views.BaseScreen
import com.example.foundation.views.HasScreenTitle
import com.example.foundation.views.screenViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentChangeColorBinding
import com.example.myapplication.views.onTryAgain
import com.example.myapplication.views.renderSimpleResult
import kotlinx.coroutines.launch

/**
 * Экран для изменения цвета.
 * 1) Отображает список доступных цветов
 * 2) Позволяет выбрать нужный цвет
 * 3) Выбранный цвет сохраняется только после нажатия кнопки "Сохранить"
 * 4) Текущий выбор сохраняется через [SavedStateHandle] (см. [ChangeColorViewModel])
 */
class ChangeColorFragment : BaseFragment(), HasScreenTitle {
    /**
     * Этот экран имеет 1 аргумент: идентификатор цвета,
     * который будет отображаться как выбранный.
     */
    class Screen(
        val currentColorId: Long,
    ) : BaseScreen

    override val viewModel by screenViewModel<ChangeColorViewModel>()

    /**
     * Пример динамического заголовка экрана
     */
    override fun getScreenTitle(): String? = viewModel.screenTitle.value

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentChangeColorBinding.inflate(
            inflater, container, false
        )

        val adapter = ColorsAdapter(viewModel)
        setupLayoutManager(binding, adapter)

        binding.saveButton.setOnClickListener { viewModel.onSavePressed() }
        binding.cancelButton.setOnClickListener { viewModel.onCancelPressed() }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { result ->
                    renderSimpleResult(binding.root, result) { viewState ->

                        adapter.items = viewState.colorsList
                        // видимость кнопок
                        binding.saveButton.visibility =
                            if (viewState.showSaveButton) View.VISIBLE else View.INVISIBLE
                        binding.cancelButton.visibility =
                            if (viewState.showCancelButton) View.VISIBLE else View.INVISIBLE

                        binding.saveProgressGroup.visibility =
                            if (viewState.showSaveProgressBar) View.VISIBLE else View.GONE
                        binding.saveProgressBar.progress = viewState.saveProgressPercentage
                        binding.savingPercentageTextView.text = viewState.saveProgressPercentageMessage

                    }
                }
            }
        }

        viewModel.screenTitle.observe(viewLifecycleOwner) {
            // если заголовок экрана изменен -> необходимо уведомлять об обновлениях
            notifyScreenUpdates()
        }

        onTryAgain(binding.root) {
            viewModel.tryAgain()
        }

        return binding.root
    }

    private fun setupLayoutManager(
        binding: FragmentChangeColorBinding,
        adapter: ColorsAdapter,
    ) {
        // кол-во колонок от ширины списка
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = binding.root.width
                val itemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
                val columns = width / itemWidth
                binding.colorsRecyclerView.adapter = adapter
                binding.colorsRecyclerView.layoutManager =
                    GridLayoutManager(requireContext(), columns)
            }
        })
    }
}