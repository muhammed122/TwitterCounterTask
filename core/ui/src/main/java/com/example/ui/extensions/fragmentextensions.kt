package com.example.ui.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment


fun Fragment.showToast(message: Any?) {
    Toast.makeText(requireContext(), "$message", Toast.LENGTH_LONG).show()
}