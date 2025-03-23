package com.example.artspaceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtGalleryApp()
        }
    }
}

data class Artwork(
    val title: String,
    val author: String,
    val imageUrl: String
)

object ArtworkLoader {
    fun loadArtworks(context: Context): List<Artwork> {
        val inputStream = context.assets.open("gallery.json")
        val json = inputStream.bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Artwork>>() {}.type
        return Gson().fromJson(json, type)
    }
}

@Composable
fun ArtGalleryApp() {
    val artworks = ArtworkLoader.loadArtworks(LocalContext.current)
    var currentIndex by rememberSaveable { mutableStateOf(0) }
    val CinzelRegular = FontFamily(
        Font(R.font.cinzel_regular)
    )
    val artwork = artworks[currentIndex]


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val maxHeight = this.maxHeight
        val maxWidth = this.maxWidth
        val isPortrait = maxWidth < maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Art Space",
                    fontSize = 24.sp,
                    fontFamily = CinzelRegular,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(24.dp),
                            clip = false
                        )
                        .border(16.dp, Color.White, RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .wrapContentSize(Alignment.Center)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = artwork.imageUrl),
                        contentDescription = artwork.title,
                        modifier = if (isPortrait) {
                            Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        } else {
                            Modifier
                                .size(maxHeight * 0.4f)
                                .aspectRatio(1f)
                        },
                        contentScale = ContentScale.Fit
                    )
                }

                Text(
                    text = artwork.title,
                    fontSize = 24.sp,
                    fontFamily = CinzelRegular,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Text(
                    text = artwork.author,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Button(
                    onClick = {
                        if (currentIndex > 0) currentIndex--
                    },
                    modifier = if (isPortrait) {
                        Modifier.weight(1f)
                    } else {
                        Modifier.width(150.dp)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Black)
                ) {
                    Text(text = "Назад")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        if (currentIndex < artworks.size - 1) currentIndex++
                    },
                    modifier = if (isPortrait) {
                        Modifier.weight(1f)
                    } else {
                        Modifier.width(150.dp)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Black)
                ) {
                    Text(text = "Вперед")
                }
            }
        }
    }
}