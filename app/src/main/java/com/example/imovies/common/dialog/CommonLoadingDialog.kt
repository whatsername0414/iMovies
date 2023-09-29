package com.example.imovies.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.example.imovies.R
import com.example.imovies.databinding.DialogCommonLoadingBinding

class CommonLoadingDialog(context: Context) : Dialog(context, R.style.CommonAlertDialogTheme) {

    private var binding: DialogCommonLoadingBinding? = null
    private var message: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogCommonLoadingBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        setCancelable(false)
        setMessage(message)
    }

    fun setMessage(message: String) {
        this.message = message
        binding?.messageTextView?.text =
            message.ifEmpty { context.getString(R.string.common_loading) }
    }
}