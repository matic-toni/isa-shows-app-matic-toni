package com.example.shows_tonimatic

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar

fun Fragment.preparePermissionsContract(
    onPermissionsGranted: () -> Unit,
    onPermissionsDenied: () -> Unit = {
        activity?.let {
            showPermissionsDeniedSnackbar(it)
        }
    }
) = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsMap ->
    permissionsMap.entries.forEach {
        if (it.value) {
            onPermissionsGranted()
        } else {
            onPermissionsDenied()
        }
    }
}

private fun showPermissionsDeniedSnackbar(
    fragmentActivity: FragmentActivity,
    message: String = "Please check your permissions in Settings."
) {
    fragmentActivity.apply {
        Snackbar.make(
            findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        )
            .setAction(
                "Settings"
            ) {
                startActivity(getApplicationSettingsIntent(this))
            }.show()
    }
}

private fun getApplicationSettingsIntent(
    fragmentActivity: FragmentActivity
): Intent {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:${fragmentActivity.packageName}")
    return intent
}
