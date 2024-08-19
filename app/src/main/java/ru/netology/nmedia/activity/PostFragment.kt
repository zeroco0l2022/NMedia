package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.service.displayNumber
import ru.netology.nmedia.viewmodel.PostViewModel

class PostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        val openPostId = viewModel.postId.value ?: findNavController().navigateUp()

        viewModel.data.observe(viewLifecycleOwner) { posts ->

            val post = posts.find { it.id == openPostId } ?: return@observe

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                viewModel.closePost()
                findNavController().navigateUp()
            }
            with(binding.openPost) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                shareButton.text = displayNumber(post.shared)
                viewsCount.text = displayNumber(post.views)
                likeButton.isChecked = post.likedByMe
                likeButton.text = displayNumber(post.likes)
                likeButton.setOnClickListener {
                    viewModel.likeById(post.id)
                }


                shareButton.setOnClickListener {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }

                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                    viewModel.shareById(post.id)
                }


                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    viewModel.removeById(post.id)
                                    viewModel.closePost()
                                    findNavController().navigateUp()
                                    true
                                }

                                R.id.edit -> {
                                    viewModel.edit(post)
                                    viewModel.closePost()
                                    findNavController().navigateUp()
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()

                }
                if (!post.video.isNullOrBlank()) {
                    video.visibility = View.VISIBLE
                    video.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                        startActivity(intent)
                    }


                }
            }

        }
        return binding.root
    }
}