package com.evgenii.photosearch.detailscreen.presentation.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.evgenii.photosearch.R
import com.evgenii.photosearch.core.presentation.utils.AnimationUtils
import com.evgenii.photosearch.core.presentation.utils.PicassoUtils.Companion.loadFromUrl
import com.evgenii.photosearch.databinding.PhotoDetailFragmentBinding
import com.evgenii.photosearch.detailscreen.presentation.viewmodel.PhotoDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoDetailFragment : Fragment() {

    private val viewModel: PhotoDetailViewModel by viewModels()

    private var _binding: PhotoDetailFragmentBinding? = null
    private val binding: PhotoDetailFragmentBinding
        get() = _binding ?: throw RuntimeException("PhotosListFragmentBinding is null")

    private val args by navArgs<PhotoDetailFragmentArgs>()
    private val navController: NavController by lazy { findNavController() }

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
        setAnimationParam()
        initObservers()
        setButtonListener()
        viewModel.loadDetailInfo(photoId)
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

    private fun initObservers() =
        with(viewModel) {
            photoDetail.observe(viewLifecycleOwner) { photoDetailItem ->
                with(photoDetailItem) {
                    binding.ivUserIcon.loadFromUrl(
                        userImageURL,
                        R.drawable.placeholder_avatar
                    )
                    binding.tvUserName.text = user
                    binding.tvPhotoLikes.text = likes
                    binding.tvPhotoDownloads.text = downloads
                    binding.tvPhotoTags.text = tags
                    binding.tvPhotoComments.text = comments
                    binding.tvPhotoViews.text = views
                }
            }
            loadingProgressVisibility.observe(viewLifecycleOwner) { visibility ->
                binding.pbLoadDetailInfo.isVisible = visibility
            }
            actionShowToastError.observe(viewLifecycleOwner) {
                showToastError()
            }
            actionToBackScreen.observe(viewLifecycleOwner) {
                navController.popBackStack()
            }
            actionOpenInBrowser.observe(viewLifecycleOwner) { event ->
                event.getValue()?.let { url -> openInBrowser(url) }
            }
        }

    private fun setButtonListener() =
        binding.btnOpenInBrowser.setOnClickListener {
            viewModel.onOpenInBrowserClick()
        }

    private fun openInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun showToastError() =
        Toast.makeText(
            requireContext(),
            getString(R.string.error_load_detail),
            Toast.LENGTH_LONG
        ).show()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}