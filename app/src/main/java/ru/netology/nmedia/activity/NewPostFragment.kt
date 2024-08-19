package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {
    companion object {
        var Bundle.textArg by StringArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        arguments?.textArg?.let(binding.edit::setText)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        binding.ok.setOnClickListener {
            val content = binding.edit.text.toString()
            if (binding.edit.text.isNotBlank()) {
                viewModel.changeContent(content)
                viewModel.save()
            }
            findNavController().navigateUp()
        }

        return binding.root
    }


}
