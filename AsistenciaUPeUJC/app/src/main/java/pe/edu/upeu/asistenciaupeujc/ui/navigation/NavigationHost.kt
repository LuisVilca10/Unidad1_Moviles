package pe.edu.upeu.asistenciaupeujc.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import pe.edu.upeu.asistenciaupeujc.ui.presentation.screens.Pantalla1
import pe.edu.upeu.asistenciaupeujc.ui.presentation.screens.Pantalla2
import pe.edu.upeu.asistenciaupeujc.ui.presentation.screens.Pantalla3
import pe.edu.upeu.asistenciaupeujc.ui.presentation.screens.Pantalla4
import pe.edu.upeu.asistenciaupeujc.ui.presentation.screens.Pantalla5
import pe.edu.upeu.asistenciaupeujc.ui.presentation.screens.actividad.ActividadForm
import pe.edu.upeu.asistenciaupeujc.ui.presentation.screens.actividad.ActividadUI
import pe.edu.upeu.asistenciaupeujc.ui.presentation.screens.login.LoginScreen
import pe.edu.upeu.asistenciaupeujc.ui.presentation.screens.users.UserForm
import pe.edu.upeu.asistenciaupeujc.ui.presentation.screens.users.UsuarioUI

@Composable
fun NavigationHost(
    navController: NavHostController,
    darkMode: MutableState<Boolean>,
    modif:PaddingValues
) {

    NavHost(
        navController = navController, startDestination = Destinations.Login.route
    ) {
        composable(Destinations.Login.route){
            LoginScreen(navigateToHome = { navController.navigate(Destinations.Pantalla1.route)})
        }

        composable(Destinations.Pantalla1.route) {
            Pantalla1(
                navegarPantalla2 = { newText ->navController.navigate(Destinations.Pantalla2.createRoute(newText))
                }
            )
        }
        composable( Destinations.Pantalla2.route,
            arguments = listOf(navArgument("newText") {
                defaultValue = "Pantalla 2"
            })
        ) { navBackStackEntry ->
            var newText =
                navBackStackEntry.arguments?.getString("newText")
            requireNotNull(newText)
            Pantalla2(newText, darkMode)
        }
        composable(Destinations.Pantalla3.route) { Pantalla3() }
        composable(Destinations.Pantalla4.route) { Pantalla4() }

        composable(Destinations.Pantalla5.route) { Pantalla5() }

        composable(Destinations.ActividadUI.route){
            ActividadUI(navegarEditarAct = {newText->navController.navigate(Destinations.ActividadForm.passId(newText))}, navController =navController )
        }
        composable(Destinations.UsuarioUI.route){
            UsuarioUI(navegarEditarUsuario = {newText->navController.navigate(Destinations.UserForm.passId(newText))}, navController =navController )
        }

        composable(Destinations.ActividadForm.route, arguments = listOf(navArgument("actId"){
            defaultValue="actId"
        })){
            navBackStackEntry -> var actId=navBackStackEntry.arguments?.getString("actId")
            requireNotNull(actId)
            ActividadForm(text = actId, darkMode = darkMode, navController =navController )
        }

        composable(Destinations.UserForm.route, arguments = listOf(navArgument("usuId"){
            defaultValue="usuId"
        })){
                navBackStackEntry -> var usuId=navBackStackEntry.arguments?.getString("usuId")
            requireNotNull(usuId)
            UserForm(text = usuId, darkMode = darkMode, navController =navController )
        }
    }

}