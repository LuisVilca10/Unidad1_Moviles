package pe.edu.upeu.asistenciaupeujc.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import pe.edu.upeu.asistenciaupeujc.data.local.dao.ActividadDao
import pe.edu.upeu.asistenciaupeujc.data.local.dao.UsuarioDao
import pe.edu.upeu.asistenciaupeujc.modelo.Actividad
import pe.edu.upeu.asistenciaupeujc.modelo.Usuario

@Database(entities = [Actividad::class], version = 1)
abstract class DbDataSource:RoomDatabase() {
    abstract fun actividadDao():ActividadDao
}

@Database(entities = [Usuario::class], version = 1)
abstract class DbDataSource1:RoomDatabase() {
    abstract fun usuariodao():UsuarioDao
}