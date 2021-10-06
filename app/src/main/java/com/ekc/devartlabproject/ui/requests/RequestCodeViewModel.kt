package com.ekc.devartlabproject.ui.requests

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.ekc.devartlabproject.Constants.IsDelivered
import com.ekc.devartlabproject.Constants.SharedPrefName

class RequestCodeViewModel(application: Application) : AndroidViewModel(application) {

    fun validate(value: String?): Boolean {
        (value == "1234").apply {

            val sharedPref = getApplication<Application>().getSharedPreferences(
                SharedPrefName,
                Context.MODE_PRIVATE
            )
            with(sharedPref.edit()) {
                putBoolean(IsDelivered, this@apply)
                Log.e("RequestCodeViewModel", "validate this@apply: ${this@apply} ")
                apply()

            }
            return this
        }

    }
}