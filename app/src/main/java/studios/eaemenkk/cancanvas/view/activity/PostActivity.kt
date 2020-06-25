package studios.eaemenkk.cancanvas.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_post.*
import studios.eaemenkk.cancanvas.R


class PostActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        fabConfirm.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        }
    }
    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
//            val baseApollo = BaseApollo(this, getString(R.string.api_base_url), getString(R.string.api_subscription_url))
//
//            val f = File(cacheDir, )
//            f.createNewFile()
//
//            val bos = ByteArrayOutputStream()
//            val photo = MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data).compress(Bitmap.CompressFormat.PNG, 0, bos)
//
//            val bitmapdata = bos.toByteArray()
//            val fos = FileOutputStream(f)
//
//            fos.write(bitmapdata)
//            fos.flush()
//            fos.close()
//
//            baseApollo.apolloClient.mutate(CreatePostMutation(fos, Input.optional("a")))
//                .enqueue(object : ApolloCall.Callback<CreatePostMutation.Data>() {
//                    override fun onFailure(e: ApolloException) {
//                        println("Apollo Error $e")
//                    }
//                    override fun onResponse(response: Response<CreatePostMutation.Data>) {
//                        println(response.errors?.get(0)?.message)
//                    }
//                })
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}