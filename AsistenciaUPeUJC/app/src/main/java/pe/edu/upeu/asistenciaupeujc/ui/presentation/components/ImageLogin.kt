package pe.edu.upeu.asistenciaupeujc.ui.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pe.edu.upeu.asistenciaupeujc.R

@Composable
fun ImageLogin(modifier: Modifier = Modifier) {
    val painter: Painter = painterResource(id = R.drawable.logo1)

    Image(
        painter = painter,
        contentDescription = "Image Login",
        modifier = modifier
            .size(300.dp),
        alignment = Alignment.BottomCenter
    )
}