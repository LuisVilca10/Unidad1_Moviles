package pe.edu.upeu.asistenciaupeujc.ui.presentation.screens.users

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.github.k0shk0sh.compose.easyforms.BuildEasyForms
import com.github.k0shk0sh.compose.easyforms.EasyFormsResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pe.edu.upeu.asistenciaupeujc.modelo.Actividad
import pe.edu.upeu.asistenciaupeujc.modelo.ComboModel
import pe.edu.upeu.asistenciaupeujc.modelo.Usuario
import pe.edu.upeu.asistenciaupeujc.ui.navigation.Destinations
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.Spacer
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.form.AccionButtonCancel
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.form.AccionButtonSuccess
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.form.ComboBox
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.form.ComboBoxTwo
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.form.DNITextField
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.form.DatePickerCustom
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.form.DropdownMenuCustom
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.form.EmailTextField
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.form.MyEasyFormsCustomStringResult
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.form.MyFormKeys
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.form.NameTextField
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.form.PasswordTextField
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.form.TimePickerCustom
import pe.edu.upeu.asistenciaupeujc.ui.presentation.screens.actividad.ActividadFormViewModel
import pe.edu.upeu.asistenciaupeujc.utils.TokenUtils

@Composable
fun UserForm(
    text: String,
    darkMode: MutableState<Boolean>,
    navController: NavHostController,
    viewModel: UserFormViewModel = hiltViewModel()
) {
    val usuariodb: Usuario
    if (text!="0"){
        usuariodb = Gson().fromJson(text, Usuario::class.java)
    }else{
        usuariodb= Usuario(0,"","", "","","","","","","",)
    }
    val isLoading by viewModel.isLoading.observeAsState(false)
    formulario(usuariodb.id!!,
        darkMode,
        navController,
        usuariodb,
        viewModel
    )

}



@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MissingPermission",
    "CoroutineCreationDuringComposition"
)
@Composable
fun formulario(
    id:Long,
    darkMode: MutableState<Boolean>,
    navController: NavHostController,
    usuario: Usuario,
    viewModel: UserFormViewModel
){

    Log.i("VERRR", "d: "+usuario?.id!!)
    val person= Usuario(0,"","", "","","","","","","")
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var locationCallback: LocationCallback? = null
    var fusedLocationClient: FusedLocationProviderClient? = null
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(
        context)
    locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            for (lo in p0.locations) {
                Log.e("LATLONX", "Lat: ${lo.latitude} Lon: ${lo.longitude}")
                person.dni=lo.latitude.toString()
                person.dni=lo.longitude.toString()
            }
        }
    }
    scope.launch{
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

        Log.e("LATLON", "Lat: ${person.dni} Lon: ${person.dni}")
        delay(1500L)
        if (fusedLocationClient != null) {
            fusedLocationClient!!.removeLocationUpdates(locationCallback);
            fusedLocationClient = null;
        }

    }

    Scaffold(modifier = Modifier.padding(top = 60.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)){
        BuildEasyForms { easyForm ->
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {


                var listEv = listOf(
                    ComboModel("Activo","Activo"),
                    ComboModel("Desactivo","Desactivo"),
                )
                NameTextField( easyForm, text =usuario.nombres, "Nombre", key = MyFormKeys.NAME )
                NameTextField( easyForm, text =usuario.apellidos, "Apellido", key = MyFormKeys.APE_MAT )
                EmailTextField(easyForms = easyForm, text =usuario.correo , label = "Correo" , tipo = usuario.correo )
                ComboBoxTwo(easyForm = easyForm, "Estado:", usuario.estado, listEv)
                DNITextField(easyForms = easyForm, text = usuario.dni , label = "DNI" )
                var listE = listOf(
                    ComboModel("SI","SI"),
                    ComboModel("NO","NO"),
                )
                ComboBox(easyForm = easyForm, "Offlinex:", usuario?.offlinex!!, listE)

                PasswordTextField(easyForms = easyForm, text = usuario.password , label = "Contraseña" )
                NameTextField(easyForms = easyForm, text = usuario.perfilPrin, label = "Perfil", key = MyFormKeys.APE_PAT )


                Row(Modifier.align(Alignment.CenterHorizontally)) {
                    AccionButtonSuccess(easyForms = easyForm, "Guardar", id) {
                        val lista = easyForm.formData()



                        person.nombres = (lista.getOrNull(0) as? EasyFormsResult.StringResult)?.value ?: ""
                        person.apellidos = (lista.getOrNull(1) as? EasyFormsResult.StringResult)?.value ?: ""
                        person.correo = (lista.getOrNull(2) as? EasyFormsResult.StringResult)?.value ?: ""
                        person.estado = (lista.getOrNull(3) as? EasyFormsResult.StringResult)?.value ?: ""
                        person.dni = (lista.getOrNull(4) as? EasyFormsResult.StringResult)?.value ?: ""
                        person.offlinex = (lista.getOrNull(5) as? EasyFormsResult.StringResult)?.value ?: ""
                        person.password = (lista.getOrNull(6) as? EasyFormsResult.StringResult)?.value ?: ""
                        person.perfilPrin = (lista.getOrNull(7) as? EasyFormsResult.StringResult)?.value ?: ""

                        if (id == 0.toLong()) {
                            Log.i("AGREGAR", "Nombre:" + person.nombres)
                            Log.i("AGREGAR", "Apellido:" + person.apellidos)
                            Log.i("AGREGAR", "Correo:" + person.correo)
                            Log.i("AGREGAR", "Estado:" + person.estado)
                            Log.i("AGREGAR", "Offlinex:" + person.offlinex)
                            Log.i("AGREGAR", "Password:" + person.password)
                            Log.i("AGREGAR", "PerfilPrin:" + person.perfilPrin)
                            viewModel.addUsuario(person)
                        } else {
                            person.id = id
                            Log.i("MODIFICAR", "M:" + person)
                            viewModel.editUsuario(person)
                        }
                        navController.navigate(Destinations.UsuarioUI.route)
                    }
                    Spacer()
                    AccionButtonCancel(easyForms = easyForm, "Cancelar") {
                        navController.navigate(Destinations.UsuarioUI.route)
                    }
                }

            }
        }
    }
}


fun splitCadena(data:String):String{
    return if(data!="") data.split("-")[0] else ""
}