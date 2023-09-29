package com.example.imovies.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.imovies.R
import com.example.imovies.common.setSingleClickListener
import com.example.imovies.databinding.DialogCommonBinding

class CommonDialog(
    context: Context, lifecycleOwner: LifecycleOwner?
) : Dialog(context, R.style.CommonAlertDialogTheme) {

    private var binding: DialogCommonBinding? = null
    private var dialogCancelable: Boolean = true
    private var message: String = ""
    private var title: String = ""
    private var button: String = ""
    private var titleColorRes: Int = -1
    private var iconRes: Int = -1
    private var isDismissedFromButton = false

    init {
        lifecycleOwner?.lifecycle?.addObserver(ParentObserver())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogCommonBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        setCancelable(dialogCancelable)
        setIcon(iconRes)
        setTitle(title)
        setTitleColor(titleColorRes)
        setMessage(message)
        setButton(button)
        binding?.okButton?.setSingleClickListener {
            isDismissedFromButton = true
            dismiss()
        }
    }

    fun setButton(button: String): CommonDialog {
        this.button = button
        binding?.okButton?.text = button.ifEmpty {
            binding?.okButton?.context?.getString(R.string.common_retry)
        }
        return this
    }

    fun setDefaultIcon(): CommonDialog {
        return setIcon(R.drawable.ic_no_connection)
    }

    fun setIcon(iconRes: Int): CommonDialog {
        this.iconRes = iconRes
        binding?.iconImageView?.let { imageView ->
            imageView.setImageDrawable(
                ActivityCompat.getDrawable(
                    imageView.context, if (iconRes == -1) {
                        R.drawable.ic_no_connection
                    } else {
                        iconRes
                    }
                )
            )
        }
        return this
    }

    fun setTitle(title: String): CommonDialog {
        this.title = title
        binding?.titleTextView?.text = title
        return this
    }

    fun setTitleColor(titleColorRes: Int): CommonDialog {
        this.titleColorRes = titleColorRes
        binding?.titleTextView?.let { textView ->
            textView.setTextColor(
                ActivityCompat.getColor(
                    textView.context, if (titleColorRes == -1) {
                        R.color.primary_27E
                    } else {
                        titleColorRes
                    }
                )
            )
        }
        return this
    }


    fun setMessage(message: String): CommonDialog {
        this.message = message
        binding?.messageTextView?.text =
            message.ifEmpty { context.getString(R.string.common_error_message) }

        return this
    }

    fun setDialogCancelable(cancelable: Boolean): CommonDialog {
        this.dialogCancelable = cancelable
        return this
    }

    fun setDialogDismissListener(listener: (isDismissedFromButton: Boolean) -> Unit): CommonDialog {
        this.setOnDismissListener {
            listener.invoke(isDismissedFromButton)
        }
        return this
    }

    override fun onStart() {
        super.onStart()
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun setCancelable(cancelable: Boolean) {
        super.setCancelable(cancelable)
        setCanceledOnTouchOutside(cancelable)
    }

    override fun show() {
        super.show()
        isDismissedFromButton = false
    }

    inner class ParentObserver : DefaultLifecycleObserver {

        override fun onDestroy(owner: LifecycleOwner) {
            if (isShowing) {
                dismiss()
            }
            super.onDestroy(owner)
        }
    }

    companion object {
        fun with(activity: AppCompatActivity): CommonDialog {
            return with(activity, activity)
        }

        fun with(fragment: Fragment): CommonDialog {
            return with(fragment.requireContext(), fragment.viewLifecycleOwner)
        }

        fun with(context: Context, lifecycleOwner: LifecycleOwner? = null): CommonDialog {
            return CommonDialog(context, lifecycleOwner)
        }
    }
}