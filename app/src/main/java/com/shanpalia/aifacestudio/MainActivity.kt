package com.shanpalia.aifacestudio

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private val Mint = Color(0xFF0E8060)
private val MintLight = Color(0xFFE9F6F0)
private val Ink = Color(0xFF18352C)
private val Muted = Color(0xFF71847B)
private val Page = Color(0xFFF7FAF8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(247, 250, 248)
        window.navigationBarColor = android.graphics.Color.rgb(247, 250, 248)
        window.decorView.systemUiVisibility =
            android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
            android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        setContent { FaceStudioApp() }
    }
}

private data class StudioTool(val title: String, val subtitle: String, val icon: ImageVector)

@Composable
fun FaceStudioApp() {
    var tab by remember { mutableStateOf("Home") }
    var activeTool by remember { mutableStateOf<StudioTool?>(null) }
    var scene by remember { mutableStateOf<Uri?>(null) }
    var portrait by remember { mutableStateOf<Uri?>(null) }
    var showBackendInfo by remember { mutableStateOf(false) }

    val scenePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { scene = it }
    val portraitPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { portrait = it }

    MaterialTheme(colorScheme = lightColorScheme(primary = Mint, secondary = Color(0xFF67A88D), background = Page, surface = Color.White, onSurface = Ink)) {
        Surface(Modifier.fillMaxSize(), color = Page) {
            if (activeTool != null) {
                ToolScreen(activeTool!!.title, scene, portrait,
                    { scenePicker.launch(arrayOf("image/*", "video/*")) },
                    { portraitPicker.launch(arrayOf("image/*")) },
                    { activeTool = null }, { showBackendInfo = true })
            } else {
                Scaffold(containerColor = Page, bottomBar = {
                    NavigationBar(containerColor = Color.White) {
                        listOf("Home" to Icons.Default.Home, "History" to Icons.Default.History, "Profile" to Icons.Default.Person).forEach { (label, icon) ->
                            NavigationBarItem(selected = tab == label, onClick = { tab = label },
                                icon = { Icon(icon, contentDescription = label) }, label = { Text(label) },
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = Mint, selectedTextColor = Mint, indicatorColor = MintLight))
                        }
                    }
                }) { padding ->
                    when (tab) {
                        "Home" -> HomeScreen(Modifier.padding(padding)) { activeTool = it }
                        "History" -> EmptyScreen(Modifier.padding(padding), Icons.Default.History, "Your creations", "Your processed results will appear here after an AI service is connected.")
                        else -> EmptyScreen(Modifier.padding(padding), Icons.Default.Person, "AI Face Studio", "PaliaAPK HUB\nDeveloper By ShanPalia")
                    }
                }
            }
            if (showBackendInfo) AlertDialog(onDismissRequest = { showBackendInfo = false },
                title = { Text("AI service not connected") },
                text = { Text("AISWAPFACE currently documents a browser-based service. No public native API contract or credentials have been verified. Obtain official API documentation and permission from its operator before enabling real photo, video or GIF processing. This app will not show fake results.") },
                confirmButton = { TextButton(onClick = { showBackendInfo = false }) { Text("OK") } })
        }
    }
}

@Composable
private fun HomeScreen(modifier: Modifier = Modifier, onTool: (StudioTool) -> Unit) {
    val tools = listOf(
        StudioTool("Photo Swap", "Swap faces in photos", Icons.Default.Image),
        StudioTool("Video Swap", "Face swap in video", Icons.Default.Videocam),
        StudioTool("GIF Swap", "Animated GIF workflow", Icons.Default.Movie),
        StudioTool("Multiple Faces", "Select several people", Icons.Default.Groups)
    )
    Column(modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Spacer(Modifier.height(22.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("PaliaAPK HUB", color = Mint, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                Text("AI Face Studio", color = Ink, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
                Text("Create. Transform. Keep it yours.", color = Muted, fontSize = 13.sp)
            }
            Box(Modifier.size(48.dp).clip(CircleShape).background(MintLight), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Face, contentDescription = null, tint = Mint, modifier = Modifier.size(28.dp))
            }
        }
        Spacer(Modifier.height(22.dp))
        Card(colors = CardDefaults.cardColors(containerColor = Mint), shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp)) {
                Text("Your creative studio", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text("Choose a tool and add media from your device.", color = Color.White.copy(alpha = .9f), fontSize = 13.sp)
                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Native Android experience", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Text("Choose a tool", color = Ink, fontSize = 19.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f)) {
            items(tools) { tool ->
                Card(modifier = Modifier.fillMaxWidth().height(148.dp).clickable { onTool(tool) },
                    shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
                        Box(Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(MintLight), contentAlignment = Alignment.Center) {
                            Icon(tool.icon, contentDescription = null, tint = Mint, modifier = Modifier.size(25.dp))
                        }
                        Column {
                            Text(tool.title, color = Ink, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(tool.subtitle, color = Muted, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
        Text("Developer By ShanPalia", color = Muted, fontSize = 11.sp, modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 8.dp))
    }
}

@Composable
private fun ToolScreen(title: String, scene: Uri?, portrait: Uri?, onPickScene: () -> Unit, onPickPortrait: () -> Unit, onBack: () -> Unit, onConnect: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Text(title, fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Ink)
        }
        Spacer(Modifier.height(12.dp))
        Text("Step 1 · Choose original media", color = Ink, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        UploadCard("Original photo / video", "Choose the scene you want to edit", scene, onPickScene)
        Spacer(Modifier.height(18.dp))
        Text("Step 2 · Choose replacement face", color = Ink, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        UploadCard("Replacement portrait", "Choose a clear portrait with permission", portrait, onPickPortrait)
        Spacer(Modifier.height(18.dp))
        Card(colors = CardDefaults.cardColors(containerColor = MintLight), shape = RoundedCornerShape(16.dp)) {
            Column(Modifier.padding(14.dp)) {
                Text("Privacy & consent", color = Ink, fontWeight = FontWeight.Bold)
                Text("Only use media you own or have permission to edit. Obtain consent from identifiable people. Do not use results to deceive, harass or impersonate.", color = Muted, fontSize = 12.sp)
            }
        }
        Spacer(Modifier.weight(1f))
        Button(onClick = onConnect, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(16.dp)) {
            Icon(Icons.Default.Cloud, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("AI processing connection")
        }
        Text("Backend connection required for real swaps.", color = Muted, fontSize = 11.sp, modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp))
    }
}

@Composable
private fun UploadCard(title: String, subtitle: String, uri: Uri?, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            if (uri != null) AsyncImage(model = uri, contentDescription = title, modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)))
            else Box(Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)).background(MintLight), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = Mint, modifier = Modifier.size(30.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(if (uri == null) title else "File selected", color = Ink, fontWeight = FontWeight.Bold)
                Text(if (uri == null) subtitle else uri.lastPathSegment ?: "Selected media", color = Muted, fontSize = 11.sp, maxLines = 2)
            }
            Icon(Icons.Default.UploadFile, contentDescription = "Choose file", tint = Mint)
        }
    }
}

@Composable
private fun EmptyScreen(modifier: Modifier, icon: ImageVector, title: String, message: String) {
    Column(modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(76.dp).clip(CircleShape).background(MintLight), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Mint, modifier = Modifier.size(38.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text(title, color = Ink, fontSize = 21.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(message, color = Muted, fontSize = 13.sp)
    }
}
