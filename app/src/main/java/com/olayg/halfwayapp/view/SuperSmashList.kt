package com.olayg.halfwayapp.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.olayg.halfwayapp.R
import com.olayg.halfwayapp.adapter.CharacterAdapter
import com.olayg.halfwayapp.constants.Constants
import com.olayg.halfwayapp.databinding.FragmentSuperSmashListBinding
import com.olayg.halfwayapp.model.custom.Character
import com.olayg.halfwayapp.viewmodel.SSBViewModel

class SuperSmashList : Fragment() {
    private var _binding: FragmentSuperSmashListBinding? = null
    private val binding get() = _binding!!
    private val ssbViewModel by activityViewModels<SSBViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = FragmentSuperSmashListBinding.inflate(
        inflater, container, false
    ).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeObservables()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeObservables() = with(ssbViewModel) {
        categories.observe(viewLifecycleOwner) { characters ->
            binding.rvCharacters.adapter = CharacterAdapter(characters, ::characterSelected)
        }
    }

    private fun characterSelected(character: Character) = with(findNavController()) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putStringSet(Constants.gifList, character.gifs?.map { it.url }?.toMutableSet())
            putString(Constants.charName, character.name)
            putString(Constants.charGuide, character.guide)
            apply()
        }
        val action = SuperSmashListDirections.actionSuperSmashListFragmentToSuperSmashInfoFragment(character)
        navigate(action)
    }

}