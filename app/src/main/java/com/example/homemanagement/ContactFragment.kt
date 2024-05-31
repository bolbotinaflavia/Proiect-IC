package com.example.homemanagement

import AboutFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

class ContactFragment(aboutFragment: AboutFragment) : Fragment() {
    private lateinit var email: View
    private lateinit var phone: View
    private lateinit var backButton: View

    private var parentFrag = aboutFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)
        email = view.findViewById(R.id.contactEmail)
        phone = view.findViewById(R.id.contactPhone)
        backButton = view.findViewById(R.id.backButton)
        backButton.setOnClickListener{
            returnToAboutPage()
        }
        return view
    }

    private fun returnToAboutPage() {
        backButton.visibility = View.GONE
        parentFrag.showAllButtons()
        parentFragmentManager.popBackStack()
    }
}
