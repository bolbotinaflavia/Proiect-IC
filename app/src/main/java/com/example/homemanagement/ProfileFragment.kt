package com.example.homemanagement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    private lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DBHelper(requireContext())

        val welcomeMessage: TextView = view.findViewById(R.id.welcomeMessage)
        val usernameText: TextView = view.findViewById(R.id.usernameText)
        val emailText: TextView = view.findViewById(R.id.emailText)
        val logoutButton: Button = view.findViewById(R.id.logoutButton)
        val editButton: ImageButton = view.findViewById(R.id.editButton)

        val sharedPref = activity?.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userEmail = sharedPref?.getString("user_email", "user@example.com")


        val cursor = dbHelper.getUserData(userEmail ?: "")
        if (cursor.moveToFirst()) {
            val userName = cursor.getString(cursor.getColumnIndexOrThrow("username"))

            welcomeMessage.text = getString(R.string.hello_username, userName)
            usernameText.text = userName
            emailText.text = userEmail
        }
        cursor.close()

        logoutButton.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        editButton.setOnClickListener {
            // edit button
        }


    }
}
