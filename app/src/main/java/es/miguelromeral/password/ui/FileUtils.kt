package es.miguelromeral.password.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.sqlite.db.SimpleSQLiteQuery
import es.miguelromeral.password.classes.CSVReader
import es.miguelromeral.password.classes.CSVWriter
import es.miguelromeral.password.classes.Password
import es.miguelromeral.password.classes.database.PasswordDatabase
import es.miguelromeral.password.classes.database.PasswordDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.io.File
import java.io.FileReader
import java.io.FileWriter

private const val FILENAME = "mypasswords.csv"
private const val TAG = "FileUtils"


private fun openFile(context: Context, name: String? = FILENAME): File {
    val exportDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.getAbsolutePath(), "")
    if (!exportDir.exists()) {
        exportDir.mkdirs()
    }
    return File(exportDir, name)
}

fun executeExportSecrets(context: Context, db: PasswordDatabase): Uri?{
    val file = openFile(context)
    try {
        file.createNewFile()
        val csvWrite = CSVWriter(FileWriter(file))
        val curCSV = db.query("SELECT word,category,level,language,hints FROM password_table", null)
        csvWrite.writeNext(curCSV.getColumnNames())
        while (curCSV.moveToNext()) {
            //Which column you want to exprort
            val columnCounts = curCSV.columnCount
            val arrStr  = Array<String>(columnCounts){String()}
            for (i in 0 until columnCounts)
                arrStr[i] = curCSV.getString(i)
            csvWrite.writeNext(arrStr)
        }
        csvWrite.close()
        curCSV.close()
        return Uri.parse(file.absolutePath)
    } catch (sqlEx: Exception) {
        Log.e(TAG, sqlEx.message, sqlEx)
    }
    return null
}




fun actionViewFile(context:Context, uri: Uri): Intent {
    var file = File(uri.path)

    var auth = context.getApplicationContext().getPackageName()

    var nu = FileProvider.getUriForFile(context,
            auth + ".provider",
            file)

    var myIntent = Intent(Intent.ACTION_VIEW)
    /*myIntent.setDataAndType(
        nu,
        MimeTypeMap.getSingleton().getMimeTypeFromExtension("csv")
    )*/
    myIntent.data = nu
    myIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
    return myIntent
}



suspend fun importSecretsFU(context: Context, db: PasswordDatabaseDao): Boolean {
    try {

        val file = openFile(context)
        val csvReader = CSVReader(FileReader(file))

        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)

        var count = 0
        var columns = StringBuilder()
        var value = StringBuilder()

        var keep = true

        while(keep){
            var nextLine = csvReader.readNext()
            if(nextLine == null){
                keep = false
                break
            }
            var i = 0
            while(i < nextLine.size){
                if(count == 0){
                    if(i == nextLine.size - 1)
                        columns.append(nextLine[i])
                    else
                        columns.append(nextLine[i]).append(",")
                } else {
                    if(i == nextLine.size - 1)
                        value.append("'").append(nextLine[i]).append("'")
                    else
                        value.append("'").append(nextLine[i]).append("',")
                }

                i += 1
            }

            Log.d(TAG, "${columns}-------${value}")
            if(count != 0){
                val query = SimpleSQLiteQuery("Insert INTO password_table (" + columns + ") values(" + value +")")
                Log.i(TAG, "Query: ${query.sql}")
                try {
                    val s = db.insertDataRawFormat(query)
                    Log.i(TAG, "Success: $s")
                }catch (e: java.lang.Exception){
                    Log.i(TAG, "Excepcion while inserting: ${e.message}")
                    e.printStackTrace()
                }
                value = StringBuilder()
            }

            count += 1
        }

        return count != 1

    } catch (sqlEx: Exception) {
        Log.e(TAG, sqlEx.message, sqlEx)
    }
    return false
}