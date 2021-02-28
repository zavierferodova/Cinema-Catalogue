package com.zavierdev.cinemacatalogue.ui.home.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.zavierdev.cinemacatalogue.R

class DeveloperInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_developer_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val edtDeveloperWebPage: TextInputEditText = view.findViewById(R.id.edt_developer_web_page)
        edtDeveloperWebPage.setOnClickListener {
            val webUrl = getString(R.string.developer_webpage)
            val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(webUrl))
            activity?.startActivity(intent)
        }
    }
}