package ru.netology.nmedia.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.edit.setText(intent.getStringExtra(KEY_TEXT))
        binding.ok.setOnClickListener {
            if (binding.edit.text.isNullOrBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                val content = binding.edit.text.toString()
                setResult(RESULT_OK, Intent().apply { putExtra(KEY_TEXT, content) })
            }
            finish()
        }
    }

    companion object {
        const val KEY_TEXT = "Text"
    }
}

object NewPostContract : ActivityResultContract<String?, String?>() {
    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(context, NewPostActivity::class.java).putExtra(
            NewPostActivity.KEY_TEXT,
            input
        )
    }

    override fun parseResult(resultCode: Int, intent: Intent?) =
        intent?.getStringExtra(NewPostActivity.KEY_TEXT).toString()

}