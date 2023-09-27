@file:Suppress("DEPRECATION")

package pe.edu.upeu.asistenciaupeujc.ui.presentation.screens.users

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.gson.Gson
import pe.edu.upeu.asistenciaupeujc.R

import pe.edu.upeu.asistenciaupeujc.modelo.Usuario
import pe.edu.upeu.asistenciaupeujc.ui.navigation.Destinations
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.BottomNavigationBar
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.ConfirmDialog
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.FabItem
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.LoadingCard
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.MultiFloatingActionButton
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.Spacer
import pe.edu.upeu.asistenciaupeujc.utils.TokenUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun UsuarioUI (navegarEditarUsuario: (String) -> Unit, viewModel:
UsuarioViewModel = hiltViewModel(), navController: NavHostController
){
    val actis by viewModel.usua.observeAsState(arrayListOf())
    val isLoading by viewModel.isLoading.observeAsState(false)
    Log.i("VERX", ""+actis.size )

    MyApp(navController,
        onAddClick = {
        viewModel.addUsuario()
        navegarEditarUsuario((0).toString())
    }, onDeleteClick = {
        viewModel.deleteUsuario(it)
    }, actis, isLoading,
        onEditClick = {
            val jsonString = Gson().toJson(it)
            navegarEditarUsuario(jsonString)
        }
    )
}




@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp(navController: NavHostController,
          onAddClick: (() -> Unit)? = null,
          onDeleteClick: ((toDelete: Usuario) -> Unit)? = null,
          usuarios: List<Usuario>,
          isLoading: Boolean,
          onEditClick: ((toPersona: Usuario) -> Unit)? = null,
) {
    val context = LocalContext.current
    //val navController = rememberNavController()
    val navigationItems2 = listOf(
        Destinations.ActividadUI,
        Destinations.UsuarioUI,
        Destinations.Pantalla1,
        Destinations.Pantalla2,
        Destinations.Pantalla3
    )
    /*  val scaffoldState = rememberScaffoldState(
          drawerState = rememberDrawerState(initialValue =
          DrawerValue.Closed)
      )*/

    val fabItems = listOf(
        FabItem(
            Icons.Filled.ShoppingCart,
            "Shopping Cart"
        ) {
            val toast = Toast.makeText(context, "Hola Mundo", Toast.LENGTH_LONG) // in Activity
            toast.view!!.background.setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN)
            toast.show()
        },
        FabItem(
            Icons.Filled.Favorite,
            "Add Usuario"
        ) { onAddClick?.invoke() }
    )

    Scaffold(
        bottomBar = {
            BottomAppBar {
                BottomNavigationBar(navigationItems2, navController = navController)
            }
        },
        modifier = Modifier,
        floatingActionButton = {
            MultiFloatingActionButton(
                navController=navController,
                fabIcon = Icons.Filled.Add,
                items = fabItems,
                showLabels = true
            )
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            LazyColumn(modifier = Modifier
                .padding(top = 60.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
                .align(alignment = Alignment.TopCenter),
                //.offset(x = (16).dp, y = (-32).dp),
                userScrollEnabled= true,
            ){
                var itemCount = usuarios.size
                if (isLoading) itemCount++
                items(count = itemCount) { index ->
                    var auxIndex = index
                    if (isLoading) {
                        if (auxIndex == 0)
                            return@items LoadingCard()
                        auxIndex--
                    }
                    val usuario = usuarios[auxIndex]
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 1.dp
                        ),
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .fillMaxWidth(),
                    ) {
                        Row(modifier = Modifier.padding(8.dp)) {
                            Image(
                                modifier = Modifier
                                    .size(50.dp)
                                    //.clip(CircleShape)
                                    .clip(RoundedCornerShape(8.dp)),
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(
                                        LocalContext.current
                                    ).data(data = usuario.nombres).apply(block = fun ImageRequest.Builder.() {
                                        placeholder(R.drawable.bg)
                                        error(R.drawable.bg)
                                    }).build()
                                ),
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight
                            )
                            Spacer()
                            Column(
                                Modifier.weight(1f),
                            ) {
                                Text("${usuario.nombres} - ${usuario.apellidos}", fontWeight = FontWeight.Bold)
                                val estado = usuario.estado
                                Box {
                                    Text(
                                        "" + estado, color =
                                        MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Spacer()
                            val showDialog = remember { mutableStateOf(false) }
                            IconButton(onClick = {
                                showDialog.value = true
                            }) {
                                Icon(Icons.Filled.Delete, "Remove", tint = MaterialTheme.colorScheme.primary)
                            }
                            if (showDialog.value){
                                ConfirmDialog(
                                    message = "Esta seguro de eliminar?",
                                    onConfirm = {
                                        onDeleteClick?.invoke(usuario)
                                        showDialog.value=false
                                    },
                                    onDimins = {
                                        showDialog.value=false
                                    }
                                )
                            }

                            IconButton(onClick = {
                                Log.i("VERTOKEN", "Holas")
                                Log.i("VERTOKEN", TokenUtils.TOKEN_CONTENT)
                                onEditClick?.invoke(usuario)
                            }) {
                                Icon(
                                    Icons.Filled.Edit,
                                    contentDescription = "Editar",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }


                        }
                    }
                }
            }

        }

    }
}