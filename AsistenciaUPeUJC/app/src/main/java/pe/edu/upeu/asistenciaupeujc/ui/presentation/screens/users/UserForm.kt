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
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.form.DatePickerCustom
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.form.DropdownMenuCustom
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.form.MyFormKeys
import pe.edu.upeu.asistenciaupeujc.ui.presentation.components.form.NameTextField
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
                NameTextField(easyForms = easyForm, text =usuario?.nombres!!,"Nomb. Usuario:", MyFormKeys.NAME )
                var listE = listOf(
                    ComboModel("Activo","Activo"),
                    ComboModel("Desactivo","Desactivo"),
                )
                ComboBox(easyForm = easyForm, "Estado:", usuario?.estado!!, listE)

                var listEv = listOf(
                    ComboModel("SI","SI"),
                    ComboModel("NO","NO"),
                )
                ComboBoxTwo(easyForm = easyForm, "Apellidos:", usuario?.apellidos!!, listEv)

                ComboBoxTwo(easyForm = easyForm, "Nombre:", usuario?.nombres!!, listEv)
                DropdownMenuCustom(easyForm = easyForm, label = "Correo", usuario.correo, list =listEv, MyFormKeys.EMAIL )
                DropdownMenuCustom(easyForm = easyForm, label = "DNI", usuario.dni, list =listEv, MyFormKeys.DNI )
                DropdownMenuCustom(easyForm = easyForm, label = "Estado", usuario.estado, list =listEv, MyFormKeys.ENTSAL )
                DropdownMenuCustom(easyForm = easyForm, label = "Offlinex:", usuario.offlinex, list =listEv, MyFormKeys.OFFLINE )
                DropdownMenuCustom(easyForm = easyForm, label = "Contraseña", usuario.password, list =listEv, MyFormKeys.PASSWORD )
                DropdownMenuCustom(easyForm = easyForm, label = "PerfilPrim:", usuario.perfilPrin, list =listEv, MyFormKeys.NAME )

                Row(Modifier.align(Alignment.CenterHorizontally)){
                    AccionButtonSuccess(easyForms = easyForm, "Guardar", id){
                        val lista=easyForm.formData()
                        person.nombres=(lista.get(0) as EasyFormsResult.StringResult).value
                        person.apellidos=splitCadena((lista.get(1) as EasyFormsResult.GenericStateResult<String>).value)
                        person.correo=splitCadena((lista.get(2) as EasyFormsResult.GenericStateResult<String>).value)
                        person.dni=(lista.get(3) as EasyFormsResult.GenericStateResult<String>).value
                        person.estado=(lista.get(4) as EasyFormsResult.GenericStateResult<String>).value
                        person.offlinex=(lista.get(5) as EasyFormsResult.GenericStateResult<String>).value
                        person.password=(lista.get(6) as EasyFormsResult.StringResult).value
                        person.perfilPrin= splitCadena((lista.get(7) as EasyFormsResult.GenericStateResult<String>).value)

                        if (id==0.toLong()){
                            Log.i("AGREGAR", "M:"+ person.nombres)
                            Log.i("AGREGAR", "VI:"+ person.apellidos)
                            Log.i("AGREGAR", "SA:"+ person.dni)
                            Log.i("AGREGAR", "ES:"+ person.estado)
                            Log.i("AGREGAR", "OF:"+ person.offlinex)
                            viewModel.addUsuario(person)
                        }else{
                            person.id=id
                            Log.i("MODIFICAR", "M:"+person)
                            viewModel.editUsuario(person)
                        }
                        navController.navigate(Destinations.ActividadUI.route)
                    }
                    Spacer()
                    AccionButtonCancel(easyForms = easyForm, "Cancelar"){
                        navController.navigate(Destinations.ActividadUI.route)
                    }
                }
            }
        }
    }
}


fun splitCadena(data:String):String{
    return if(data!="") data.split("-")[0] else ""
}