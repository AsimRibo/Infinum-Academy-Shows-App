package hr.asimr.shows_asim.fragments


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import hr.asimr.shows_asim.R
import hr.asimr.shows_asim.adapters.ShowsAdapter
import hr.asimr.shows_asim.databinding.DialogUserProfileBinding
import hr.asimr.shows_asim.databinding.FragmentShowsBinding
import hr.asimr.shows_asim.models.Show
import hr.asimr.shows_asim.viewModels.ShowsViewModel

class ShowsFragment : Fragment() {
    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ShowsAdapter
    private lateinit var email: String

    private val viewModel by viewModels<ShowsViewModel>()

    private val args by navArgs<ShowsFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentShowsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = args.email

        initShowsRecycler()
        initListeners()
    }

    private fun initToolbarMenuItemListeners() {
        binding.toolbarShows.menu.findItem(R.id.userProfile).setIcon(R.drawable.ic_profile_placeholder)

        binding.toolbarShows.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.userProfile -> initUserProfileBottomSheet()

                else -> {
                    Log.i("menuItem", "Unknown id")
                    false
                }
            }
        }
    }

    private fun initUserProfileBottomSheet(): Boolean {
        val dialog = BottomSheetDialog(requireContext())
        val bottomSheet = DialogUserProfileBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheet.root)

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
        return true
    }

    private fun changeUserImage() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivity(intent)
    }

    private fun logout() {
        val loginPreferences = requireContext().getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE)
        loginPreferences.edit {
            putBoolean(REMEMBER_ME, false)
            remove(USER_EMAIL)
        }
        findNavController().navigate(R.id.action_showsFragment_loginFragment)
    }

    private fun initListeners() {
        initToolbarMenuItemListeners()
        initButtonListeners()
    }

    private fun initButtonListeners() {
        binding.btnToggleShows.setOnClickListener {
            binding.groupEmptyState.isVisible = !binding.groupEmptyState.isVisible
            binding.groupFullState.isVisible = !binding.groupFullState.isVisible
        }
    }

    private fun initShowsRecycler() {
        viewModel.showsLiveData.observe(viewLifecycleOwner) { shows ->
            adapter = ShowsAdapter(shows) { show ->
                showClicked(show)
            }

            binding.rvShows.adapter = adapter
        }
    }

    private fun showClicked(show: Show) {
        findNavController().navigate(
            ShowsFragmentDirections.actionShowsFragmentToShowDetailsFragment(
                email,
                show
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}