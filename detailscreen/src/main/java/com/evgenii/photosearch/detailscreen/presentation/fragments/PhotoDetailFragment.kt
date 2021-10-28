package com.evgenii.photosearch.detailscreen.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.evgenii.photosearch.R
import com.evgenii.photosearch.core.presentation.fragments.BaseFragment
import com.evgenii.photosearch.core.presentation.utils.AnimationUtils
import com.evgenii.photosearch.core.presentation.utils.ImageLoaderUtils.Companion.loadFromUrl
import com.evgenii.photosearch.core.presentation.utils.InternetUtils
import com.evgenii.photosearch.databinding.PhotoDetailFragmentBinding
import com.evgenii.photosearch.detailscreen.presentation.model.*
import com.evgenii.photosearch.detailscreen.presentation.viewmodel.PhotoDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoDetailFragment : BaseFragment<PhotoDetailScreenState, Commands, PhotoDetailViewModel>(
    PhotoDetailViewModel::class.java
) {

    private var _binding: PhotoDetailFragmentBinding? = null
    private val binding: PhotoDetailFragmentBinding
        get() = _binding ?: throw RuntimeException("PhotosListFragmentBinding is null")

    private val args by navArgs<PhotoDetailFragmentArgs>()

    private val photoId by lazy { args.photoId }
    private val largeImageUrl by lazy { args.largeImageUrl }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = PhotoDetailFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setAnimationParam()
        setButtonListener()
        viewModel.onInitScreen(photoId)
    }

    override fun updateScreen(screenState: PhotoDetailScreenState) {
        with(screenState) {
            with(binding) {
                pbLoadDetailInfo.isVisible = progressBarVisibility
                ivUserIcon.loadFromUrl(userImageURL, R.drawable.placeholder_avatar)
                tvUserName.text = userName
                layoutInformation.tvPhotoLikes.text = likeCount
                layoutInformation.tvPhotoDownloads.text = downloadCount
                tvPhotoTags.text = tags
                layoutInformation.tvPhotoComments.text = commentCount
                layoutInformation.tvPhotoViews.text = viewsCount
                pbLoadDetailInfo.isVisible = progressBarVisibility
            }
        }
    }

    override fun executeCommand(command: Commands) {
        when (command) {
            is NavigateToBackScreen -> navController.popBackStack()
            is OpenInBrowser -> InternetUtils.openInBrowser(requireContext(), command.path)
            is ShowToast -> showToast(getString(R.string.error_load_detail))
        }
    }

    private fun setToolbar() {
        val toolbar = binding.toolbarPhotoDetail
        toolbar.setNavigationIcon(R.drawable.ic_back_button)
        toolbar.setNavigationOnClickListener {
            viewModel.onBackButtonPressed()
        }
    }

    /**
     * The pixabay library generates a new url to the image, with each request.
     * In order not to download it again and get it from the cache,
     * as well as the animation was displayed smoothly, the link is passed in parameters.
     *
     * API Pixabay Documentation- [https://pixabay.com/api/docs/]
     */
    private fun setAnimationParam() {
        binding.ivPhoto.loadFromUrl(largeImageUrl, R.drawable.placeholder_main_image)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        ViewCompat.setTransitionName(
            binding.ivPhoto,
            AnimationUtils.getUniqueTransitionLargePhoto(photoId)
        )
        ViewCompat.setTransitionName(
            binding.ivUserIcon,
            AnimationUtils.getUniqueTransitionUserPhoto(photoId)
        )
    }

    private fun setButtonListener() =
        binding.btnOpenInBrowser.setOnClickListener {
            viewModel.onOpenInBrowserClick()
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}