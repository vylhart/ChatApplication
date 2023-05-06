package com.example.chatapplication.presentation.screens.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.chatapplication.R
import com.example.chatapplication.domain.model.Contact
import com.example.chatapplication.presentation.Screen
import com.example.chatapplication.presentation.composables.BackGroundCompose
import com.example.chatapplication.presentation.composables.getFeatureColor
import com.example.chatapplication.presentation.screens.main.BottomBar

@Composable
fun ContactScreen(navController: NavHostController, viewModel: ContactViewModel = hiltViewModel()) {
    val state by viewModel.contactList.collectAsState()
    PermissionScreen(viewModel)
    ContactScreenContent(navController = navController, state.toList()) {
        viewModel.joinChannel(it) { channelID ->
            navController.navigate(Screen.MessageScreen.route + "/$channelID")
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ContactScreenContent(
    navController: NavHostController,
    state: List<Contact>,
    onClick: (Contact) -> Unit,
) {
    Scaffold(topBar = {
        BottomBar(
            navController = navController,
            featureColor = getFeatureColor()
        )
    }) {
        BackGroundCompose {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state) { contact ->
                    ContactItem(contact = contact, onClick)
                }
            }
        }
    }
}

@Composable
fun ContactItem(contact: Contact, onClick: (Contact) -> Unit) {
    Spacer(modifier = Modifier.padding(10.dp))
    Row(modifier = Modifier
        .clickable { onClick(contact) }
        .fillMaxWidth()
        .height(45.dp))

    {
        Image(
            painter = painterResource(id = R.drawable.ic_user),
            contentDescription = "User",
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(3.dp)
                .weight(5f)
        ) {
            Text(text = contact.name)
            Text(text = contact.number)
        }
    }
}

@Composable
fun PermissionScreen(viewModel: ContactViewModel) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("ExampleScreen", "PERMISSION GRANTED")
            viewModel.getContacts()
        } else {
            Log.d("ExampleScreen", "PERMISSION DENIED")
        }
    }
    val context = LocalContext.current


    when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) -> {
            viewModel.getContacts()
        }
        else -> {
            SideEffect {
                launcher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewContact() {
    ContactItem(contact = Contact("Shashank", "8874857282"), onClick = {})
}