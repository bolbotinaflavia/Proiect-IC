package com.example.homemanagement

import AboutFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

class ContactFragment : Fragment() {
    private lateinit var email: View
    private lateinit var phone: View
    private lateinit var backButton: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)
        email = view.findViewById(R.id.contactEmail)
        phone = view.findViewById(R.id.contactPhone)
        backButton = view.findViewById(R.id.backButton)
        showAllButtons()
        backButton.setOnClickListener{
            hideAllButtons()
            returnToAboutPage()
        }
        return view
    }

    private fun returnToAboutPage() {
        childFragmentManager.commit {
            replace(R.id.contact_page, AboutFragment())
        }
    }

    private fun hideAllButtons() {
        email.visibility = View.GONE;
        phone.visibility = View.GONE;
        backButton.visibility = View.GONE;
    }

    private fun showAllButtons() {
        email.visibility = View.VISIBLE;
        phone.visibility = View.VISIBLE;
        backButton.visibility = View.VISIBLE;
    }
}
