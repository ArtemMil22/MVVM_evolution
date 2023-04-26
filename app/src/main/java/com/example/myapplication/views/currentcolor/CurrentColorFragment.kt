package com.example.myapplication.views.currentcolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foundation.model.ErrorResult
import com.example.foundation.model.PendingResult
import com.example.foundation.model.SuccessResult
import com.example.myapplication.databinding.FragmentCurrentColorBinding
import com.example.foundation.views.BaseFragment
import com.example.foundation.views.BaseScreen
import com.example.foundation.views.screenViewModel
import com.example.myapplication.databinding.PartResultBinding
import com.example.myapplication.views.onTryAgain
import com.example.myapplication.views.renderSimpleResult

class CurrentColorFragment : BaseFragment() {

    // no arguments for this screen
    class Screen : BaseScreen

    override val viewModel by screenViewModel<CurrentColorViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentCurrentColorBinding.inflate(inflater, container, false)
        viewModel.currentColor.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onSuccess = {
                    binding.colorView.setBackgroundColor(it.value)
                }
            )
        }

        binding.changeColorButton.setOnClickListener {
            viewModel.changeColor()
        }
        binding.askPermissionsButton.setOnClickListener {
            viewModel.requestPermission()
        }

        onTryAgain(binding.root) {
            viewModel.tryAgain()
        }

        return binding.root
    }


}
//class CurrentColorFragment : BaseFragment() {
//
//    class Screen : BaseScreen
//
//    override val viewModel by screenViewModel<CurrentColorViewModel>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View {
//        val binding = FragmentCurrentColorBinding.inflate(inflater, container, false)
//            val resultBinding = PartResultBinding.bind(binding.root)
//
//
//        viewModel.currentColor.observe(viewLifecycleOwner) { result ->
//            // Будем показывать и скрывать вьюшки в зависимости от результата асинхронки
//            when (result) {
//                is PendingResult -> {
//                resultBinding.errorContainer.visibility = View.GONE
//                    resultBinding.progressBar.visibility = View.VISIBLE
//                    binding.colorContainer.visibility = View.GONE
//                    binding.changeColorButton.visibility = View.GONE
//                }
//                is ErrorResult -> {
//                    resultBinding.errorContainer.visibility = View.VISIBLE
//                    resultBinding.progressBar.visibility = View.GONE
//                    binding.colorContainer.visibility = View.GONE
//                    binding.changeColorButton.visibility = View.GONE
//                }
//                is SuccessResult -> {
//                    resultBinding.errorContainer.visibility = View.GONE
//                    resultBinding.progressBar.visibility = View.GONE
//                    binding.colorContainer.visibility = View.VISIBLE
//                    binding.changeColorButton.visibility = View.VISIBLE
//
//                    binding.colorView.setBackgroundColor(result.data.value)
//                }
//            }
//        }
//
//        binding.changeColorButton.setOnClickListener {
//            viewModel.changeColor()
//        }
//
//        // обработчкик на кнопку
//        onTryAgain(binding.root){
//            viewModel.tryAgain()
//        }
//
//        return binding.root
//    }
//
//}
//
////            renderResult(
//                root = binding.root,
//                result = result,
//                onSuccess = {
//                    binding.colorContainer.visibility = View.VISIBLE
//                    binding.changeColorButton.visibility = View.VISIBLE
//                },
//                onError = {
//                    resultBinding.errorContainer.visibility = View.VISIBLE
//                          },
//                onPending = {
//                    resultBinding.progressBar.visibility = View.VISIBLE
//                })
//        }
//
//        binding.changeColorButton.setOnClickListener {
//            viewModel.changeColor()
//        }
//
//        return binding.root
//    }
//
//
//}
//
//        renderSimpleResult(
//                root = binding.root,
//                result = result,
//                onSuccess = {
//                    binding.colorView.setBackgroundColor(it.value)
//                })