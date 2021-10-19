package com.evgenii.searchphoto.presentation.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.evgenii.searchphoto.databinding.PhotoDetailFragmentNewBinding
import com.evgenii.searchphoto.presentation.utils.AnimationUtils
import com.evgenii.searchphoto.presentation.viewmodel.PhotoDetailViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoDetailFragment : Fragment() {

    private val viewModel: PhotoDetailViewModel by viewModels()

    private var _binding: PhotoDetailFragmentNewBinding? = null
    private val binding: PhotoDetailFragmentNewBinding
        get() = _binding ?: throw RuntimeException("PhotosListFragmentBinding is null")

    private val args by navArgs<PhotoDetailFragmentArgs>()

    private val photoId by lazy { args.photoId }
    private val largeImageUrl by lazy { args.largeImageUrl }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = PhotoDetailFragmentNewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAnimationParam()
        initObservers()
        setButtonListener()
        viewModel.loadDetailInfo(photoId)
    }

    private fun setAnimationParam() {
        Picasso.get()
            .load(largeImageUrl)
            .into(binding.ivPhoto)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        ViewCompat.setTransitionName(
            binding.ivPhoto,
            AnimationUtils.getUniqueTransitionName(photoId)
        )
        ViewCompat.setTransitionName(
            binding.ivUserIcon,
            AnimationUtils.getUniqueTransitionNameAvatar(photoId)
        )

    }

    private fun setButtonListener() {
        binding.btnOpenInBrowser.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(viewModel.photoDetail.value?.pageURL)
            startActivity(intent)
        }
    }

    private fun initObservers() {
        with(viewModel) {
            photoDetail.observe(viewLifecycleOwner) { photoDetailItem ->
                with(photoDetailItem) {
                    if (userImageURL.isNotEmpty()){
                        Picasso.get()
                            .load(userImageURL)
                            .into(binding.ivUserIcon)
                    }
                    binding.tvUserName.text = user
                    binding.tvPhotoLikes.text = likes
                    binding.tvPhotoDownloads.text = downloads
                    binding.tvPhotoTags.text = tags
                    binding.tvPhotoComments.text = comments
                    binding.tvPhotoViews.text = views
                }
            }
            loadingState.observe(viewLifecycleOwner) { loadResult ->
//                when(loadResult){
//                    is EmptyResult -> TODO()
//                    is ErrorResult -> TODO()
//                    is PendingResult -> binding.pbLoadImage.isVisible = true
//                    is SuccessResult<*> -> binding.pbLoadImage.isVisible = false
//                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}