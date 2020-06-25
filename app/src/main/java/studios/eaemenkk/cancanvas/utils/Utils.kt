package studios.eaemenkk.cancanvas.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.loader.content.CursorLoader
import com.apollographql.apollo.api.FileUpload
import studios.eaemenkk.cancanvas.R
import java.io.File
import java.io.FileOutputStream
import kotlin.math.abs
import kotlin.math.floor

class Utils(private val context: Context) {

    fun createFileUploadFromUri(uri: Uri): FileUpload? {
        val filePath = getPathFromUri(uri)
        val ext = filePath?.let { getExtensionFromPath(it) }
        val file = File.createTempFile("image", ".$ext")
        file.deleteOnExit()
        val out = FileOutputStream(file)
        context.contentResolver.openInputStream(uri)?.copyTo(out)
        val type = context.contentResolver.getType(uri)
        return type?.let { FileUpload(it, file.path) }
    }

    private fun getPathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(context, uri, projection, null, null, null)
        val cursor = loader.loadInBackground()
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return cursor?.getString(columnIndex!!)
    }

    private fun getExtensionFromPath(path: String): String {
        val regex = """(.+)/(.+)\.(.+)""".toRegex()
        val matchResult = regex.matchEntire(path)
        return if (matchResult != null) {
            val (_, _, extension) = matchResult.destructured
            extension
        } else ""
    }

    fun timestampToTimeInterval(timestamp: String): String {
        val currentTimestamp = System.currentTimeMillis() / 1000
        val timestampDiff = currentTimestamp - timestamp.toLong()

        val ago = context.getString(R.string.ago)
        val word: String
        var divider = 1

        if(timestampDiff < 60){
            word = context.getString(R.string.second)
        } else if(timestampDiff < 3600) {
            word = context.getString(R.string.minute)
            divider = 60
        }
        else if (timestampDiff < 86400) {
            word = context.getString(R.string.hour)
            divider = 3600
        }
        else if (timestampDiff < 604800) {
            word = context.getString(R.string.day)
            divider = 86400
        }
        else if (timestampDiff < 2419200) {
            word = context.getString(R.string.week)
            divider = 604800
        }
        else if (timestampDiff < 29030400) {
            word = context.getString(R.string.month)
            divider = 2419200
        }
        else {
            word = context.getString(R.string.year)
            divider = 29030400
        }

        val unit = abs(floor(timestampDiff.toDouble()/divider).toInt())

        return if(unit <= 1){
            "$unit $word $ago"
        } else {
            "$unit ${word}s $ago"
        }

    }
}