package hr.asimr.shows_asim.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import hr.asimr.shows_asim.BuildConfig
import hr.asimr.shows_asim.R
import hr.asimr.shows_asim.adapters.ShowsAdapter
import hr.asimr.shows_asim.databinding.DialogUserProfileBinding
import hr.asimr.shows_asim.databinding.FragmentShowsBinding
import hr.asimr.shows_asim.models.Show
import hr.asimr.shows_asim.networking.ApiModule
import hr.asimr.shows_asim.utils.FileUtils
import hr.asimr.shows_asim.viewModels.ACCESS_TOKEN
import hr.asimr.shows_asim.viewModels.CLIENT
import hr.asimr.shows_asim.viewModels.ShowsViewModel
import hr.asimr.shows_asim.viewModels.UID
import java.io.File

class ShowsFragment : Fragment() {
    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ShowsAdapter
    private lateinit var email: String

    private lateinit var loginPreferences: SharedPreferences
    private val viewModel by viewModels<ShowsViewModel>()

    private val args by navArgs<ShowsFragmentArgs>()

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            loadImage(
                binding.toolbarShows.findViewById(R.id.toolbarProfileImage) as ShapeableImageView,
                FileUtils.getImageFile(requireContext())
            )
        } else {
            Log.e("Image", "Image not taken")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentShowsBinding.inflate(layoutInflater)
        loginPreferences = requireContext().getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE)
        initApiModule()
        return binding.root
    }

    private fun initApiModule() {
        val accessToken = loginPreferences.getString(ACCESS_TOKEN, "").orEmpty()
        val client = loginPreferences.getString(CLIENT, "").orEmpty()
        val uid = loginPreferences.getString(UID, "").orEmpty()

        if(accessToken.isEmpty() || client.isEmpty() || uid.isEmpty()){
            findNavController().navigate(R.id.action_showsFragment_loginFragment)
        }
        else{
            ApiModule.initRetrofit(requireContext(), accessToken, client, uid)
        }
    }


override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    email = args.email

    initShowsRecycler()
    initListeners()
    viewModel.getAllShows()
    loadImage(binding.toolbarShows.findViewById(R.id.toolbarProfileImage) as ShapeableImageView, FileUtils.getImageFile(requireContext()))
    initShowsObserving()
    initSuccessObserving()
}

private fun initSuccessObserving() {
    viewModel.success.observe(viewLifecycleOwner) { success ->
        if (!success) {
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}

private fun initShowsObserving() {
    viewModel.showsLiveData.observe(viewLifecycleOwner) { shows ->
        adapter.updateShows(shows)
    }
}

private fun loadImage(view: ImageView, file: File?) {
    file?.let { image ->
        Glide
            .with(requireContext())
            .load(image)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .centerCrop()
            .into(view)
    }
}

private fun initToolbarMenuItemListeners() {
    (binding.toolbarShows.findViewById(R.id.toolbarProfileImage) as ShapeableImageView).setOnClickListener {
        initUserProfileBottomSheet()
    }
}

private fun initUserProfileBottomSheet() {
    val dialog = BottomSheetDialog(requireContext())
    val bottomSheet = DialogUserProfileBinding.inflate(layoutInflater)
    dialog.setContentView(bottomSheet.root)

    loadImage(bottomSheet.ivUserProfile, FileUtils.getImageFile(requireContext()))
    bottomSheet.tvEmail.text = email
    bottomSheet.btnLogout.setOnClickListener {
        dialog.dismiss()
        AlertDialog.Builder(requireContext()).apply {
            setTitle(R.string.logout)
            setMessage(R.string.logout_confirm_message)
            setNegativeButton(R.string.cancel, null)
            setPositiveButton(R.string.logout) { _, _ -> logout() }
            show()
        }
    }
    bottomSheet.btnChangeProfilePhoto.setOnClickListener {
        dialog.dismiss()
        changeUserImage()
    }
    dialog.show()
}

private fun changeUserImage() {
    val photoFile: File? = FileUtils.createImageFile(requireContext())
    photoFile?.also {
        val photoURI: Uri = FileProvider.getUriForFile(
            requireContext(),
            BuildConfig.APPLICATION_ID + ".fileProvider",
            it
        )
        takePictureLauncher.launch(photoURI)
    }
}

private fun logout() {
    val loginPreferences = requireContext().getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE)
    loginPreferences.edit {
        putBoolean(REMEMBER_ME, false)
        remove(USER_EMAIL)
        remove(ACCESS_TOKEN)
        remove(UID)
        remove(CLIENT)
    }
    findNavController().navigate(R.id.action_showsFragment_loginFragment)
}

private fun initListeners() {
    initToolbarMenuItemListeners()
}

private fun initShowsRecycler() {
    adapter = ShowsAdapter(listOf()) { show ->
        showClicked(show)
    }
    binding.rvShows.adapter = adapter
}

private fun showClicked(show: Show) {
    findNavController().navigate(
        ShowsFragmentDirections.actionShowsFragmentToShowDetailsFragment(
            show.id
        )
    )
}

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}
}