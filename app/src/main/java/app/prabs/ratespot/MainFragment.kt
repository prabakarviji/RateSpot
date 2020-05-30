package app.prabs.ratespot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import app.prabs.ratespot.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenAuthState()
    }

    private fun listenAuthState(){
        viewModel.authenticationState.observe(viewLifecycleOwner,Observer { authenticationState ->
            when(authenticationState){
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    val intent = Intent (activity, MapsActivity::class.java)
                    activity?.startActivity(intent)
                }
                else -> {
                    findNavController().navigate(R.id.loginFragment)
                }
            }
        })

    }

}
