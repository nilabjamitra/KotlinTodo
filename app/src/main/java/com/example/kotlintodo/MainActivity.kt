@file:Suppress("DEPRECATION")

package com.example.kotlintodo

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kotlintodo.fragments.NotificationPermissionHelper
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var notificationManager: NotificationManager

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Notification setup
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        handleNotificationIntent(intent)

        // Edge-to-edge display
        enableEdgeToEdge()

        // Initialize views
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        // Toolbar setup
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.title = ""
        toolbar.subtitle = ""

        // Navigation setup
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Drawer toggle setup
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.nav_open,
            R.string.nav_close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // AppBar configuration
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.signInFragment),
            drawerLayout
        )

        // Connect components
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Window insets handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Navigation item selection handling
        navView.setNavigationItemSelectedListener { menuItem ->
            handleNavigationItemSelected(menuItem)
            true
        }

        // Toolbar visibility control
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val currentToolbar = findViewById<Toolbar>(R.id.toolbar)

            when (destination.id) {
                R.id.homeFragment -> {
                    currentToolbar?.visibility = View.VISIBLE
                    actionBarDrawerToggle.isDrawerIndicatorEnabled = true
                    supportActionBar?.show()
                }
                else -> {
                    currentToolbar?.visibility = View.GONE
                    actionBarDrawerToggle.isDrawerIndicatorEnabled = false
                    supportActionBar?.hide()
                }
            }
        }

        // Check notification permissions
        checkNotificationPermission()
    }

    private fun handleNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_home -> navController.navigate(R.id.homeFragment)
            R.id.nav_logout -> handleLogout()
        }
        drawerLayout.closeDrawers()
        return true
    }

    private fun handleLogout() {
        auth.signOut()
        navController.navigate(R.id.signInFragment)
        Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }


    // Notification handling methods
    private fun handleNotificationIntent(intent: Intent?) {
        if (intent?.getBooleanExtra("notification_data", false) == true) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().apply {
                putString("PENDING_INTENT", intent.toUri(0))
                apply()
            }
            if (auth.currentUser != null) {
                navController.navigate(R.id.homeFragment)
            } else {
                navController.navigate(R.id.signInFragment)
            }
            intent.removeExtra("from_notification")
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                NotificationPermissionHelper.areNotificationsEnabled(this) -> {
                    // Permissions already granted
                }
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) -> {
                    showPermissionRationale()
                }
                else -> {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        NOTIFICATION_PERMISSION_REQUEST
                    )
                }
            }
        }
    }

    private fun showPermissionRationale() {
        AlertDialog.Builder(this)
            .setTitle("Enable Notifications")
            .setMessage("Allow notifications to receive important todo reminders")
            .setPositiveButton("Allow") { _, _ ->
                NotificationPermissionHelper.requestNotificationPermission(this)
            }
            .setNegativeButton("Later", null)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            NOTIFICATION_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this, "Notifications enabled", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        const val NOTIFICATION_PERMISSION_REQUEST = 1001
    }
}